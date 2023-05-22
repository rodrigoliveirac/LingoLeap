package com.rodcollab.lingoleap.core.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.rodcollab.lingoleap.search.SearchedWord

@Dao
interface SearchHistoryDao {

    @Insert
    suspend fun addSearchedWord(word: SearchedWord)

    @Query("SELECT * FROM search_history")
    suspend fun fetchSearchHistory(): List<SearchedWord>

}
