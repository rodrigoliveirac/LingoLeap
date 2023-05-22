package com.rodcollab.lingoleap.search

interface SearchHistory {

    suspend fun add(word: Word)

    suspend fun getList(): List<SearchedWord>
}
