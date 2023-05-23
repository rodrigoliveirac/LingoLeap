package com.rodcollab.lingoleap.history

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun HistoryScreen(modifier: Modifier, viewModel: HistoryViewModel) {

    val state by viewModel.state.collectAsState()

    DisposableEffect(viewModel) {
        viewModel.onResume()
        onDispose {  }
    }

    Column(modifier = modifier) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(state.list) { word ->
                SearchWordItem(
                    wordItemUiState = word,
                    onClick = {},
                    modifier = Modifier.fillMaxWidth()
                )
            }

        }
    }

}

@Composable
fun SearchWordItem(
    wordItemUiState: SearchedWordItemState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.surface)
            .clickable { onClick() },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = modifier.padding(16.dp),
            text = wordItemUiState.name,
            style = MaterialTheme.typography.body1,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

    }
}