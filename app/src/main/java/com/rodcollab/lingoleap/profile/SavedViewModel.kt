package com.rodcollab.lingoleap.profile

import androidx.lifecycle.*
import kotlinx.coroutines.launch

class SavedViewModel(
    private val wordUseCase: WordsSavedUseCase,
) : ViewModel() {


    private val _state: MutableLiveData<UiState> by lazy {
        MutableLiveData<UiState>(UiState(list = emptyList()))
    }

    val state: LiveData<UiState> = _state

    fun showList() {
        viewModelScope.launch {
            _state.value = _state.value?.copy(list = wordUseCase().map {
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
