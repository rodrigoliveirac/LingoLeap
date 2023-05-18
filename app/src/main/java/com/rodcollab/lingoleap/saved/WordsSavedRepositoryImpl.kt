package com.rodcollab.lingoleap.saved

import android.util.Log
import com.rodcollab.lingoleap.search.WordSaved

class WordsSavedRepositoryImpl : WordsSavedRepository {

    private val words = mutableListOf<WordSaved>(
        WordSaved(name = "hello")
    )

    override suspend fun unsavedWord(name: String) {
        val word = words.find { it.name == name }
        words.remove(word)
    }

    override suspend fun savedWord(name: String) {

        val savedWord = WordSaved(
            name = name,
        )
        words.add(savedWord)
        Log.d("hello", savedWord.toString())
    }

    override suspend fun getSavedWords(): List<WordSaved> = words
}