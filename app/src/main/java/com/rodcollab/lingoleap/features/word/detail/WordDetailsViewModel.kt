package com.rodcollab.lingoleap.features.word.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rodcollab.lingoleap.features.word.translation.TranslationRepository
import com.rodcollab.lingoleap.TranslatorApiService
import com.rodcollab.lingoleap.collections.search.domain.GetWordUseCase
import com.rodcollab.lingoleap.core.database.SearchHistoryDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

data class WordDetails(
    val word: String = "",
    val meanings: List<MeaningDomain> = listOf(),
    val audio: String = "",
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
    private val getWord: GetWordUseCase,
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

    val wordId = _wordId

    val word: StateFlow<WordDetails> = flow {

        val source = withContext(Dispatchers.IO) {
            historyDao.getWordWithMeanings(_wordId)
        }

        val audio = withContext(Dispatchers.IO) { source.word.audio }

        val meanings = withContext(Dispatchers.IO) {
            source.meanings.map { meaningEntity ->
                val definitions =
                    historyDao.getMeaningWithDefinitions(meaningEntity.meaningId).definitions.map { definitionEntity ->
                        DefinitionDomain(
                            definition = definitionEntity.definition,
                            example = definitionEntity.example
                        )
                    }
                MeaningDomain(
                    partOfSpeech = meaningEntity.partOfSpeech,
                    definitions = definitions
                )
            }
        }

        emit(WordDetails(word = _wordId, meanings = meanings, audio = audio))

    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(1000),
        initialValue = WordDetails()
    )

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

}

data class LanguageOption(
    val code: String,
    val name: String
)

data class AudioUri(
    val audioUri: String
)
