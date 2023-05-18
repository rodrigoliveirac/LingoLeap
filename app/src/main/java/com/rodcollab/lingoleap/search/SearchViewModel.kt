package com.rodcollab.lingoleap.search

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rodcollab.lingoleap.saved.WordsSavedRepository
import com.rodcollab.lingoleap.saved.WordsSavedRepositoryImpl
import com.rodcollab.lingoleap.search.domain.GetWordUseCase
import com.rodcollab.lingoleap.search.domain.GetWordUseCaseImpl
import kotlinx.coroutines.launch

class SearchViewModel() : ViewModel() {

    private val saveWord : SaveWord by lazy {
        SaveWordImpl()
    }

    private val wordsSavedRepository : WordsSavedRepository by lazy {
        WordsSavedRepositoryImpl()
    }

    private val getWord: GetWordUseCase by lazy {
        GetWordUseCaseImpl()
    }

    var state by mutableStateOf(SearchState(infoItem = InfoItemClicked()))
        private set


    fun onEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.OnQueryChange -> {
                state = state.copy(query = event.query)
            }
            is SearchEvent.OnSearch -> {
                executeSearch()
            }
            is SearchEvent.OnWordClick -> {

                state.infoItem = state.infoItem.copy(
                    word = event.word.name,
                    meaning = event.word.arrayInformation[0].meanings[0].definitions[0].definition
                        ?: "no definitions for this word :(",
                    saved = event.word.saved
                )


                event.word.arrayInformation[0].phonetics.onEach {
                    if (it.audio?.isNotBlank() == true) {
                        state.let { currentState ->
                            state = currentState.copy(openDialog = true)
                            state.infoItem = currentState.infoItem.copy(audio = it.audio)
                        }
                        state.infoItem = state.infoItem.copy(
                            audio = it.audio,

                            )
                    }
                }

            }
            is SearchEvent.OnSearchFocusChange -> {
                state = state.copy(
                    isHintVisible = !event.isFocused && state.query.isBlank()
                )
            }
            is SearchEvent.OpenDialog -> {
                state = state.copy(
                    openDialog = event.openDialog
                )
            }
        }
    }

    private fun executeSearch() {
        viewModelScope.launch {

            state = state.copy(
                isSearching = true,
                words = emptyList()
            )

            state = state.copy(
                words = getWord(state.query).map { infoItem ->
                        WordItemUiState(element = infoItem)
                    },
                isSearching = false,
                query = ""
            )
        }
    }

    fun onToggleSaveWord(word: InfoItemClicked) {
        viewModelScope.launch {
           saveWord(word.word)
            Log.d("savedWords", wordsSavedRepository.getSavedWords().toString())
        }
    }
}