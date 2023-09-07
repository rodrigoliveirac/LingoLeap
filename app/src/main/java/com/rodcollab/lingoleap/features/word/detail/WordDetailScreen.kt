package com.rodcollab.lingoleap.features.word.detail

import androidx.compose.animation.core.Spring.DampingRatioMediumBouncy
import androidx.compose.animation.core.Spring.StiffnessLow
import androidx.compose.animation.core.spring
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rodcollab.lingoleap.R
import com.rodcollab.lingoleap.features.word.detail.components.MainWordDetailsPagerComponent
import com.rodcollab.lingoleap.features.word.detail.components.SongsWordDetailsPagerComponent
import java.util.*


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WordDetailScreen(
    onNavigateBack: () -> Unit,
    wordDetailsViewModel: WordDetailsViewModel = hiltViewModel()
) {

    val wordDetailsUiState by wordDetailsViewModel.uiState.collectAsState()

    val pagerState = rememberPagerState(pageCount = {
        2
    })

    var scroll by remember { mutableStateOf(false) }

    var goTo by remember { mutableStateOf(false) }

    LaunchedEffect(scroll) {
        if (goTo) {
            pagerState.animateScrollToPage(
                page = 1,
                animationSpec = spring(
                    dampingRatio = DampingRatioMediumBouncy,
                    stiffness = StiffnessLow
                )
            )
            goTo = false
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

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(it)
        ) {
            if (wordDetailsUiState.isLoading) {
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
                VerticalPager(state = pagerState) { page ->
                    when (page) {
                        0 -> {
                            MainWordDetailsPagerComponent(
                                toSongsPager = {
                                    scroll = true
                                    goTo = true
                                },
                                wordDetailsUiState,
                                wordDetailsViewModel,
                            )
                        }

                        1 -> {
                            SongsWordDetailsPagerComponent(wordDetailsUiState)
                            scroll = false
                        }
                    }
                }
            }
        }
    }
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