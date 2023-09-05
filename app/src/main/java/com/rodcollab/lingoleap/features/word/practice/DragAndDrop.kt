package com.rodcollab.lingoleap.features.word.practice

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp

internal val LocalDragTargetInfo = compositionLocalOf { DragTargetInfo() }

@Composable
fun PressDraggable(
    modifier: Modifier = Modifier,
    vm: PracticeViewModel,
    content: @Composable BoxScope.() -> Unit,
) {

    val state = remember { DragTargetInfo() }

    CompositionLocalProvider(
        LocalDragTargetInfo provides state
    ) {
        Box(modifier = modifier.fillMaxSize())
        {
            content()
            if (state.isDragging) {
                var targetSize by remember {
                    mutableStateOf(IntSize.Zero)
                }
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
                            val offset = (state.dragPosition + state.dragOffset)
                            scaleX = 1.0f
                            scaleY = 1.0f
                            alpha = if (targetSize == IntSize.Zero) 0f else 1f
                            translationX = offset.x
                            translationY = offset.y
                        }
                        .onGloballyPositioned {
                            targetSize = it.size
                        },
                    elevation = 4.dp
                ) {
                    Text(
                        maxLines = 1,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(8.dp), text = state.dataToDropValue
                    )
                    state.draggableComposable?.invoke()
                }
            }
        }
    }
}

@Composable
fun <T> DragTarget(
    modifier: Modifier,
    dataToDrop: T,
    drag: (WordItem) -> Unit,
    end: (String) -> Unit,
    content: @Composable (() -> Unit)
) {

    var currentPosition by remember { mutableStateOf(Offset.Zero) }
    val currentState = LocalDragTargetInfo.current

    Box(modifier = modifier
        .onGloballyPositioned {
            currentPosition = it.localToWindow(Offset.Zero)
        }
        .pointerInput(Unit) {
            detectDragGestures(onDragStart = {
                drag(dataToDrop as WordItem)
                currentState.dataToDrop = dataToDrop
                currentState.dataToDropValue = dataToDrop.value
                currentState.isDragging = true
                currentState.dragPosition = currentPosition
                currentState.draggableComposable = content
            }, onDrag = { change, dragAmount ->
                change.consumeAllChanges()
                currentState.dragOffset += Offset(dragAmount.x, dragAmount.y)
            }, onDragEnd = {
                currentState.isDragging = false
                currentState.dragOffset = Offset.Zero
                end("")
            }, onDragCancel = {
                currentState.dragOffset = Offset.Zero
                currentState.isDragging = false
                end("")
            })
        }) {
        content()
    }
}

@Composable
fun <T> DropTarget(
    uiState: PracticeUiState,
    modifier: Modifier,
    content: @Composable() (BoxScope.(isInBound: Boolean) -> Unit)
) {

    val dragInfo = LocalDragTargetInfo.current
    val dragPosition = dragInfo.dragPosition
    val dragOffset = dragInfo.dragOffset
    var isCurrentDropTarget by remember {
        mutableStateOf(false)
    }


    Box(modifier = modifier
        .onGloballyPositioned {
        it.boundsInWindow().let { rect ->
            isCurrentDropTarget = rect.contains(dragPosition + dragOffset)
        }
    }, propagateMinConstraints = true) {
       // val data = if (isCurrentDropTarget && !dragInfo.isDragging) dragInfo.dataToDrop as T? else null
        content(isCurrentDropTarget)
    }
}

internal class DragTargetInfo {
    var isDragging: Boolean by mutableStateOf(false)
    var dataToDropValue: String by mutableStateOf("")
    var dragPosition by mutableStateOf(Offset.Zero)
    var dragOffset by mutableStateOf(Offset.Zero)
    var draggableComposable by mutableStateOf<(@Composable () -> Unit)?>(null)
    var dataToDrop by mutableStateOf<Any?>(null)
}