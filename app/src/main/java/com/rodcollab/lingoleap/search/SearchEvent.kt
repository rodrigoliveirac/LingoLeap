package com.rodcollab.lingoleap.search

import com.rodcollab.lingoleap.api.model.Word

sealed class SearchEvent {
    data class OnQueryChange(val query: String) : SearchEvent()
    object OnSearch : SearchEvent()
    data class OnSearchFocusChange(val isFocused: Boolean) : SearchEvent()
    data class OnWordClick(val word: Word): SearchEvent()
    data class OpenDialog(val openDialog: Boolean) : SearchEvent()
}
