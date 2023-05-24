package com.rodcollab.lingoleap.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.rodcollab.lingoleap.saved.WordsSavedRepository
import com.rodcollab.lingoleap.search.domain.GetWordUseCase
import com.rodcollab.lingoleap.search.domain.GetWordUseCaseImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchViewModel(
    private val searchHistory: SearchHistory,
    private val wordsSavedRepository: WordsSavedRepository
) : ViewModel() {

    private var listCache = mutableListOf<WordItemUiState>()

    private var listCacheFlow = MutableStateFlow(listCache.map { it.copy() })

    private val saveWord: SaveWord by lazy {
        SaveWordImpl(wordsSavedRepository)
    }

    private val getWord: GetWordUseCase by lazy {
        GetWordUseCaseImpl(wordsSavedRepository)
    }

    private val _state by lazy {
        MutableStateFlow(
            SearchState(
                infoItem = InfoItemClicked(),
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
            is SearchEvent.OnWordClick -> {

                _state.value.infoItem.let {
                    _state.value.infoItem = it.copy(
                        word = event.word.name,
                        meanings = event.word.arrayInformation[0].meanings,
                        saved = event.word.saved
                    )
                }

                event.word.arrayInformation[0].phonetics.onEach {
                    if (it.audio?.isNotBlank() == true) {
                        _state.value.let { currentState ->
                            _state.value = currentState.copy(openDialog = true)
                            _state.value.infoItem = currentState.infoItem.copy(audio = it.audio)
                        }

                        _state.value.infoItem.let { infoItemClicked ->
                            _state.value.infoItem = infoItemClicked.copy(
                                audio = infoItemClicked.audio,
                            )
                        }

                    }
                }

            }
            is SearchEvent.OnSearchFocusChange -> {
                _state.value = _state.value.copy(
                    isHintVisible = !event.isFocused && _state.value.query.isBlank()
                )
            }
            is SearchEvent.OpenDialog -> {
                _state.value = _state.value.copy(
                    openDialog = event.openDialog
                )
            }
            is SearchEvent.OnSaveWord -> {
                viewModelScope.launch {
                    saveWord(event.word.word)
                    Log.d("savedWords", wordsSavedRepository.getSavedWords().toString())

                    _state.value.infoItem.let { itemClicked ->
                        val saved = withContext(Dispatchers.IO) {
                            wordsSavedRepository.getSavedWords()
                                .any { itemClicked.word == it.name }
                        }
                        _state.value = SearchState(
                            query = _state.value.query,
                            isHintVisible = _state.value.isHintVisible,
                            isSearching = _state.value.isSearching,
                            words = _state.value.words,
                            openDialog = _state.value.openDialog,
                            infoItem = InfoItemClicked(
                                word = itemClicked.word,
                                meanings = itemClicked.meanings,
                                audio = itemClicked.audio,
                                saved = saved
                            )
                        )
                    }

                }
            }
        }
    }

    private fun executeSearch() {
        viewModelScope.launch {

            withContext(Dispatchers.Main) {
                _state.value = _state.value.copy(
                    isSearching = true,
                    words = listCacheFlow.value.reversed()
                )
            }

            withContext(Dispatchers.IO) {
                getWord(_state.value.query).map { infoItem ->
                    listCache.add(WordItemUiState(element = infoItem))
                    listCacheFlow.value = listCache
                    searchHistory.add(infoItem)
                }
            }

            withContext(Dispatchers.Main) {
                _state.value = _state.value.copy(
                    words = listCacheFlow.value.reversed(),
                    isSearching = false,
                    query = ""
                )
            }
        }
    }

    fun onResume() {
        viewModelScope.launch {
            _state.value.infoItem.let { itemClicked ->
                _state.value = SearchState(
                    query = _state.value.query,
                    isHintVisible = _state.value.isHintVisible,
                    isSearching = _state.value.isSearching,
                    words = _state.value.words,
                    openDialog = _state.value.openDialog,
                    infoItem = InfoItemClicked(
                        word = itemClicked.word,
                        meanings = itemClicked.meanings,
                        audio = itemClicked.audio,
                        saved = wordsSavedRepository.getSavedWords()
                            .any { itemClicked.word == it.name })
                )
            }
        }
    }

    class MyViewModelFactory(
        private val searchHistory: SearchHistory,
        private val wordsSavedRepository: WordsSavedRepository
    ) :
        ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            SearchViewModel(searchHistory, wordsSavedRepository) as T
    }

    override fun onCleared() {
        super.onCleared()
        listCache.clear()
    }
}