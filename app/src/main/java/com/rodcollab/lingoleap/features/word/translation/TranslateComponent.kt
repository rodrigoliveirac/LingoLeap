package com.rodcollab.lingoleap.features.word.translation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TranslateComponent(
    isLoading: Boolean,
    text: String
) {

    if (text.isBlank()) {
        androidx.compose.material.LinearProgressIndicator(
            color = Color(
                250,
                128,
                46
            ), modifier = Modifier
                .height(15.dp)
                .fillMaxWidth()
                .padding(12.dp)
        )
    } else {
        Card(
            colors = CardDefaults.cardColors(Color.White),
            border = BorderStroke(1.dp, Color.LightGray),
            modifier = Modifier
                .fillMaxWidth()
                .sizeIn()
                .padding(start = 16.dp, top = 8.dp, bottom = 16.dp, end = 16.dp),
            elevation = CardDefaults.cardElevation(5.dp)
        ) {
            Text(modifier = Modifier.padding(12.dp), text = text)
        }
    }

}