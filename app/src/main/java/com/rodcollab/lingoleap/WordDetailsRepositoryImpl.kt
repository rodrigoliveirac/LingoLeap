package com.rodcollab.lingoleap

import com.rodcollab.lingoleap.core.database.SearchHistoryDao
import com.rodcollab.lingoleap.features.word.detail.DefinitionDomain
import javax.inject.Inject


interface WordDetailsRepository {

    suspend fun getAudioSource(word: String): String

    suspend fun getPartOfSpeeches(word: String): List<String>

    suspend fun getMeanings(word: String, partOfSpeech: String): List<DefinitionDomain>
}

class WordDetailsRepositoryImpl @Inject constructor(private val dao: SearchHistoryDao) :
    WordDetailsRepository {

    override suspend fun getAudioSource(word: String): String = dao.getWordBy(word).audio

    override suspend fun getPartOfSpeeches(word: String): List<String> = dao.getPartOfSpeeches(word)

    override suspend fun getMeanings(word: String, partOfSpeech: String): List<DefinitionDomain> {
        return dao.getMeaningsAndDefinitions(word, partOfSpeech).map {
            DefinitionDomain(
                definition = it.definition,
                example = it.example
            )
        }
    }
}

data class WordDetailsDomain(
    val word: String,
    val audio: String,
    val partOfSpeech: List<String>,
    val meanings: List<DefinitionDomain>
)

data class MeaningDomain(
    val partOfSpeech: String,
    val definitions: List<DefinitionDomain>
)