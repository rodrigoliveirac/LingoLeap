package com.rodcollab.lingoleap.search

import com.rodcollab.lingoleap.saved.WordsSavedRepository
import com.rodcollab.lingoleap.saved.WordsSavedRepositoryImpl

class SaveWordImpl : SaveWord {

    private val getSavedWord: WordsSavedRepository by lazy {
        WordsSavedRepositoryImpl()
    }

    override suspend fun invoke(name: String) {
        val wordSaved = getSavedWord.getSavedWords().any { it.name == name }

        if (wordSaved) {
            getSavedWord.unsavedWord(name)
        } else {
            getSavedWord.savedWord(name)
        }
    }

}
