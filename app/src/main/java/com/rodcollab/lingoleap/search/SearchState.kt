package com.rodcollab.lingoleap.search

data class SearchState(
    val query: String = "",
    val isHintVisible:Boolean = false,
    val isSearching:Boolean = false,
    val words: List<WordItemUiState> = emptyList(),
    val openDialog: Boolean = false,
    val audio: String = ""
)
