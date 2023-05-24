package com.rodcollab.lingoleap.search

data class SearchedWordDomain(
    val uui: String,
    val name: String,
    val createdAt: String,
    val meaning: String,
    val saved: Boolean,
    val audio: String
)
