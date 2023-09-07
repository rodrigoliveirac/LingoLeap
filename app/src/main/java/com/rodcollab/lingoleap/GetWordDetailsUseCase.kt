package com.rodcollab.lingoleap

import com.rodcollab.lingoleap.features.word.detail.SongsRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface GetWordDetailsUseCase {

    suspend operator fun invoke(word: String): WordDetailsDomain

    suspend fun onToggleMark(word: String)
}

class GetWordDetailsUseCaseImpl @Inject constructor(
    private val songsRepository: SongsRepositoryImpl,
    private val repository: WordDetailsRepository
) : GetWordDetailsUseCase {

    override suspend fun invoke(word: String): WordDetailsDomain {

        val partOfSpeech = withContext(Dispatchers.IO) {
            repository.getPartOfSpeeches(word)
        }
        val wordInfo = withContext(Dispatchers.IO) {
            repository.getWordInfo(word)
        }

        return WordDetailsDomain(
            word = word,
            audio = wordInfo.audio,
            marked = wordInfo.marked,
            partOfSpeech = partOfSpeech,
            meanings = repository.getMeanings(word, partOfSpeech[0]),
            songs = songsRepository.getSongs(word),
        )
    }

    override suspend fun onToggleMark(word: String) {
        if (repository.getWordInfo(word).marked == 0) {
            repository.onToggleMark(word, 1)
        } else {
            repository.onToggleMark(word, 0)
        }
    }
}