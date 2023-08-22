package com.rodcollab.lingoleap.core.networking.dictionary.model

import com.squareup.moshi.Json



data class Meaning(
    @Json(name = "definitions")
    val definitions: List<Definition>,
    @Json(name = "partOfSpeech")
    val partOfSpeech: String
)

data class WordItem(
    val word: String,
    val meanings: List<ItemMeaning>
)
data class ItemMeaning(
    val word: String,
    val definitions: List<ItemDefinition>,
    val partOfSpeech: String?
)

data class ItemDefinition(
    val partOfSpeech: String?,
    val definition: String?,
    val example: String?,
    val textTranslated: List<TextTranslated>,
)
data class TextTranslated(
    val code: String,
    val definitionTranslated: String,
    val exampleTranslated: String
)

data class ItemTranslated(
    val example: String,
    val definition: String
)