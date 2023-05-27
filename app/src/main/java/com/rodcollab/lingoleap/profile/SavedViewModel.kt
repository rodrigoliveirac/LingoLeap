package com.rodcollab.lingoleap.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rodcollab.lingoleap.collections.saved.domain.WordsSavedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedViewModel @Inject constructor(
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
}

data class UiState(
    val list: List<SavedWordItemState> = emptyList()
)

data class SavedWordItemState(
    val name: String,
    val meaning: String,
    val audio: String
)
