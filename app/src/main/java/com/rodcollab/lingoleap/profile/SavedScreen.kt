package com.rodcollab.lingoleap.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun SavedScreen(modifier: Modifier, viewModel: SavedViewModel) {

    val state by viewModel.state.observeAsState()

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        state?.let {
            items(it.list) { savedWord ->
                SavedWordItem(wordItemUiState = savedWord, onClick = { /*TODO*/ })
            }
        }
    }
}

@Composable
fun SavedWordItem(
    wordItemUiState: SavedWordItemState,
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