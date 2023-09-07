package com.rodcollab.lingoleap.core.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.rodcollab.lingoleap.features.word.detail.SongEntity

@Dao
interface SongDao {

    @Insert
    suspend fun insert(song: SongEntity)

    @Query("SELECT * FROM song WHERE word_related = :word")
    fun getSongsByWord(word: String) : List<SongEntity>

}
