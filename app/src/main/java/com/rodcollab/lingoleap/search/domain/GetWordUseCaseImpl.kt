package com.rodcollab.lingoleap.search.domain

import com.rodcollab.lingoleap.saved.WordsSavedRepositoryImpl
import com.rodcollab.lingoleap.search.DictionaryApi
import com.rodcollab.lingoleap.search.Word
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetWordUseCaseImpl : GetWordUseCase {

    private val service by lazy {
        DictionaryApi.retrofitService
    }

    private val wordSaved by lazy {
        WordsSavedRepositoryImpl()
    }

    override suspend fun invoke(query: String): List<Word> {

        val arrayInformation = withContext(Dispatchers.IO) {
            service.getWord(query)
        }

        val saved = wordSaved.getSavedWords().any { it.name == query }



        return listOf(
            Word(
                name = query,
                arrayInformation = arrayInformation,
                saved = saved
            )
        )
    }
}