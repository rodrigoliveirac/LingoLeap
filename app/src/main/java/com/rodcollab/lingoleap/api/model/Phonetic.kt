package com.rodcollab.lingoleap.api.model
import com.squareup.moshi.Json

data class Phonetic(
    @Json(name = "text")
    val text: String?,
    @Json(name = "audio")
    val audio: String?
)