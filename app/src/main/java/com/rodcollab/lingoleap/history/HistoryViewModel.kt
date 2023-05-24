package com.rodcollab.lingoleap.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.rodcollab.lingoleap.search.SearchHistory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HistoryViewModel(private val searchHistory: SearchHistory) : ViewModel() {


    private val _state by lazy {
        MutableStateFlow(UiState(list = emptyList()))
    }

    val state: StateFlow<UiState> = _state

    fun onResume() {
        viewModelScope.launch {
            _state.value = _state.value.copy(list = searchHistory.getList().map { SearchedWordItemState(it.name,it.meaning, it.audio, it.saved, it.createdAt) })
        }
    }
    data class UiState(val list: List<SearchedWordItemState>)

    class HistoryViewModelFactory(
        private val searchHistory: SearchHistory,
    ) :
        ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            HistoryViewModel(searchHistory) as T
    }
}

data class SearchedWordItemState(
    val name: String,
    val meaning: String,
    val audio: String,
    val saved: Boolean,
    val date: String
)