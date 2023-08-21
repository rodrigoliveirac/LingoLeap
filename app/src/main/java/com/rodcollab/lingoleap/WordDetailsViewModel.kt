package com.rodcollab.lingoleap

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rodcollab.lingoleap.api.model.Meaning
import com.rodcollab.lingoleap.collections.search.domain.GetWordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class WordDetails(
    val word: String = "",
    val meanings: List<Meaning> = listOf(),
    val audio: String = "",
)

@HiltViewModel
class WordDetailsViewModel @Inject constructor(
    private val getWord: GetWordUseCase,
    private val translation: TranslatorApiService,
    private val repository: TranslationRepository,
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
        val word = getWord(_wordId).map {

            val audioUri = it.arrayInformation[0].phonetics.map { phonetic ->
                var uri = ""
                if (phonetic.audio?.isNotBlank() == true) {
                    uri = phonetic.audio
                }
                AudioUri(audioUri = uri)
            }[0]

            WordDetails(
                word = _wordId,
                meanings = it.arrayInformation[0].meanings,
                audio = audioUri.audioUri,
            )
        }
        emit(word.first())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000), initialValue = WordDetails())

    suspend fun translate(langCode: String, text: String, successful: (String) -> Unit) {

        viewModelScope.launch {
            val translatedText = translation.translate(langCode, "en", text)
            Log.d("langCode", langCode)
            if (translatedText.isSuccessful) {
                successful(translatedText.body()!!.data.translatedText)
            }
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
