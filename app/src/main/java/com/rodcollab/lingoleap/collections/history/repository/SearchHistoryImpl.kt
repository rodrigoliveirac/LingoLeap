package com.rodcollab.lingoleap.collections.history.repository

import com.rodcollab.lingoleap.core.database.SearchHistoryDao
import com.rodcollab.lingoleap.search.SearchedWord
import com.rodcollab.lingoleap.search.SearchedWordDomain
import com.rodcollab.lingoleap.search.Word
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class SearchHistoryImpl @Inject constructor(private val dao: SearchHistoryDao) : SearchHistory {

    override suspend fun add(word: Word) {
        var audio = ""
        var searchWord: SearchedWord
        withContext(Dispatchers.IO) {
            word.arrayInformation[0].phonetics.onEach {
                if (it.audio?.isNotBlank() == true) {
                    audio = it.audio
                }
            }

            val id = UUID.randomUUID().toString()

            searchWord = SearchedWord(
                uui = id,
                createdAt = System.currentTimeMillis(),
                name = word.name,
                meaning = word.arrayInformation[0].meanings[0].definitions[0].definition.toString(),
                audio = audio,
                saved = word.saved
            )
        }

        dao.addSearchedWord(searchWord)
    }

    override suspend fun getList(): List<SearchedWordDomain> {
        return dao.fetchSearchHistory().map {
            val date = withContext(Dispatchers.IO) {
                getDate(it)
            }
            SearchedWordDomain(
                uui = it.uui,
                name = it.name,
                createdAt = date,
                meaning = it.meaning,
                saved = it.saved,
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