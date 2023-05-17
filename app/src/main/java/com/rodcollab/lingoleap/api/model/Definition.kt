package com.rodcollab.lingoleap.api.model
import com.squareup.moshi.Json

data class Definition(
    @Json(name = "antonyms")
    val antonyms: List<Any?>,
    @Json(name = "definition")
    val definition: String?,
    @Json(name = "example")
    val example: String?,
    @Json(name = "synonyms")
    val synonyms: List<Any?>
)