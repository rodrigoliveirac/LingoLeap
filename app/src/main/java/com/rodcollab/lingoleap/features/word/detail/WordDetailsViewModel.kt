package com.rodcollab.lingoleap.features.word.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rodcollab.lingoleap.GetWordDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class WordDetailsUiState(
    val word: String = "",
    val audio: String = "",
    val partOfSpeech: String = "",
    val partOfSpeeches: List<String> = listOf(),
    val definitionsAndExamples: List<DefinitionDomain> = listOf(),
    val songs: List<SongDomain> = listOf()
)

data class SongDomain(
    val title: String,
    val thumbnailUrl: String
)

data class DefinitionDomain(
    val definition: String,
    val example: String
)

@HiltViewModel
class WordDetailsViewModel @Inject constructor(
    private val songs: GetSongsUseCase,
    private val getMeaningsUseCase: GetMeaningsUseCase,
    private val getWordUseCase: GetWordDetailsUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _wordId = savedStateHandle.get<String>("word").orEmpty()

    init {
        viewModelScope.launch(Dispatchers.IO) {

            val dataSource = getWordUseCase(_wordId)
            wordDetailsStateUi.update {
                WordDetailsUiState(
                    word = dataSource.word,
                    audio = dataSource.audio,
                    partOfSpeech = dataSource.partOfSpeech[0],
                    partOfSpeeches = dataSource.partOfSpeech,
                    definitionsAndExamples = getMeaningsUseCase(
                        _wordId,
                        dataSource.partOfSpeech[0]
                    ),
                    songs = emptyList()
                )
            }
        }
    }

    private val wordDetailsStateUi = MutableStateFlow(WordDetailsUiState())

    val word: StateFlow<WordDetailsUiState> = wordDetailsStateUi.asStateFlow()

    fun getDefinitionsBy(partOfSpeech: String) {
        viewModelScope.launch(Dispatchers.IO) {
            wordDetailsStateUi.update {
                WordDetailsUiState(
                    word = it.word,
                    audio = it.audio,
                    partOfSpeech = partOfSpeech,
                    partOfSpeeches = it.partOfSpeeches,
                    definitionsAndExamples = getMeaningsUseCase(_wordId, partOfSpeech),
                    songs = it.songs
                )
            }
        }
    }

    fun getSongs() {
        viewModelScope.launch {
            wordDetailsStateUi.update {
                WordDetailsUiState(
                    word = it.word,
                    audio = it.audio,
                    partOfSpeech = it.partOfSpeech,
                    partOfSpeeches = it.partOfSpeeches,
                    definitionsAndExamples = it.definitionsAndExamples,
                    songs = songs(_wordId)
                )
            }
        }
    }

}

data class LanguageOption(
    val code: String,
    val name: String
)