package com.rodcollab.lingoleap.features.word.detail

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import com.rodcollab.lingoleap.R
import com.rodcollab.lingoleap.features.word.translation.TranslateComponent
import com.rodcollab.lingoleap.features.word.translation.ItemComponent
import java.util.*


@Composable
fun WordDetailScreen(
    onNavigateBack: () -> Unit,
    wordDetailsViewModel: WordDetailsViewModel = hiltViewModel()
) {

    val wordDetailsUiState by wordDetailsViewModel.word.collectAsState()
    val languages by wordDetailsViewModel.languages.collectAsState()

    var actualPartOfSpeech by rememberSaveable { mutableStateOf(wordDetailsUiState.partOfSpeech) }

    var translatedText by rememberSaveable { mutableStateOf("") }

    var isLoading by rememberSaveable { mutableStateOf(false) }

    var selectedLanguage by remember { mutableStateOf<(String?) -> Unit>({}) }

    var langCode by remember { mutableStateOf(Locale.getDefault().language) }

    var wordTranslated by remember { mutableStateOf("") }

    var isWordTranslated by remember { mutableStateOf(false) }

    val scope = LocalLifecycleOwner.current.lifecycleScope

    DisposableEffect(Unit) {
//        scope.launch {
//            wordDetailsViewModel.translate(langCode = langCode, wordDetailsViewModel.wordId) {
//                wordTranslated = it
//                isWordTranslated = true
//            }
//        }
        onDispose { }
    }

    LaunchedEffect(Unit) {
        wordDetailsViewModel.loadLanguages()
    }

    /*selectedLanguage = { selectedLang ->
        isLoading = true
        if (selectedLang != null) {
            langCode = selectedLang
            scope.launch {
                scope.launch {
                    *//*wordDetailsViewModel.translate(langCode, actualText) { newTranslatedText ->
                        translatedText = newTranslatedText
                        isLoading = false
                    }*//*
                }
                scope.launch {
                    wordDetailsViewModel.translate(
                        langCode = langCode,
                        wordDetailsViewModel.wordId
                    ) {
                        wordTranslated = it
                        isWordTranslated = true
                        isLoading = false
                    }
                }
            }
        }
    }*/

    Scaffold(topBar = {
        TopAppBar() {
            IconButton(onClick = {
                onNavigateBack()
            }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
            }
            Text(text = stringResource(R.string.word_details_title))
        }
    }) {
        Box(modifier = Modifier.padding(it)) {
            Column(
                modifier = Modifier.padding(
                    top = 32.dp,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 32.dp
                )
            ) {


                Row {
                    Text(
                        modifier = Modifier.padding(start = 12.dp),
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold,
                        text = wordDetailsUiState.word
                    )
                    Box {
                        if (isLoading && !isWordTranslated) {
                            CircularProgressIndicator(
                                strokeWidth = 1.dp,
                                strokeCap = StrokeCap.Square,
                                color = Color(255, 20, 147),
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .size(72.dp)
                                    .padding(24.dp)
                            )
                        }
                    }

                }

                LazyRow(
                    modifier = Modifier
                        .padding(start = 12.dp, top = 16.dp, end = 12.dp)
                ) {
                    items(wordDetailsUiState.partOfSpeeches) { partOfSpeech ->
                        Text(modifier = Modifier
                            .clickable {
                                wordDetailsViewModel.getDefinitionsBy(partOfSpeech)
                            }
                            .border(
                                1.dp,
                                if (actualPartOfSpeech == partOfSpeech) Color(
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

                IconButton(onClick = { }) {
                    Icon(
                        modifier = Modifier
                            .padding(top = 16.dp, bottom = 24.dp),
                        imageVector = Icons.Default.VolumeUp,
                        contentDescription = null
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

                LazyColumn(modifier = Modifier.fillMaxSize()) {


                    items(wordDetailsUiState.definitionsAndExamples) { item ->
                        MeaningItemComponent(
                            isLoading,
                            item.definition,
                            item.example
                        )
                    }
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalFoundationApi::class)
private fun MeaningItemComponent(
    isLoading: Boolean,
    definition: String,
    example: String
) {

    val state = rememberPagerState(pageCount = {
        2
    })

    var actualPage by rememberSaveable { mutableStateOf(2) }

    var arrowClicked by remember { mutableStateOf(false) }

    var showTranslationComponent by remember { mutableStateOf(false) }

    LaunchedEffect(arrowClicked) {
        showTranslationComponent = arrowClicked
    }

    LaunchedEffect(state) {
        snapshotFlow { state.currentPage }.collect { page ->
            actualPage = page
        }
    }

    Card(
        backgroundColor = Color(255, 255, 255, 255),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp, top = 16.dp, end = 12.dp, bottom = 8.dp),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 24.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Spacer(
                    modifier = Modifier
                        .align(Alignment.Bottom)
                        .weight(1f)
                        .height(1.dp)
                        .background(Color.LightGray)
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .bottomBorder(
                            1.dp,
                            if (actualPage == 0) Color(250, 128, 46) else Color.LightGray
                        )
                        .padding(bottom = 8.dp), text = "Definition"
                )
                Spacer(
                    modifier = Modifier
                        .align(Alignment.Bottom)
                        .weight(1f)
                        .height(1.dp)
                        .background(Color.LightGray)
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .bottomBorder(
                            1.dp,
                            if (actualPage == 1) Color(250, 128, 46) else Color.LightGray
                        )
                        .padding(bottom = 8.dp), text = "Sentences"
                )
                Spacer(
                    modifier = Modifier
                        .align(Alignment.Bottom)
                        .weight(1f)
                        .height(1.dp)
                        .background(Color.LightGray)
                )
            }

            HorizontalPager(
                state = state,
                beyondBoundsPageCount = 2,
            ) { page ->
                when (page) {
                    0 -> {
                        DefinitionsPage(isLoading = isLoading, definition = definition)
                    }

                    1 -> SentencesPage(isLoading = isLoading, sentence = example)
                }
            }

            IconButton(
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(top = 16.dp),
                onClick = { arrowClicked = !arrowClicked }) {
                Icon(
                    imageVector = if (showTranslationComponent) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                    contentDescription = null,
                    modifier = Modifier.size(32.dp)
                )
            }
            if (arrowClicked) {
                Text(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                    text = "Translate to"
                )
                TranslateComponent({ }, emptyList(), isLoading, definition)
            }
        }
    }
}

@Composable
fun SentencesPage(isLoading: Boolean, sentence: String) {
    ItemComponent(isLoading = isLoading, text = sentence)
}

@Composable
fun DefinitionsPage(isLoading: Boolean, definition: String) {
    ItemComponent(isLoading = isLoading, text = definition)
}

fun Modifier.bottomBorder(strokeWidth: Dp, color: Color) = composed(
    factory = {
        val density = LocalDensity.current
        val strokeWidthPx = density.run { strokeWidth.toPx() }

        Modifier.drawBehind {
            val width = size.width
            val height = size.height - strokeWidthPx / 2

            drawLine(
                color = color,
                start = Offset(x = 0f, y = height),
                end = Offset(x = width, y = height),
                strokeWidth = strokeWidthPx
            )
        }
    }
)

@Preview
@Composable
fun PagerScreenPreview() {

}
