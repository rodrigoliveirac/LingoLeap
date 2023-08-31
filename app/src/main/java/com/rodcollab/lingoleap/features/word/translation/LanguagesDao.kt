package com.rodcollab.lingoleap.features.word.translation

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface LanguagesDao {

    @Insert
    suspend fun insert(languageEntity: LanguageEntity)

    @Query("SELECT * FROM language")
    suspend fun fetchAll():List<LanguageEntity>
}
