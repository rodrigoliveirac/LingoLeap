package com.rodcollab.lingoleap.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun WordItem(
    wordItemUiState: WordItemUiState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val item = wordItemUiState.element

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(5.dp))
            .padding(4.dp)
            .shadow(
                elevation = 1.dp,
                shape = RoundedCornerShape(5.dp)
            )
            .background(MaterialTheme.colors.surface)
            .clickable { onClick() }
            .padding(end = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f)
            ) {

                Column(
                    modifier = Modifier.align(Alignment.CenterVertically),
                ) {
                    Text(
                        text = item.word,
                        style = MaterialTheme.typography.body1,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = modifier.height(8.dp))
                    Text(
                        text = item.meaning,
                        style = MaterialTheme.typography.body2
                    )
                }
            }
        }
    }
}