package com.rodcollab.lingoleap.features.history.repository

import com.rodcollab.lingoleap.features.search.SearchedWordDomain
import com.rodcollab.lingoleap.features.search.Word

interface SearchHistory {

    suspend fun add(word: Word)

    suspend fun getList(): List<SearchedWordDomain>
}
