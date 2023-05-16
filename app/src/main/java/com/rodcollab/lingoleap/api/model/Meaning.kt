package com.rodcollab.lingoleap.api.model
import com.squareup.moshi.Json

data class Meaning(
    @Json(name = "definitions")
    val definitions: List<Definition?>,
    @Json(name = "partOfSpeech")
    val partOfSpeech: String?
)