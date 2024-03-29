package com.rodcollab.lingoleap.features.word.detail

import android.media.AudioAttributes
import android.media.MediaPlayer
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rodcollab.lingoleap.GetWordDetailsUseCase
import com.rodcollab.lingoleap.features.GetLanguages
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

data class WordDetailsUiState(
    val isLoading: Boolean = true,
    val word: String = "",
    val audio: String = "",
    val marked: Int = -1,
    val partOfSpeech: String = "",
    val partOfSpeeches: List<String> = listOf(),
    val definitionsAndExamples: List<DefinitionDomain> = listOf(),
    val songs: List<SongDomain> = listOf(),
    val translatedText: String = "",
    val textToTranslate: String = "",
    val languages: List<LanguageOption> = listOf()
)

data class SongDomain(
    val word: String = "",
    val title: String,
    val thumbnailUrl: String
)

data class DefinitionDomain(
    val definition: String,
    val example: String
)

@HiltViewModel
class WordDetailsViewModel @Inject constructor(
    private val translator: TranslatorUseCase,
    private val languages: GetLanguages,
    private val songs: GetSongsUseCase,
    private val getMeaningsUseCase: GetMeaningsUseCase,
    private val getWordUseCase: GetWordDetailsUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _wordId = savedStateHandle.get<String>("word").orEmpty()

    init {
        viewModelScope.launch(Dispatchers.IO) {

            val flow = flow {
                emit(getWordUseCase(_wordId))
            }
            val songs = flow {
                emit(songs(_wordId))
            }

            flow.collect { word ->
                songs.collect { songs ->
                    _uiState.update {
                        WordDetailsUiState(
                            isLoading = false,
                            word = _wordId,
                            audio = word.audio,
                            marked = word.marked,
                            partOfSpeech = word.partOfSpeech[0],
                            partOfSpeeches = word.partOfSpeech,
                            definitionsAndExamples = getMeaningsUseCase(
                                _wordId,
                                word.partOfSpeech[0]
                            ),
                            songs = songs,
                            languages = emptyList()
                        )
                    }
                }

            }
        }
    }

    private val _uiState = MutableStateFlow(WordDetailsUiState())

    val uiState: StateFlow<WordDetailsUiState> = _uiState.asStateFlow()

    fun getDefinitionsBy(partOfSpeech: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update {
                WordDetailsUiState(
                    word = it.word,
                    audio = it.audio,
                    partOfSpeech = partOfSpeech,
                    partOfSpeeches = it.partOfSpeeches,
                    definitionsAndExamples = getMeaningsUseCase(_wordId, partOfSpeech),
                    isLoading = false,
                    songs = it.songs
                )
            }
        }
    }

    fun translate(targetSource: String, text: String) {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                _uiState.value = _uiState.value.copy(translatedText = "")
            }
            val translatedText = translator(targetSource, text)
            _uiState.value = _uiState.value.copy(
                languages = languages(),
                translatedText = translatedText,
                textToTranslate = text
            )
        }
    }

    fun playAudio() {
        viewModelScope.launch(Dispatchers.IO) {
            MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                )
                setDataSource(uiState.value.audio)
                prepare()
                start()

            }.setOnCompletionListener {
                it.release()
            }
        }
    }

    fun markWord() {
        viewModelScope.launch(Dispatchers.IO) {
            getWordUseCase.onToggleMark(_wordId)
            _uiState.update {
                WordDetailsUiState(
                    isLoading = it.isLoading,
                    word = it.word,
                    audio = it.audio,
                    marked = getWordUseCase(_wordId).marked,
                    partOfSpeech = it.partOfSpeech,
                    partOfSpeeches = it.partOfSpeeches,
                    definitionsAndExamples = it.definitionsAndExamples,
                    songs = it.songs
                )
            }
        }
    }

}

data class LanguageOption(
    val code: String,
    val name: String
)