package com.rodcollab.lingoleap.search

import com.rodcollab.lingoleap.saved.WordsSavedRepository

class SaveWordImpl(private val getSavedWord: WordsSavedRepository) : SaveWord {

    override suspend fun invoke(name: String) {
        val wordSaved = getSavedWord.getSavedWords().any { it.name == name }

        if (wordSaved) {
            getSavedWord.unsavedWord(name)
        } else {
            getSavedWord.savedWord(name)
        }
    }

}
