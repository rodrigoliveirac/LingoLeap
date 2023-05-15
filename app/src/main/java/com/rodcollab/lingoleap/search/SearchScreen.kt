package com.rodcollab.lingoleap.search

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.*
import com.rodcollab.lingoleap.R

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchScreen(modifier: Modifier, viewModel: SearchViewModel = viewModel()) {

    val context = LocalContext.current


    val uri = Uri.parse("https://ssl.gstatic.com/dictionary/static/sounds/20200429/hello--_gb_1.mp3")

    val mediaPlayer = remember { MediaPlayer() }
    // on below line we are creating a variable for our audio url
    var audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3"

    val audioAttributes = AudioAttributes.Builder()
        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
        .setUsage(AudioAttributes.USAGE_MEDIA)
        .build()

    mediaPlayer.setAudioAttributes(audioAttributes)

    var isPlaying = remember { mutableStateOf(false) }

    val openDialog = remember { mutableStateOf(false) }
    var progress = remember { mutableStateOf(0f) }

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

                                // on below line we are running a try and catch block
                                // for our media player.
                                try {
                                    // on below line we are setting audio source
                                    // as audio url on below line.
                                    mediaPlayer.setDataSource(audioUrl)

                                    // on below line we are preparing
                                    // our media player.
                                    mediaPlayer.prepare()


                                    progress.value = (mediaPlayer.currentPosition / 100).toFloat()

                                    // on below line we are starting
                                    // our media player.
                                    if(mediaPlayer.isPlaying) {
                                        mediaPlayer.stop()
                                    } else {
                                        mediaPlayer.start()
                                    }


                                } catch (e: Exception) {

                                    // on below line we are
                                    // handling our exception.
                                    e.printStackTrace()
                                }
                            }
                        ) {
                            Icon(
                                painter = painterResource(id = setIcon(isPlaying.value)),
                                contentDescription = "Play",
                                tint = MaterialTheme.colors.primary
                            )
                        }

                        Slider(
                            value = progress.value,
                            onValueChange = { progress.value = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            colors = SliderDefaults.colors(
                                thumbColor = MaterialTheme.colors.primary
                            )
                        )
                    }
                }
            }
        }
    }
}

fun setIcon(isPlaying: Boolean): Int {
    return if(isPlaying) {
        R.drawable.ic_pause
    } else {
        R.drawable.ic_play
    }
}

@Composable
fun DialogWindow() {


}