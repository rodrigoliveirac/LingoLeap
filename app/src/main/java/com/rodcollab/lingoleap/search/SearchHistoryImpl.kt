package com.rodcollab.lingoleap.search

import com.rodcollab.lingoleap.core.database.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID

class SearchHistoryImpl(app: AppDatabase) : SearchHistory {

    private val dao = app.searchHistoryDao()

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

    override suspend fun getList(): List<SearchedWord> = dao.fetchSearchHistory()
}