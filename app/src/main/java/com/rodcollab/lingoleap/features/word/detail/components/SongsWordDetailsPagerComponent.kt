package com.rodcollab.lingoleap.features.word.detail.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.rodcollab.lingoleap.features.word.detail.WordDetailsUiState

@Composable
fun SongsWordDetailsPagerComponent(
    wordDetailsUiState: WordDetailsUiState,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = 16.dp,
                start = 16.dp,
                end = 16.dp,
                bottom = 16.dp
            )
    ) {
        Text(
            text = "All Songs",
            color = Color.DarkGray,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, top = 16.dp, bottom = 8.dp),
            fontSize = 24.sp,
            fontWeight = FontWeight.Light,
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .padding(start = 12.dp, top = 8.dp, end = 12.dp)
                .fillMaxWidth()
        ) {
            items(wordDetailsUiState.songs) { song ->
                Column(modifier = Modifier.padding(16.dp)) {
                    Card(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .sizeIn()
                            .padding(8.dp),
                        shape = RoundedCornerShape(12.dp),
                        elevation = 8.dp
                    ) {
                        AsyncImage(
                            contentScale = ContentScale.Fit,
                            model = song.thumbnailUrl,
                            contentDescription = null
                        )
                    }
                    Text(
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        text = song.title,
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
}