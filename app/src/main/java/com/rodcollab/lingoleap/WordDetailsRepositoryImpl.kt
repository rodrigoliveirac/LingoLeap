package com.rodcollab.lingoleap

import com.rodcollab.lingoleap.core.database.SearchHistoryDao
import com.rodcollab.lingoleap.core.networking.dictionary.model.WordEntity
import com.rodcollab.lingoleap.features.word.detail.DefinitionDomain
import com.rodcollab.lingoleap.features.word.detail.SongDomain
import javax.inject.Inject

data class WordDomain(
    val audio: String,
    val marked: Int
)

fun WordDetailsRepositoryImpl.localToWordDomain(word: WordEntity): WordDomain {
    return WordDomain(
        audio = word.audio,
        marked = word.marked,
    )
}

interface WordDetailsRepository {

    suspend fun getWordInfo(word: String): WordDomain

    suspend fun getPartOfSpeeches(word: String): List<String>

    suspend fun getMeanings(word: String, partOfSpeech: String): List<DefinitionDomain>
    suspend fun onToggleMark(word: String, mark: Int)
}

class WordDetailsRepositoryImpl @Inject constructor(private val dao: SearchHistoryDao) :
    WordDetailsRepository {

    override suspend fun getWordInfo(word: String): WordDomain =
        this.localToWordDomain(dao.getWordBy(word))

    override suspend fun getPartOfSpeeches(word: String): List<String> = dao.getPartOfSpeeches(word)

    override suspend fun getMeanings(word: String, partOfSpeech: String): List<DefinitionDomain> {
        return dao.getMeaningsAndDefinitions(word, partOfSpeech).map {
            DefinitionDomain(
                definition = it.definition,
                example = it.example
            )
        }
    }

    override suspend fun onToggleMark(word: String, mark: Int) {
        dao.onToggleMark(word, mark)
    }

}

data class WordDetailsDomain(
    val word: String,
    val audio: String,
    val marked: Int,
    val partOfSpeech: List<String>,
    val meanings: List<DefinitionDomain>,
    val songs: List<SongDomain>
)

data class MeaningDomain(
    val partOfSpeech: String,
    val definitions: List<DefinitionDomain>
)