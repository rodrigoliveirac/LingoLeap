package com.rodcollab.lingoleap.collections.history.repository

import com.rodcollab.lingoleap.features.word.search.SearchedWordDomain
import com.rodcollab.lingoleap.features.word.search.Word

interface SearchHistory {

    suspend fun add(word: Word)

    suspend fun getList(): List<SearchedWordDomain>
}
