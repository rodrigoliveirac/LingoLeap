package com.rodcollab.lingoleap.collections.history.repository

import com.rodcollab.lingoleap.search.SearchedWordDomain
import com.rodcollab.lingoleap.search.Word

interface SearchHistory {

    suspend fun add(word: Word)

    suspend fun getList(): List<SearchedWordDomain>
}
