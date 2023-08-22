package com.rodcollab.lingoleap.collections.saved.repository

import android.util.Log
import com.rodcollab.lingoleap.core.database.SavedWord
import com.rodcollab.lingoleap.core.database.dao.SavedWordDao
import com.rodcollab.lingoleap.features.word.search.WordSaved
import javax.inject.Inject

class WordsSavedRepositoryImpl @Inject constructor(private val dao: SavedWordDao) :
    WordsSavedRepository {

    override suspend fun unsavedWord(name: String) {
        dao.unsaved(name)
        Log.d("delete_word", name)
    }

    override suspend fun savedWord(name: String) {

        val savedWord = SavedWord(
            name = name,
        )
        dao.savedWord(savedWord)
        Log.d("saved_word", savedWord.name)
    }

    override suspend fun getSavedWords(): List<WordSaved> = dao.fetchSavedWords()
}