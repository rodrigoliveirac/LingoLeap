package com.rodcollab.lingoleap.collections.saved.repository

import com.rodcollab.lingoleap.features.word.search.WordSaved

interface WordsSavedRepository {

    suspend fun unsavedWord(name:String)

    suspend fun savedWord(name: String)

    suspend fun getSavedWords(): List<WordSaved>

}
