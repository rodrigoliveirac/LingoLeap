package com.rodcollab.lingoleap.core.networking.dictionary.model
import com.squareup.moshi.Json

data class InfoWord(
    @Json(name = "meanings")
    val meanings: List<Meaning>,
    @Json(name = "origin")
    val origin: String?,
    @Json(name = "phonetic")
    val phonetic: String?,
    @Json(name = "phonetics")
    val phonetics: List<Phonetic>,
    @Json(name = "word")
    val word: String
)