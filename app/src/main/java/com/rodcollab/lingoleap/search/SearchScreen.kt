package com.rodcollab.lingoleap.search

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.*
import com.rodcollab.lingoleap.R
import kotlinx.coroutines.*


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchScreen(
    onClickWord: (String) -> Unit,
    modifier: Modifier,
    viewModel: SearchViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsState()
    val keyBoardController = LocalSoftwareKeyboardController.current

    DisposableEffect(state) {
        viewModel.onResume()
        onDispose { }
    }

    LaunchedEffect(state) {
        viewModel.onResume()
    }

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
        if (state.words.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier
                .align(Alignment.End)
                .clickable { viewModel.onEvent(SearchEvent.OnClearHistory) }) {
                Text("clear history", fontSize = 14.sp)
                Spacer(modifier = Modifier.size(8.dp))
            }
            Spacer(modifier = Modifier.height(8.dp))
        } else {
            Spacer(modifier = Modifier.height(16.dp))
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
        if (state.openDialog) {

            DialogComponent(viewModel, state)
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun DialogComponent(
    viewModel: SearchViewModel,
    state: SearchState
) {
    Dialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        onDismissRequest = { viewModel.onEvent(SearchEvent.OpenDialog(false)) }) {

        val audioAttributes = AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .build()


        var progress by remember { mutableStateOf(0f) }

        var isPlaying by remember { mutableStateOf(false) }
        val mediaPlayer by remember { mutableStateOf(MediaPlayer()) }

        var saved by remember { mutableStateOf(false) }

        var meaningIndex by remember { mutableStateOf(0) }

        var definition by remember { mutableStateOf("") }
        var example by remember { mutableStateOf("") }

        LaunchedEffect(state.infoItem.saved) {
            saved = state.infoItem.saved
        }

        LaunchedEffect(meaningIndex) {

            definition = state.infoItem.meanings[meaningIndex].definitions[0].definition.toString()
            example = if (state.infoItem.meanings[meaningIndex].definitions[0].example != null) {
                state.infoItem.meanings[meaningIndex].definitions[0].example.toString()
            } else {
                ""
            }
        }


        DisposableEffect(state.infoItem.audio) {


            mediaPlayer.apply {
                setDataSource(state.infoItem.audio)
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


        Box(
            Modifier
                .width(320.dp)
                .background(Color.White, RoundedCornerShape(8.dp))
        ) {
            Column(
                Modifier
                    .sizeIn()
                    .padding(18.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = state.infoItem.word.replaceFirstChar { it.uppercase() },
                        fontSize = 24.sp
                    )
                    Row(
                        modifier = Modifier
                            .sizeIn()
                            .clickable {
                                viewModel.onEvent(SearchEvent.OnSaveWord(state.infoItem))
                            },
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {}
                        ) {
                            Icon(
                                painterResource(id = R.drawable.ic_bookmark),
                                contentDescription = "Bookmark",
                                tint = ifWordIsSaved(state.infoItem.saved)
                            )
                        }
                        Text(
                            "Save",
                            color = ifWordIsSaved(saved)
                        )
                        Spacer(Modifier.size(24.dp))
                    }
                }

                Spacer(Modifier.size(8.dp))
                Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                    state.infoItem.meanings.forEachIndexed { index, meaning ->
                        Chip(
                            modifier = Modifier.sizeIn(),
                            onClick = { meaningIndex = index }) {
                            Text(meaning.partOfSpeech.toString())
                        }
                        Spacer(modifier = Modifier.size(8.dp))
                    }
                }
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = "Definition: $definition", fontSize = 16.sp,
                    color = Color.Gray,
                    fontStyle = FontStyle.Italic
                )
                Spacer(modifier = Modifier.size(8.dp))
                if (example.isNotBlank()) {
                    Text(
                        text = "Example: $example", fontSize = 16.sp,
                        color = Color.Gray,
                        fontStyle = FontStyle.Italic
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                }
                Spacer(modifier = Modifier.size(8.dp))
                Row(
                    modifier = Modifier
                        .sizeIn()
                        .padding(top = 8.dp)
                        .align(Alignment.CenterHorizontally),
                    verticalAlignment = Alignment.Bottom
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
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

fun ifWordIsSaved(value: Boolean): Color {
    return if (value) {
        Color.Magenta
    } else {
        Color.Gray
    }
}

fun setIcon(isPlaying: Boolean): Int {
    return if (isPlaying) {
        R.drawable.ic_pause
    } else {
        R.drawable.ic_play
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
fun DialogPreview() {
    Box(
        Modifier
            .sizeIn()
            .background(Color.White, RoundedCornerShape(8.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .padding(16.dp)
        ) {
            Text(text = "Hello", fontSize = 24.sp)
            Spacer(Modifier.size(8.dp))
            Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                for (i in listOf("one", "two")) {
                    Chip(
                        modifier = Modifier.sizeIn(),
                        onClick = { /* do something*/ }) {
                        Text(i)
                    }
                    Spacer(modifier = Modifier.size(8.dp))
                }
            }
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = "Definition: Used as a greeting or to begin a phone conversation.",
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
                    onClick = {}
                ) {
                    Icon(
                        painter = painterResource(id = setIcon(false)),
                        contentDescription = "Play",
                        tint = MaterialTheme.colors.primary
                    )
                }

                Slider(
                    value = 0f,
                    onValueChange = { },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = SliderDefaults.colors(
                        thumbColor = MaterialTheme.colors.primary
                    ),
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.End)
                    .clickable { },
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {}
                ) {
                    Icon(
                        painterResource(id = R.drawable.ic_bookmark),
                        contentDescription = "Bookmark",
                        tint = Color.Gray
                    )
                }
                Text(
                    "SAVE",
                    color = Color.Gray
                )
                Spacer(Modifier.size(24.dp))
            }
        }
    }
}
