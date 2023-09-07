package com.rodcollab.lingoleap.core.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.rodcollab.lingoleap.core.networking.dictionary.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchHistoryDao {


    @Query("UPDATE word SET marked = :mark WHERE word = :word")
    suspend fun onToggleMark(word: String,mark: Int)
    @Insert
    suspend fun addDefinition(definition: DefinitionEntity)
    @Insert
    suspend fun addMeaning(meaning: MeaningEntity)
    @Insert
    suspend fun addWord(word: WordEntity)

    @Transaction
    @Query("SELECT * FROM meaning WHERE meaningId = :meaningId")
    fun getMeaningWithDefinitions(meaningId: String): MeaningWithDefinitions

    @Query("SELECT * FROM word WHERE word = :word")
    fun getWordBy(word: String) : WordEntity

    @Query("SELECT DISTINCT partOfSpeech FROM meaning WHERE wordCreatorId = :word")
    fun getPartOfSpeeches(word: String) : List<String>

    @Query("SELECT * FROM word WHERE word = :wordId")
    fun findWordById(wordId:String) : Flow<WordEntity>

    @Query("SELECT * FROM meaning WHERE wordCreatorId = :wordCreatorId")
    fun meaningsByWordCreatorId(wordCreatorId:String) : List<MeaningEntity>

    @Query("SELECT * FROM definition WHERE word = :word")
    fun definitionsBy(word: String,): List<DefinitionEntity>

    @Transaction
    @Query("SELECT * FROM definition WHERE word = :word AND partOfSpeechCreator = :partOfSpeech")
    fun getMeaningsAndDefinitions(word: String, partOfSpeech: String) : List<DefinitionEntity>

    @Query("SELECT * FROM word")
    suspend fun fetchSearchHistory(): List<WordEntity>

}

