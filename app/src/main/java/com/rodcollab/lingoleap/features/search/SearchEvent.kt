package com.rodcollab.lingoleap.features.search

sealed class SearchEvent {
    data class OnQueryChange(val query: String) : SearchEvent()
    object OnSearch : SearchEvent()
    object OnClearHistory : SearchEvent()
    data class OnSearchFocusChange(val isFocused: Boolean) : SearchEvent()
}
