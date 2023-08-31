package com.rodcollab.lingoleap.features.word.detail

import com.rodcollab.lingoleap.core.database.SearchHistoryDao
import javax.inject.Inject

interface GetMeaningsUseCase {

    suspend operator fun  invoke(word:String, partOfSpeech:String): List<DefinitionDomain>
}

class GetMeaningsUseCaseImpl @Inject constructor(private val dao: SearchHistoryDao) : GetMeaningsUseCase {
    override suspend operator fun invoke(word: String, partOfSpeech: String): List<DefinitionDomain> {
        return dao.getMeaningsAndDefinitions(word, partOfSpeech).map {
            DefinitionDomain(
                definition = it.definition,
                example = it.example
            )
        }
    }

}
