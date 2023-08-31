package com.rodcollab.lingoleap.features.word

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("search_history")
data class SearchedWord(
    @PrimaryKey val uui: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "createdAt") val createdAt: Long,
    @ColumnInfo(name = "meaning") val meaning: String,
    @ColumnInfo(name = "saved") val saved: Boolean,
    @ColumnInfo(name = "audio") val audio: String
)