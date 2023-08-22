package com.rodcollab.lingoleap.collections.history.repository

import com.rodcollab.lingoleap.core.database.SearchHistoryDao
import com.rodcollab.lingoleap.core.networking.dictionary.model.DefinitionEntity
import com.rodcollab.lingoleap.core.networking.dictionary.model.MeaningEntity
import com.rodcollab.lingoleap.core.networking.dictionary.model.WordEntity
import com.rodcollab.lingoleap.features.word.SearchedWord
import com.rodcollab.lingoleap.features.word.search.SearchedWordDomain
import com.rodcollab.lingoleap.features.word.search.Word
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class SearchHistoryImpl @Inject constructor(
    private val dao: SearchHistoryDao
) : SearchHistory {

    override suspend fun add(word: Word) {
        var audio = ""
        withContext(Dispatchers.IO) {
            word.arrayInformation[0].phonetics.onEach {
                if (it.audio?.isNotBlank() == true) {
                    audio = it.audio
                }
            }

            dao.addWord(WordEntity(word.name, audio))

            word.arrayInformation.map { infoWord ->
                infoWord.meanings.map { meaning ->
                    meaning.definitions.map { definition ->
                        val meaningId = withContext(Dispatchers.IO) { UUID.randomUUID().toString() }
                        dao.addMeaning(
                            MeaningEntity(
                                meaningId = meaningId,
                                wordCreatorId = word.name,
                                partOfSpeech = meaning.partOfSpeech
                            )
                        )
                        val definitionId =
                            withContext(Dispatchers.IO) { UUID.randomUUID().toString() }
                        dao.addDefinition(
                            DefinitionEntity(
                                definitionId = definitionId,
                                meaningCreatorId = meaningId,
                                definition = definition.definition ?: "",
                                example = definition.example ?: ""
                            )
                        )
                    }
                }
            }
        }


    }

    override suspend fun getList(): List<SearchedWordDomain> {
        return dao.fetchSearchHistory().map {
            /*val date = withContext(Dispatchers.IO) {
                getDate(it)
            }*/
            SearchedWordDomain(
                name = it.word,
                audio = it.audio
            )
        }
    }

    private fun getDate(it: SearchedWord): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = it.createdAt
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        return "$day/$month/$year"
    }
}