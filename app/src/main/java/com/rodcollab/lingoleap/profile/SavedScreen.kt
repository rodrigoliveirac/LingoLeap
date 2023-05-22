package com.rodcollab.lingoleap.profile

import android.media.AudioAttributes
import android.media.MediaPlayer
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.rodcollab.lingoleap.R
import com.rodcollab.lingoleap.search.setIcon
import kotlinx.coroutines.delay

@Composable
fun SavedScreen(navController: NavController, modifier: Modifier, viewModel: SavedViewModel) {

    val state by viewModel.state.observeAsState()

    Scaffold(
        modifier = modifier.fillMaxWidth(),
        topBar = {
            TopAppBar(title = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { },
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Saved Words")
                }
            }, navigationIcon = {
                IconButton(
                    onClick = { navController.navigateUp() }
                ) {
                    Icon(
                        painterResource(id = R.drawable.ic_arrow_back),
                        contentDescription = "Arrow Back",
                    )
                }
            })
        },
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(paddingValues)
        ) {
            state?.let { uiState ->
                items(uiState.list) { savedWord ->
                    SavedWordItem(wordItemUiState = savedWord, onClick = { TODO() })
                }
            }
        }
    }
}

@Composable
fun SavedWordItem(
    wordItemUiState: SavedWordItemState,
    onClick: () -> Unit,
) {


    val audioAttributes = AudioAttributes.Builder()
        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
        .setUsage(AudioAttributes.USAGE_MEDIA)
        .build()


    var progress by remember { mutableStateOf(0f) }

    var isPlaying by remember { mutableStateOf(false) }
    val mediaPlayer by remember { mutableStateOf(MediaPlayer()) }

    LaunchedEffect(wordItemUiState.audio) {

        mediaPlayer.apply {
            setDataSource(wordItemUiState.audio)
            setAudioAttributes(audioAttributes)
            prepare()
        }
    }
    LaunchedEffect(Unit) {

        isPlaying = mediaPlayer.isPlaying

    }

    if (isPlaying) {
        LaunchedEffect(Unit) {
            while (true) {
                progress = mediaPlayer.currentPosition.toFloat()
                delay(1000 / 30)
            }
        }
    }


    LaunchedEffect(mediaPlayer) {
        mediaPlayer.setOnCompletionListener {
            progress = 0f
            isPlaying = false
        }
    }


    Card(
        Modifier
            .sizeIn()
            .background(Color.White, RoundedCornerShape(8.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = wordItemUiState.name, fontSize = 24.sp)
            Spacer(Modifier.size(8.dp))
            Text(
                text = wordItemUiState.meaning,
                fontSize = 16.sp,
                color = Color.Gray,
                fontStyle = FontStyle.Italic
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .align(Alignment.Start)
            ) {
                IconButton(
                    onClick = {
                        isPlaying = if (isPlaying) {
                            mediaPlayer.pause()
                            false
                        } else {
                            mediaPlayer.start()
                            true
                        }
                    }
                ) {
                    Icon(
                        painter = painterResource(id = setIcon(isPlaying)),
                        contentDescription = "Play",
                        tint = MaterialTheme.colors.primary
                    )
                }

                Slider(
                    value = progress,
                    onValueChange = { progress = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = SliderDefaults.colors(
                        thumbColor = MaterialTheme.colors.primary
                    ),
                    valueRange = 0f..mediaPlayer.duration.toFloat(),
                )
            }

        }
    }
}