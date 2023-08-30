package com.rodcollab.lingoleap.features.word.translation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun ItemComponent(isLoading: Boolean, text: String) {

    var expanded by remember { mutableStateOf(false) }

    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(start = 16.dp, top = 24.dp,bottom = 8.dp, end = 16.dp), shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(5.dp)) {
        if(isLoading) {
            CircularProgressIndicator(strokeWidth = 1.dp, strokeCap = StrokeCap.Square, color = Color(255,20,147), modifier = Modifier.size(72.dp).align(Alignment.CenterHorizontally).padding(24.dp))
        } else {
            Text(modifier = Modifier.clickable { expanded = !expanded }.padding(16.dp),text = text, maxLines = if(expanded) Int.MAX_VALUE else 1, overflow = if(expanded) TextOverflow.Visible else TextOverflow.Ellipsis)
        }
    }
}