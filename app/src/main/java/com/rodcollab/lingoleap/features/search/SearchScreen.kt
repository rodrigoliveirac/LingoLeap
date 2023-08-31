package com.rodcollab.lingoleap.features.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.*
import com.rodcollab.lingoleap.features.word.WordItem
import kotlinx.coroutines.*


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchScreen(
    onClickWord: (String) -> Unit,
    modifier: Modifier,
    viewModel: SearchViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsState()

    Column(
        modifier
            .fillMaxSize()
            .padding(vertical = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        SearchBar(searchValue = state.query,
            { query -> viewModel.onEvent(SearchEvent.OnQueryChange(query)) },
            onSearch = {
                viewModel.onEvent(SearchEvent.OnSearch)
            },
            searching = state.isSearching)

        if (state.words.isNotEmpty()) {
            Row(modifier = Modifier
                .padding(top = 16.dp, end = 16.dp)
                .align(Alignment.End)
                .clickable { viewModel.onEvent(SearchEvent.OnClearHistory) }) {
                Text("clear history", fontSize = 18.sp)
            }
            Spacer(modifier = Modifier.height(8.dp))
        } else {
            Spacer(modifier = Modifier.height(8.dp))
        }
        if(state.isSearching) {
            CircularProgressIndicator(
                strokeWidth = 1.dp,
                strokeCap = StrokeCap.Square,
                color = Color(255, 20, 147),
                modifier = Modifier
                    .align(Alignment.Start)
                    .size(42.dp)
                    .padding(start = 18.dp, top = 16.dp, bottom = 8.dp)
            )
        } else {
            Spacer(modifier = Modifier
                .size(40.dp)
                .padding(top = 8.dp, start = 16.dp, bottom = 8.dp))
        }
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(state.words) { word ->
                WordItem(
                    wordItemUiState = word,
                    onClick = {
                        onClickWord(word.element.name)
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
