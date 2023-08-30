package com.rodcollab.lingoleap.features.word.detail.components

import androidx.compose.runtime.Composable
import com.rodcollab.lingoleap.features.word.translation.ItemComponent

@Composable
fun SentencesPage(sentence: String) {
    ItemComponent(text = sentence)
}

@Composable
fun DefinitionsPage(definition: String) {
    ItemComponent(text = definition)
}