package com.rodcollab.lingoleap.features.word.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rodcollab.lingoleap.TranslatorApiService
import com.rodcollab.lingoleap.core.database.SearchHistoryDao
import com.rodcollab.lingoleap.features.word.translation.TranslationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

data class WordDetails(
    val word: String = "",
    val audio: String = "",
    val partOfSpeeches: List<String> = listOf(),
    val partOfSpeech: String = "",
    val definitionsAndExamples: List<DefinitionDomain> = listOf(),
)

data class MeaningDomain(
    val partOfSpeech: String,
    val definitions: List<DefinitionDomain>
)

data class DefinitionDomain(
    val definition: String,
    val example: String
)

@HiltViewModel
class WordDetailsViewModel @Inject constructor(
    private val translation: TranslatorApiService,
    private val repository: TranslationRepository,
    private val historyDao: SearchHistoryDao,
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
        viewModelScope.launch {
            val source = withContext(Dispatchers.IO) { historyDao.getWordBy(_wordId) }
            val audio = withContext(Dispatchers.IO) { source.audio }
            val partOfSpeeches = withContext(Dispatchers.IO) { historyDao.getPartOfSpeeches(_wordId) }
            val definitionsAndExamples = withContext(Dispatchers.IO) {
                historyDao.definitionsBy(_wordId).map {
                    DefinitionDomain(
                        definition = it.definition,
                        example = it.example
                    )
                }
            }

            wordDetailsStateUi.value =
                wordDetailsStateUi.value.copy(
                    word = _wordId,
                    audio = audio,
                    partOfSpeeches = partOfSpeeches,
                    partOfSpeech = partOfSpeeches[0],
                    definitionsAndExamples = definitionsAndExamples
                )
        }
    }

    private val wordDetailsStateUi = MutableStateFlow(WordDetails())

    val word: StateFlow<WordDetails> = wordDetailsStateUi.asStateFlow()

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
            val definitionsAndExamples =
                historyDao.getMeaningsAndDefinitions(_wordId, partOfSpeech).map {
                    DefinitionDomain(
                        definition = it.definition,
                        example = it.example
                    )
                }
            wordDetailsStateUi.value = wordDetailsStateUi.value.copy(partOfSpeech = partOfSpeech, definitionsAndExamples = definitionsAndExamples)
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
