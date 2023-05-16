package com.rodcollab.lingoleap.search

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.*
import com.rodcollab.lingoleap.R
import kotlinx.coroutines.*

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchScreen(modifier: Modifier, viewModel: SearchViewModel = viewModel()) {
    // on below line we are creating a variable for our audio url


    val audioUrl = "https://ssl.gstatic.com/dictionary/static/sounds/20200429/hello--_gb_1.mp3"

    val audioAttributes = AudioAttributes.Builder()
        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
        .setUsage(AudioAttributes.USAGE_MEDIA)
        .build()

    val mediaPlayer by remember { mutableStateOf(MediaPlayer()) }

    var progress by remember { mutableStateOf(0f) }

    var isPlaying by remember { mutableStateOf(false) }

    DisposableEffect(audioUrl) {


        mediaPlayer.apply {
            setDataSource(audioUrl)
            setAudioAttributes(audioAttributes)
            prepare()
        }

        onDispose {
            mediaPlayer.release()

        }
    }
    DisposableEffect(Unit) {

        isPlaying = mediaPlayer.isPlaying

        onDispose {
            isPlaying = false
        }
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


    val openDialog = remember { mutableStateOf(false) }


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
            items(state.words) { word ->
                WordItem(
                    wordItemUiState = word,
                    onClick = {
                        openDialog.value = true
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        if (openDialog.value) {

            val dialogWidth = 200.dp
            val dialogHeight = 50.dp

            Dialog(onDismissRequest = { openDialog.value = false }) {
                Box(
                    Modifier
                        .size(dialogWidth, dialogHeight)
                        .background(Color.White)
                ) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {
                                Log.d("isPlaying", isPlaying.toString())

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
                            valueRange = 0f..mediaPlayer.duration.toFloat()
                        )
                    }
                }
            }
        }
    }
}

fun setIcon(isPlaying: Boolean): Int {
    return if (isPlaying) {
        R.drawable.ic_pause
    } else {
        R.drawable.ic_play
    }
}
