package com.rodcollab.lingoleap.features.word.detail

import com.rodcollab.lingoleap.core.networking.lyrics.LyricsApiService
import javax.inject.Inject

interface GetSongsUseCase {

    suspend operator fun invoke(word: String): List<SongDomain>
}

class GetSongsUseCaseImpl @Inject constructor(private val repository: SongsRepository) :
    GetSongsUseCase {

    override suspend operator fun invoke(word: String): List<SongDomain> = repository.getSongs(word)

}

interface SongsRepository {

    suspend fun getSongs(word: String): List<SongDomain>
}

class SongsRepositoryImpl @Inject constructor(private val service: LyricsApiService) :
    SongsRepository {

    override suspend fun getSongs(word: String): List<SongDomain> {
        try {
            return service.getSongs(word).body()!!.response.hits.map {
                SongDomain(
                    title = it.result.title,
                    thumbnailUrl = it.result.thumbnailUrl
                )
            }
        } catch (e: Exception) {

        }
        return emptyList()
    }
}
