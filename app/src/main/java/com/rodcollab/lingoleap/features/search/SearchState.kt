package com.rodcollab.lingoleap.features.search

import com.rodcollab.lingoleap.core.networking.dictionary.model.InfoWord
import com.rodcollab.lingoleap.features.word.WordItemUiState

data class SearchState(
    val query: String = "",
    val isHintVisible: Boolean = false,
    val isSearching: Boolean = false,
    val words: List<WordItemUiState> = emptyList(),
)

data class Word(
    val name: String,
    val arrayInformation: List<InfoWord>,
)
