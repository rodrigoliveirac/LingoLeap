package com.rodcollab.lingoleap.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class SearchViewModel() : ViewModel() {

    private val service by lazy {
        DictionaryApi.retrofitService
    }


    var state by mutableStateOf(SearchState())
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
                state = state.copy(
                    audio = event.word.phonetics[0].audio,
                    openDialog = true
                )
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
                words = service
                    .getWord(state.query).map { infoItem ->
                        WordItemUiState(element = infoItem)
                    },
                isSearching = false,
                query = ""
            )
        }
    }
}