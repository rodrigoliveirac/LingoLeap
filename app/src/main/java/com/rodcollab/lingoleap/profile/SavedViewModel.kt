package com.rodcollab.lingoleap.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SavedViewModel(
    private val wordUseCase: WordsSavedUseCase,
) : ViewModel() {


    private val _state = MutableStateFlow<UiState>(UiState())
    val wordList: StateFlow<UiState> = _state

    fun showList() {
        viewModelScope.launch {
            _state.value = _state.value.copy(list = wordUseCase().map {
                SavedWordItemState(it.name, it.meaning, it.audio)
            }.reversed())
        }
    }

    class SavedViewModelFactory(private val wordUseCase: WordsSavedUseCase) :
        ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            SavedViewModel(wordUseCase) as T
    }
}

data class UiState(
    val list: List<SavedWordItemState> = emptyList()
)

data class SavedWordItemState(
    val name: String,
    val meaning: String,
    val audio: String
)
