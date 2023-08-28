package com.rodcollab.lingoleap.features.word.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rodcollab.lingoleap.GetWordDetailsUseCase
import com.rodcollab.lingoleap.TranslatorApiService
import com.rodcollab.lingoleap.features.word.translation.TranslationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface ResourceUiState {

    object Loading : ResourceUiState
    data class Error(val throwable: Throwable) : ResourceUiState
    data class Success(val data: Any) : ResourceUiState
}

data class WordDetailsUiState(
    val word: String = "",
    val audio: String = "",
    val partOfSpeech: String = "",
    val partOfSpeeches: List<String> = listOf(),
    val definitionsAndExamples: List<DefinitionDomain> = listOf(),
)

data class DefinitionDomain(
    val definition: String,
    val example: String
)

@HiltViewModel
class WordDetailsViewModel @Inject constructor(
    private val translation: TranslatorApiService,
    private val repository: TranslationRepository,
    private val getMeaningsUseCase: GetMeaningsUseCase,
    private val getWordUseCase: GetWordDetailsUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _languages = MutableStateFlow(emptyList<LanguageOption>())
    val languages = _languages.asStateFlow()

    fun loadLanguages() {
        viewModelScope.launch {
            repository.loadLanguages {
                _languages.value = it
            }
        }
    }

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
                    definitionsAndExamples = getMeaningsUseCase(_wordId,dataSource.partOfSpeech[0])
                )
            }
        }
    }

    private val wordDetailsStateUi = MutableStateFlow(WordDetailsUiState())

    val word: StateFlow<WordDetailsUiState> = wordDetailsStateUi.asStateFlow()

    suspend fun translate(langCode: String, text: String, successful: (String) -> Unit) {

        viewModelScope.launch {
            /*val translatedText = translation.translate(langCode, "en", text)
            Log.d("langCode", langCode)
            if (translatedText.isSuccessful) {
                successful(translatedText.body()!!.translatedText)
            }*/
            successful("Mock Text")
        }
    }

    fun getDefinitionsBy(partOfSpeech: String) {
        viewModelScope.launch(Dispatchers.IO) {
            wordDetailsStateUi.update {
                WordDetailsUiState(
                    word = it.word,
                    audio = it.audio,
                    partOfSpeech = partOfSpeech,
                    partOfSpeeches = it.partOfSpeeches,
                    definitionsAndExamples = getMeaningsUseCase(_wordId,partOfSpeech),
                )
            }
        }
    }

}

data class DefinitionsUiState(
    val definitions: List<DefinitionDomain> = listOf()
)

data class LanguageOption(
    val code: String,
    val name: String
)

data class AudioUri(
    val audioUri: String
)
