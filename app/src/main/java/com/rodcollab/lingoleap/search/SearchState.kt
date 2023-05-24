package com.rodcollab.lingoleap.search

import com.rodcollab.lingoleap.api.model.Meaning

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
    val meanings: List<Meaning> = listOf(),
    val audio: String = "",
    val saved: Boolean = false
)

data class Word(
    val name: String,
    val arrayInformation: List<com.rodcollab.lingoleap.api.model.InfoWord>,
    val saved: Boolean
)

data class WordSaved(
    val name: String
)
