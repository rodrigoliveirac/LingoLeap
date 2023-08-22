package com.rodcollab.lingoleap.collections.search.domain

import com.rodcollab.lingoleap.core.networking.dictionary.DictionaryApiService
import com.rodcollab.lingoleap.collections.saved.repository.WordsSavedRepository
import com.rodcollab.lingoleap.core.database.SearchHistoryDao
import com.rodcollab.lingoleap.features.word.search.Word
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetWordUseCaseImpl @Inject constructor(
    private val service: DictionaryApiService,
    private val dao: SearchHistoryDao,
    private val wordSaved: WordsSavedRepository
) : GetWordUseCase {

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