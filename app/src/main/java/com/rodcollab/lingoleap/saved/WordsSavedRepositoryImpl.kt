package com.rodcollab.lingoleap.saved

import android.util.Log
import com.rodcollab.lingoleap.core.database.AppDatabase
import com.rodcollab.lingoleap.core.database.SavedWord
import com.rodcollab.lingoleap.search.WordSaved

class WordsSavedRepositoryImpl(appDatabase: AppDatabase) : WordsSavedRepository {

    private val dao = appDatabase.savedWordDao()

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