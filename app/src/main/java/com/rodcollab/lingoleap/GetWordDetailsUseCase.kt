package com.rodcollab.lingoleap

import javax.inject.Inject

interface GetWordDetailsUseCase {

    suspend operator fun invoke(word: String): WordDetailsDomain
}

class GetWordDetailsUseCaseImpl @Inject constructor(private val repository: WordDetailsRepository) : GetWordDetailsUseCase {

    override suspend fun invoke(word: String): WordDetailsDomain {

        val partOfSpeech = repository.getPartOfSpeeches(word)

        return WordDetailsDomain(
            word = word,
            audio = repository.getAudioSource(word),
            partOfSpeech = partOfSpeech,
            meanings = repository.getMeanings(word, partOfSpeech[0])
        )
    }

}