package com.rodcollab.lingoleap.core.networking.dictionary.model
import com.squareup.moshi.Json

data class Phonetic(
    @Json(name = "text")
    val text: String?,
    @Json(name = "audio")
    val audio: String?
)