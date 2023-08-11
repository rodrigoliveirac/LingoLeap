package com.rodcollab.lingoleap

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.delay


@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
fun WordDetailScreen(
    onNavigateBack: () -> Unit,
    wordDetailsViewModel: WordDetailsViewModel = hiltViewModel()
) {

    val viewModel by wordDetailsViewModel.word.collectAsState()

    val languages = wordDetailsViewModel.languages

    val pagerState = rememberPagerState(pageCount = {
        2
    })

    var actualPage by rememberSaveable { mutableStateOf(2) }

    var meaningIndex by rememberSaveable { mutableStateOf(0) }

    var definition by rememberSaveable { mutableStateOf("") }

    var example by rememberSaveable { mutableStateOf("") }

    var translatedText by rememberSaveable { mutableStateOf("") }

    var actualText by rememberSaveable { mutableStateOf(definition) }

    var isLoading by rememberSaveable { mutableStateOf(true) }

    var selectedLanguage by remember { mutableStateOf<(String?) -> Unit>({})}

    var langCode by remember { mutableStateOf("pt")}

    DisposableEffect(Unit) {
        wordDetailsViewModel.getLanguages()
        onDispose {  }
    }

    LaunchedEffect(meaningIndex, pagerState) {

        isLoading = true
        delay(500)

        definition = viewModel.meanings[meaningIndex].definitions[0].definition.toString()
            .ifEmpty { "Sorry. We don't have a definition for this" }
        example = if (viewModel.meanings[meaningIndex].definitions[0].example != null) {
            viewModel.meanings[meaningIndex].definitions[0].example.toString()
        } else {
            "Sorry. We don't have an example for this word"
        }

        actualText = if (actualPage == 0) {
            definition
        } else {
            example
        }


        wordDetailsViewModel.translate(langCode,actualText) {
            translatedText = it
            isLoading = false
        }

    }

    selectedLanguage = { selectedLang ->
        if(selectedLang != null) {
            langCode = selectedLang
            wordDetailsViewModel.translate(langCode,actualText) { newTranslatedText ->
                translatedText = newTranslatedText
            }
        }
    }



    LaunchedEffect(pagerState) {

        snapshotFlow { pagerState.currentPage }.collect { page ->

            isLoading = true
            actualPage = page
            actualText = if (actualPage == 0) {
                definition
            } else {
                example
            }
            wordDetailsViewModel.translate(langCode,actualText) {

                translatedText = it
                isLoading = false
            }
        }
    }
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
            Column(modifier = Modifier.padding(top = 32.dp, start = 16.dp, end = 16.dp)) {


                Text(
                    modifier = Modifier.padding(start = 12.dp),
                    fontSize = 32.sp,
                    text = wordDetailsViewModel.wordId
                )

                Row(
                    modifier = Modifier
                        .horizontalScroll(rememberScrollState())
                        .padding(start = 12.dp, top = 16.dp)
                ) {
                    viewModel.meanings.forEachIndexed { index, meaning ->

                        Text(modifier = Modifier
                            .clickable {
                                meaningIndex = index
                            }
                            .border(
                                1.dp,
                                if (meaningIndex == index) Color(
                                    250,
                                    128,
                                    46
                                ) else Color.LightGray,
                                RoundedCornerShape(2.dp)
                            )
                            .padding(start = 6.dp, top = 2.dp, bottom = 2.dp, end = 6.dp),
                            text = meaning.partOfSpeech.toString(),
                            fontSize = 10.sp)

                        Spacer(modifier = Modifier.size(8.dp))
                    }
                }

                IconButton(onClick = { }) {
                    Icon(
                        modifier = Modifier
                            .padding(top = 16.dp),
                        imageVector = Icons.Default.VolumeUp,
                        contentDescription = null
                    )
                }


                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 12.dp, top = 16.dp, end = 12.dp),
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
                            .padding(bottom = 8.dp), text = "Definitions"
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
                    state = pagerState,
                    beyondBoundsPageCount = 2,
                ) { page ->
                    when (page) {
                        0 -> {
                            DefinitionsPage(isLoading = isLoading, definition = definition)
                        }
                        1 -> SentencesPage(isLoading = isLoading, sentence = example)
                    }
                }

                Text(
                    modifier = Modifier
                        .padding(top = 32.dp)
                        .align(Alignment.CenterHorizontally), text = "Translate to"
                )
                TranslateComponent(selectedLanguage,languages, isLoading, translatedText)
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
