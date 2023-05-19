package com.rodcollab.lingoleap.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.rodcollab.lingoleap.core.database.SavedWord
import com.rodcollab.lingoleap.search.WordSaved

@Dao
interface SavedWordDao {

    @Query("SELECT * FROM saved_word WHERE name LIKE :word ")
    suspend fun fetchSavedWord(word: String) : List<SavedWord>

    @Insert
    suspend fun savedWord(word: SavedWord)

    @Query("SELECT * FROM saved_word")
    suspend fun fetchSavedWords(): List<WordSaved>

    @Query("""DELETE FROM saved_word WHERE name = :name""")
    suspend fun unsaved(name: String)
}