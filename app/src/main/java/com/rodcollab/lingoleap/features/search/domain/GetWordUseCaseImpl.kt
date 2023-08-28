package com.rodcollab.lingoleap.features.search.domain

import com.rodcollab.lingoleap.core.networking.dictionary.DictionaryApiService
import com.rodcollab.lingoleap.features.search.Word
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetWordUseCaseImpl @Inject constructor(
    private val service: DictionaryApiService,
) : GetWordUseCase {

    override suspend fun invoke(query: String): List<Word> {

        val arrayInformation = withContext(Dispatchers.IO) {
            service.getWord(query)
        }

        return listOf(
            Word(
                name = query,
                arrayInformation = arrayInformation,
            )
        )
    }
}