package com.rodcollab.lingoleap.features.word.translation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.rodcollab.lingoleap.R

@Composable
fun ItemComponent(translate: (String) -> Unit, text: String) {

    var expanded by remember { mutableStateOf(false) }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, top = 18.dp, end = 8.dp),
            shape = MaterialTheme.shapes.medium,
            elevation = CardDefaults.cardElevation(5.dp)
        ) {
            Row {
                Text(
                    modifier = Modifier
                        .fillMaxWidth().weight(1f)
                        .clickable { expanded = !expanded }
                        .padding(16.dp),
                    text = text.ifBlank { "We couldn't find an example for this context :/" },
                    maxLines = if (expanded) Int.MAX_VALUE else 1,
                    overflow = if (expanded) TextOverflow.Visible else TextOverflow.Ellipsis
                )
                if(text.isNotBlank()) {
                    IconButton(modifier = Modifier.align(Alignment.CenterVertically).padding(end = 16.dp),onClick = { translate(text) }) {
                        Icon(
                            painterResource(id = R.drawable.icon),
                            contentDescription = null,
                        )
                    }
                }
            }
        }
}