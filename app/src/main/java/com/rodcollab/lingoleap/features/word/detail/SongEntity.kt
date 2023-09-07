package com.rodcollab.lingoleap.features.word.detail

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "song")
class SongEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo("word_related") val word: String,
    @ColumnInfo("song_title") val title: String,
    @ColumnInfo("song_thumbnailUrl") val thumbnailUrl: String
)
