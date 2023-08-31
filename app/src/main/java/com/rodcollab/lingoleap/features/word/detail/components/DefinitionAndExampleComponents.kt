package com.rodcollab.lingoleap.features.word.detail.components

import androidx.compose.runtime.Composable
import com.rodcollab.lingoleap.features.word.translation.ItemComponent

@Composable
fun SentencesPage(sentence: String, translate: (String) -> Unit) {
    ItemComponent(text = sentence, translate = translate)
}

@Composable
fun DefinitionsPage(definition: String, translate: (String) -> Unit) {
    ItemComponent(text = definition, translate = translate)
}