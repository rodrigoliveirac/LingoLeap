package com.rodcollab.lingoleap.collections.search.domain

import com.rodcollab.lingoleap.collections.saved.repository.WordsSavedRepository
import javax.inject.Inject

class SaveWordImpl @Inject constructor(private val getSavedWord: WordsSavedRepository) : SaveWord {

    override suspend fun invoke(name: String) {
        val wordSaved = getSavedWord.getSavedWords().any { it.name == name }

        if (wordSaved) {
            getSavedWord.unsavedWord(name)
        } else {
            getSavedWord.savedWord(name)
        }
    }

}
