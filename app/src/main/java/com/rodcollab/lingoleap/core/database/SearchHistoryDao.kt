package com.rodcollab.lingoleap.core.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.rodcollab.lingoleap.core.networking.dictionary.model.*
import com.rodcollab.lingoleap.features.word.SearchedWord
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchHistoryDao {

    @Insert
    suspend fun addDefinition(definition: DefinitionEntity)
    @Insert
    suspend fun addMeaning(meaning: MeaningEntity)
    @Insert
    suspend fun addWord(word: WordEntity)

    @Transaction
    @Query("SELECT * FROM meaning WHERE meaningId = :meaningId")
    fun getMeaningWithDefinitions(meaningId: String): MeaningWithDefinitions

    @Transaction
    @Query("SELECT * FROM word WHERE word = :word")
    fun getWordWithMeanings(word: String) : WordWithMeanings

    @Query("SELECT * FROM word WHERE word = :wordId")
    fun findWordById(wordId:String) : Flow<WordEntity>

    @Query("SELECT * FROM meaning WHERE wordCreatorId = :wordCreatorId")
    fun meaningsByWordCreatorId(wordCreatorId:String) : List<MeaningEntity>

    @Insert
    suspend fun addSearchedWord(word: SearchedWord)

    @Query("SELECT * FROM word")
    suspend fun fetchSearchHistory(): List<WordEntity>

}

