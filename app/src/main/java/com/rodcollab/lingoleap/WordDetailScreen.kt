package com.rodcollab.lingoleap

import android.util.Log
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

    val pagerState = rememberPagerState(pageCount = {
        2
    })

    var actualPage by remember { mutableStateOf(2) }

    var meaningIndex by remember { mutableStateOf(0) }

    var definition by remember { mutableStateOf("") }

    var example by remember { mutableStateOf("") }

    LaunchedEffect(meaningIndex) {

        delay(1000)
        definition = viewModel.meanings[meaningIndex].definitions[0].definition.toString()
        example = if (viewModel.meanings[meaningIndex].definitions[0].example != null) {
            viewModel.meanings[meaningIndex].definitions[0].example.toString()
        } else {
            ""
        }

    }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            actualPage = page
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
                    text = viewModel.word
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

                IconButton(onClick = { Log.d("iconAudio", "CLicked") }) {
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
                    state = pagerState, modifier = Modifier
                        .fillMaxWidth()
                ) {
                    when (actualPage) {
                        0 -> DefinitionsPage(definition)
                        1 -> SentencesPage(example)
                    }
                }

            }
        }
    }
}

@Composable
fun SentencesPage(sentence: String) {
    ItemComponent(text = sentence)
}

@Composable
fun DefinitionsPage(definition: String) {
    ItemComponent(text = definition)
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
