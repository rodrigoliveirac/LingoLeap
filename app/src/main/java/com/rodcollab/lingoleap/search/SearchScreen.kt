package com.rodcollab.lingoleap.search

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.*

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchScreen(modifier: Modifier, viewModel: SearchViewModel = viewModel()) {

    val state = viewModel.state
    val keyBoardController = LocalSoftwareKeyboardController.current

    Column(
        modifier
            .fillMaxSize()
            .padding(vertical = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        SearchBar(searchValue = state.query,
            { query -> viewModel.onEvent(SearchEvent.OnQueryChange(query)) },
            shouldShowHint = state.isHintVisible,
            onSearch = {
                keyBoardController?.hide()
                viewModel.onEvent(SearchEvent.OnSearch)
            },
            onFocusChanged = {
                viewModel.onEvent(SearchEvent.OnSearchFocusChange(it.isFocused))

            })
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(state.words) { food ->
                WordItem(
                    wordItemUiState = food,
                    onClick = {

                    },

                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}