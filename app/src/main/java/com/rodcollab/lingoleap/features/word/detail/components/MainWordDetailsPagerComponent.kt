package com.rodcollab.lingoleap.features.word.detail.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import com.rodcollab.lingoleap.R
import com.rodcollab.lingoleap.features.word.detail.WordDetailsUiState
import com.rodcollab.lingoleap.features.word.detail.WordDetailsViewModel
import com.rodcollab.lingoleap.features.word.translation.TranslateComponent
import java.util.Locale

@Composable
@OptIn(ExperimentalFoundationApi::class)
fun MainWordDetailsPagerComponent(
    toSongsPager: () -> Unit,
    wordDetailsUiState: WordDetailsUiState,
    wordDetailsViewModel: WordDetailsViewModel,
) {

    val pagerState = rememberPagerState(pageCount = {
        wordDetailsUiState.definitionsAndExamples.size
    })
    var actualPartOfSpeech by rememberSaveable { mutableStateOf(wordDetailsUiState.partOfSpeech) }

    var showDialog by remember { mutableStateOf(false) }

    val defLanguage by remember { mutableStateOf(Locale.getDefault().language.lowercase()) }

    var originText by remember { mutableStateOf("") }

    if (showDialog) {
        MinimalDialog(wordDetailsViewModel, originText, wordDetailsUiState) { showDialog = false }
    }

    LaunchedEffect(actualPartOfSpeech) {
        pagerState.animateScrollToPage(
            page = 0,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        )
    }

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

        Row {
            Text(
                modifier = Modifier.padding(start = 12.dp),
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                text = wordDetailsUiState.word
            )
        }

        PartOfSpeechesRow(
            wordDetailsUiState,
            wordDetailsViewModel,
            actualPartOfSpeech = { actualPartOfSpeech = it })

        IconButton(onClick = { wordDetailsViewModel.playAudio() }, enabled = wordDetailsUiState.audio.isNotBlank()) {
            Icon(
                modifier = Modifier
                    .padding(top = 16.dp, bottom = 24.dp),
                imageVector = Icons.Default.VolumeUp,
                contentDescription = null,
                tint = if(wordDetailsUiState.audio.isNotBlank()) Color.Black else Color.LightGray
            )
        }

        Text(
            color = Color.DarkGray,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 12.dp, bottom = 8.dp),
            fontSize = 24.sp,
            fontWeight = FontWeight.Light,
            text = "meanings"
        )

        MeaningsHorizontalPager(
            translate = { text ->
                originText = text
                showDialog = true
                wordDetailsViewModel.translate(defLanguage, text)
            },
            pagerState,
            wordDetailsUiState
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Songs with this word",
                color = Color.DarkGray,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(start = 12.dp, top = 16.dp, bottom = 8.dp),
                fontSize = 24.sp,
                fontWeight = FontWeight.Light,
            )

            IconButton(
                onClick = { toSongsPager() },
            ) {
                Icon(imageVector = Icons.Default.MoreHoriz, contentDescription = null)
            }
        }
        Spacer(Modifier.height(8.dp))
        SongsSession(wordDetailsUiState)
    }
}

@Composable
fun MinimalDialog(
    vm: WordDetailsViewModel,
    originText: String,
    uiState: WordDetailsUiState,
    onDismissRequest: () -> Unit,
) {

    Dialog(
        onDismissRequest = { onDismissRequest() },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {

        var expanded by remember { mutableStateOf(false) }
        var language by remember { mutableStateOf(Locale.getDefault().displayLanguage) }
        var isLoading by remember { mutableStateOf(false) }

        Box(
            modifier = Modifier
                .padding(start = 40.dp, end = 40.dp)
                .background(Color.White, RoundedCornerShape(16.dp)),
        ) {
            Column(
                Modifier
                    .sizeIn()
                    .padding(16.dp)
                    .align(Alignment.Center)
            ) {

                Row(modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp)) {
                    Text(
                        modifier = Modifier.padding(end = 8.dp),
                        text = "Translation",
                        fontSize = 24.sp
                    )
                    Icon(
                        painterResource(id = R.drawable.icon),
                        contentDescription = null,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }


                androidx.compose.material3.Card(
                    colors = CardDefaults.cardColors(Color.White),
                    border = BorderStroke(1.dp, Color.LightGray),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, top = 8.dp, bottom = 8.dp, end = 16.dp),
                    elevation = CardDefaults.cardElevation(5.dp)
                ) {
                    androidx.compose.material3.Text(
                        modifier = Modifier.padding(
                            12.dp
                        ),
                        text = originText
                    )
                }

                androidx.compose.material3.Card(
                    colors = CardDefaults.cardColors(Color.White),
                    border = BorderStroke(1.dp, Color(250, 128, 46)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .zIndex(1f)
                        .align(Alignment.CenterHorizontally)
                        .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp),
                    elevation = CardDefaults.cardElevation(5.dp),
                ) {

                    Row(
                        modifier = Modifier.clickable {
                            expanded = !expanded
                        },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = language,
                            color = Color.Gray,
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(12.dp),
                        )
                        androidx.compose.material3.Icon(
                            modifier = Modifier.padding(8.dp),
                            imageVector = if (!expanded) Icons.Default.ArrowDropDown else Icons.Default.ArrowDropUp,
                            contentDescription = null,
                            tint = Color.Gray
                        )
                    }

                    if (expanded) {
                        Box(Modifier.heightIn(max = 120.dp)) {
                            LazyColumn(
                                Modifier
                                    .sizeIn()
                                    .fillMaxWidth()
                            ) {
                                items(uiState.languages) {

                                    Text(
                                        text = it.name,
                                        color = Color.Gray,
                                        modifier = Modifier
                                            .clickable {
                                                language = it.name
                                                expanded = !expanded
                                                vm.translate(it.code, uiState.textToTranslate)
                                            }
                                            .fillMaxWidth()
                                            .padding(start = 12.dp, 8.dp),
                                    )
                                }
                            }
                        }
                    }
                }
                TranslateComponent(isLoading = isLoading, text = uiState.translatedText)
                Spacer(modifier = Modifier.height(8.dp))
            }

        }
    }
}

@Composable
private fun SongsSession(wordDetailsUiState: WordDetailsUiState) {
    if (wordDetailsUiState.songs.isEmpty()) {
        LinearProgressIndicator(
            color = Color(
                250,
                128,
                46
            ), modifier = Modifier
                .height(10.dp)
                .fillMaxWidth()
                .padding(start = 12.dp, top = 8.dp, end = 24.dp)
        )
    } else {
        LazyRow(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            items(wordDetailsUiState.songs) { song ->
                Card(
                    modifier = Modifier
                        .sizeIn()
                        .padding(4.dp),
                    shape = RoundedCornerShape(12.dp),
                    elevation = 8.dp
                ) {
                    AsyncImage(
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(62.dp),
                        model = song.thumbnailUrl,
                        contentDescription = null
                    )
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalFoundationApi::class)
private fun MeaningsHorizontalPager(
    translate: (String) -> Unit,
    pagerState: PagerState,
    wordDetailsUiState: WordDetailsUiState,
) {


    HorizontalPager(
        modifier = Modifier.fillMaxWidth(),
        pageSpacing = 8.dp,
        state = pagerState
    ) { pager ->

        Column {
            wordDetailsUiState.definitionsAndExamples.mapIndexed { index, definitionDomain ->
                if (index == pager) {
                    MeaningItemComponent(
                        { text ->
                            translate(text)
                        },
                        definitionDomain.definition,
                        definitionDomain.example
                    )
                }
            }
            Row(
                modifier = Modifier
                    .padding(top = 16.dp, bottom = 8.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                List(wordDetailsUiState.definitionsAndExamples.size) { index ->
                    Icon(
                        modifier = Modifier.size(12.dp),
                        imageVector = Icons.Default.Circle,
                        contentDescription = null,
                        tint = if (index <= pager) Color(
                            250,
                            128,
                            46
                        ) else Color.LightGray
                    )
                }
            }
        }
    }
}

@Composable
private fun PartOfSpeechesRow(
    wordDetailsUiState: WordDetailsUiState,
    wordDetailsViewModel: WordDetailsViewModel,
    actualPartOfSpeech: (String) -> Unit
) {

    LazyRow(
        modifier = Modifier
            .padding(start = 12.dp, top = 16.dp, end = 12.dp)
    ) {
        items(wordDetailsUiState.partOfSpeeches) { partOfSpeech ->
            Text(modifier = Modifier
                .clickable {
                    wordDetailsViewModel.getDefinitionsBy(partOfSpeech)
                    actualPartOfSpeech(partOfSpeech)
                }
                .border(
                    1.dp,
                    if (wordDetailsUiState.partOfSpeech == partOfSpeech) Color(
                        250,
                        128,
                        46
                    ) else Color.LightGray,
                    RoundedCornerShape(2.dp)
                )
                .padding(start = 6.dp, top = 2.dp, bottom = 2.dp, end = 6.dp),
                text = partOfSpeech,
                fontSize = 14.sp)

            Spacer(modifier = Modifier.size(8.dp))
        }
    }
}