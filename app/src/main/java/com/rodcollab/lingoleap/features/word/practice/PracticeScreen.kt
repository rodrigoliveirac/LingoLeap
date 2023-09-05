package com.rodcollab.lingoleap.features.word.practice

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun PracticeScreen(
    onNavigateBack: () -> Unit,
    practiceViewModel: PracticeViewModel = hiltViewModel()
) {

    val uiState by practiceViewModel.uiState.collectAsState()

    var currentDragItem by remember { mutableStateOf("") }

    Scaffold(topBar = {
        TopAppBar() {
            IconButton(onClick = { onNavigateBack() }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Arrow Back")
            }
            Text(text = "Practice")
        }
    }) {
        PressDraggable(modifier = Modifier.fillMaxSize(), vm = practiceViewModel) {
            Column(
                Modifier
                    .padding(it)
                    .fillMaxSize(), verticalArrangement = Arrangement.Center
            ) {
                Text(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    text = "Let's Practice",
                    fontSize = 24.sp
                )

                Column(Modifier.padding(it).fillMaxWidth()) {

                    if (uiState.answer.isEmpty()) {
                        Spacer(
                            Modifier
                                .height(50.dp)
                                .fillMaxWidth()
                        )
                    } else {

                        DropTarget<WordItem>(
                            uiState = uiState,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                                .wrapContentSize(unbounded = true)

                        ) { isInBound ->
                            Column(modifier = Modifier.fillMaxWidth().background(if(isInBound) Color.Red else Color.Transparent),horizontalAlignment = Alignment.CenterHorizontally) {
                                uiState.answer.forEach { list ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(
                                                start = 16.dp,
                                                end = 24.dp,
                                                top = 8.dp,
                                                bottom = 8.dp
                                            ),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        list.map { word ->
                                            Card(
                                                backgroundColor = Color(255, 255, 255, 255),
                                                shape = RoundedCornerShape(12.dp),
                                                border = BorderStroke(
                                                    2.dp, Color(
                                                        250,
                                                        128,
                                                        46
                                                    )
                                                ),
                                                modifier = Modifier
                                                    .wrapContentSize(unbounded = true)
                                                    .padding(4.dp)
                                                    .graphicsLayer {
                                                        alpha = 0f
                                                    },
                                                elevation = 4.dp
                                            ) {
                                                Text(
                                                    maxLines = 1,
                                                    modifier = Modifier
                                                        .padding(12.dp), text = word.value
                                                )
                                            }
                                        }
                                        Spacer(modifier = Modifier.fillMaxWidth().weight(1f))
                                    }
                                    Spacer(
                                        modifier = Modifier
                                            .height(2.dp)
                                            .fillMaxWidth()
                                            .background(Color.LightGray)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    uiState.question.forEach { list ->
                        Row(
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            list.map { word ->

                                DragTarget(
                                    modifier = Modifier.sizeIn(),
                                    dataToDrop = word,
                                    drag = { currentDragItem = it.id },
                                    end = { currentDragItem = it }
                                ) {
                                    Card(
                                        backgroundColor = Color(255, 255, 255, 255),
                                        shape = RoundedCornerShape(12.dp),
                                        border = BorderStroke(
                                            2.dp, Color(
                                                250,
                                                128,
                                                46
                                            )
                                        ),
                                        modifier = Modifier
                                            .padding(4.dp)
                                            .graphicsLayer {
                                                alpha = if (currentDragItem == word.id) 0f else 1f
                                            },
                                        elevation = 4.dp
                                    ) {
                                        Text(
                                            maxLines = 1,
                                            modifier = Modifier
                                                .padding(12.dp), text = word.value
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}