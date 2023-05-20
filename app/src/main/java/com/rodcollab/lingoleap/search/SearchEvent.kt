package com.rodcollab.lingoleap.search

sealed class SearchEvent {
    data class OnQueryChange(val query: String) : SearchEvent()
    object OnSearch : SearchEvent()

    object OnClearHistory : SearchEvent()
    data class OnSearchFocusChange(val isFocused: Boolean) : SearchEvent()
    data class OnWordClick(val word: Word): SearchEvent()
    data class OnSaveWord(val word: InfoItemClicked): SearchEvent()
    data class OpenDialog(val openDialog: Boolean) : SearchEvent()
}
