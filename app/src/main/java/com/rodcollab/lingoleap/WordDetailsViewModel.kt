package com.rodcollab.lingoleap

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rodcollab.lingoleap.api.model.Meaning
import com.rodcollab.lingoleap.collections.search.domain.GetWordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _wordId = savedStateHandle.get<String>("word").orEmpty()

    val wordId = _wordId

    val word: StateFlow<WordDetails> = flow {
        val word = getWord(_wordId)

        val audioUri = word[0].arrayInformation[0].phonetics.map {
            var uri = ""
            if (it.audio?.isNotBlank() == true) {
                uri = it.audio
            }
            AudioUri(audioUri = uri)
        }[0]

        emit(
            WordDetails(
                word = _wordId,
                meanings = word[0].arrayInformation[0].meanings,
                audio = audioUri.audioUri,
            )
        )

    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000), initialValue = WordDetails())

    fun translate(langCode: String,text: String, successful: (String) -> Unit) {

        viewModelScope.launch {
            val some = translation.translate(langCode, "en", text)

            if (some.isSuccessful) {
                successful(some.body()?.data!!.translatedText)
            }
        }
    }

    private val _languages = mutableListOf<LanguageOption>()

    private val _languagesFlow = MutableStateFlow(_languages)
    val languages = _languagesFlow.value
    fun getLanguages() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = translation.getLanguages()
            if (result.isSuccessful) {
                result.body()?.data?.languages?.map {
                    _languages.add(LanguageOption(code = it.code, name = it.name))
                    _languagesFlow.value = _languages
                }
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
