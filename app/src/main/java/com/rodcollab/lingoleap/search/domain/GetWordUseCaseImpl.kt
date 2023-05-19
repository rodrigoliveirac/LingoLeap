package com.rodcollab.lingoleap.search.domain

import com.rodcollab.lingoleap.saved.WordsSavedRepository
import com.rodcollab.lingoleap.search.DictionaryApi
import com.rodcollab.lingoleap.search.Word
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetWordUseCaseImpl(private val wordSaved: WordsSavedRepository) : GetWordUseCase {

    private val service by lazy {
        DictionaryApi.retrofitService
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