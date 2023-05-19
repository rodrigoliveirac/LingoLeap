package com.rodcollab.lingoleap.core.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_word")
data class SavedWord(
    @PrimaryKey val name: String,
)