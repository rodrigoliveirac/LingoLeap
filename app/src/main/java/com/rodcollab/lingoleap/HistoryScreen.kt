package com.rodcollab.lingoleap

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun HistoryScreen(modifier: Modifier, toDetailsScreen: (String) -> Unit, viewModel: HistoryViewModel = hiltViewModel()) {

    val state by viewModel.state.collectAsState()

    DisposableEffect(viewModel) {
        viewModel.onResume()
        onDispose { }
    }

    Column(modifier = modifier) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(state.list) { word ->
                SearchWordItem(
                    wordItemUiState = word,
                    toDetailsScreen = toDetailsScreen,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun SearchWordItem(
    wordItemUiState: SearchedWordItemState,
    toDetailsScreen: (String) -> Unit,
    modifier: Modifier = Modifier
) {


    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.surface)
            .clickable { toDetailsScreen(wordItemUiState.name) },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier
                .padding(16.dp)
                .weight(1f),
            text = wordItemUiState.name,
            style = MaterialTheme.typography.body1,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            text = wordItemUiState.date,
            textAlign = TextAlign.End,
            fontSize = 12.sp,
            fontStyle = FontStyle.Italic,
            color = Color.Gray,
            style = MaterialTheme.typography.body1,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.size(8.dp))
    }
}


@Preview
@Composable
fun SearchWordItemPreview(modifier: Modifier = Modifier) {


    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = modifier,
            text = "Word",
            style = MaterialTheme.typography.body1,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            text = "24/05/2023",
            textAlign = TextAlign.End,
            fontSize = 12.sp,
            fontStyle = FontStyle.Italic,
            color = Color.Gray,
            style = MaterialTheme.typography.body1,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

    }


}