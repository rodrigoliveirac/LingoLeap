package com.rodcollab.lingoleap.features.word.search

import com.rodcollab.lingoleap.core.networking.dictionary.model.Meaning
import com.rodcollab.lingoleap.core.networking.dictionary.model.InfoWord
import com.rodcollab.lingoleap.features.word.WordItemUiState

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
    val arrayInformation: List<InfoWord>,
    val saved: Boolean
)

data class WordSaved(
    val name: String
)
