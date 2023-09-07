package com.rodcollab.lingoleap.features.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rodcollab.lingoleap.features.history.repository.SearchHistory
import com.rodcollab.lingoleap.features.search.domain.GetWordUseCase
import com.rodcollab.lingoleap.features.word.WordItemUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchHistory: SearchHistory,
    private val getWord: GetWordUseCase
) : ViewModel() {

    private var listCache = mutableListOf<WordItemUiState>()

    private var listCacheFlow = MutableStateFlow(listCache.map { it.copy() })

    private val _state by lazy {
        MutableStateFlow(
            SearchState(
                words = listCacheFlow.value
            )
        )
    }
    val state: StateFlow<SearchState> = _state

    fun onEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.OnQueryChange -> {
                _state.value.let {
                    _state.value = it.copy(query = event.query)
                }

            }

            is SearchEvent.OnSearch -> {
                executeSearch()
            }

            is SearchEvent.OnClearHistory -> {
                viewModelScope.launch {
                    listCache.clear()
                    _state.value.let {
                        _state.value = it.copy(words = listCache)
                    }
                }
            }

            is SearchEvent.OnSearchFocusChange -> {
                _state.value = _state.value.copy(
                    isHintVisible = !event.isFocused && _state.value.query.isBlank(),

                )
            }

        }
    }

    private fun executeSearch() {
        viewModelScope.launch {

            _state.value = _state.value.copy(
                isSearching = true
            )


            withContext(Dispatchers.IO) {
                try {
                    getWord(_state.value.query).map { infoItem ->
                        listCache.add(WordItemUiState(element = infoItem))
                        listCacheFlow.value = listCache
                        searchHistory.add(infoItem)
                    }

                } catch (e: Exception) {

                }
                _state.value = _state.value.copy(
                    words = listCacheFlow.value.reversed(),
                    isSearching = false,
                    query = ""
                )

            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        listCache.clear()
    }
}