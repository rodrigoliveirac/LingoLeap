package com.rodcollab.lingoleap

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rodcollab.lingoleap.collections.history.repository.SearchHistory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(private val searchHistory: SearchHistory) : ViewModel() {


    private val _state by lazy {
        MutableStateFlow(UiState(list = emptyList()))
    }

    val state: StateFlow<UiState> = _state

    fun onResume() {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                list = searchHistory.getList().map {
                    SearchedWordItemState(
                        it.name,
                        it.audio,
                    )
                })
        }
    }

    data class UiState(val list: List<SearchedWordItemState>)

}

data class SearchedWordItemState(
    val name: String,
    val audio: String,
)