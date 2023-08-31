package com.rodcollab.lingoleap.features.word.detail.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.rodcollab.lingoleap.features.word.detail.bottomBorder

@Composable
@OptIn(ExperimentalFoundationApi::class)
fun MeaningItemComponent(
    translate :(String) -> Unit,
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
            .padding(start = 16.dp, top = 16.dp, bottom = 8.dp, end = 16.dp),
        elevation = 8.dp
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            TabRow(actualPage)

            HorizontalPager(
                state = state,
            ) { page ->
                when (page) {
                    0 -> {
                        DefinitionsPage(definition = definition, translate = { translate(it) } )
                    }

                    1 -> SentencesPage(sentence = example, translate = { translate(it) })
                }
            }
        }
    }
}

@Composable
private fun TabRow(actualPage: Int) {
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


    }
}