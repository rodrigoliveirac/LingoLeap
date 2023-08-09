package com.rodcollab.lingoleap

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rodcollab.lingoleap.api.model.Meaning
import com.rodcollab.lingoleap.collections.search.domain.GetWordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class WordDetails(
    val word: String = "",
    val meanings: List<Meaning> = listOf(),
    val audio: String = "",
)

@HiltViewModel
class WordDetailsViewModel @Inject constructor(
    private val getWord: GetWordUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val wordId = savedStateHandle.get<String>("word").orEmpty()

    val word: StateFlow<WordDetails> = flow {
        val word = getWord(wordId)

        val audioUri = word[0].arrayInformation[0].phonetics.map {
            var uri = ""
            if (it.audio?.isNotBlank() == true) {
                uri = it.audio
            }
            AudioUri(audioUri = uri)
        }[0]

        emit(WordDetails(
            word = wordId,
            meanings = word[0].arrayInformation[0].meanings,
            audio = audioUri.audioUri
        ))
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000),WordDetails())

}

data class AudioUri(
    val audioUri: String
)
