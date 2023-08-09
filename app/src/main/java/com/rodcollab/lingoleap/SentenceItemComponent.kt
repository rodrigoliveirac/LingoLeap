package com.rodcollab.lingoleap

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ItemComponent(text: String) {
    Card(modifier = Modifier.fillMaxWidth().padding(start = 16.dp, top = 24.dp, end = 16.dp), shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(5.dp)) {
       Text(modifier = Modifier.padding(16.dp),text = text)
    }
}