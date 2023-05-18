package com.rodcollab.lingoleap.search

data class SearchState(
    val query: String = "",
    val isHintVisible: Boolean = false,
    val isSearching: Boolean = false,
    val words: List<WordItemUiState> = emptyList(),
    val openDialog: Boolean = false,
    var infoItem: InfoItemClicked,
)

data class InfoItemClicked(
    val word: String = "",
    val meaning: String = "",
    val audio: String = "",
    val saved: Boolean = false
)

data class Word(
    val name: String,
    val arrayInformation: List<com.rodcollab.lingoleap.api.model.Word>,
    val saved: Boolean
)

data class WordSaved(
    val name: String
)
