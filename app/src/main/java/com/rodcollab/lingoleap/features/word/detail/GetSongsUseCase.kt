package com.rodcollab.lingoleap.features.word.detail

import com.rodcollab.lingoleap.core.database.SongDao
import com.rodcollab.lingoleap.core.networking.lyrics.LyricsApiService
import javax.inject.Inject

interface GetSongsUseCase {

    suspend operator fun invoke(word: String): List<SongDomain>

    suspend fun addSongs(songs: List<SongDomain>)
}

class GetSongsUseCaseImpl @Inject constructor(private val repository: SongsRepository) :
    GetSongsUseCase {

    override suspend operator fun invoke(word: String): List<SongDomain> = repository.getSongs(word)
    override suspend fun addSongs(songs: List<SongDomain>) {
        repository.addSongs(songs)
    }

}

interface SongsRepository {

    suspend fun getSongs(word: String): List<SongDomain>

    suspend fun addSongs(songs: List<SongDomain>)
}

class SongsRepositoryImpl @Inject constructor(
    private val dao: SongDao,
    private val service: LyricsApiService
) :
    SongsRepository {

    override suspend fun getSongs(word: String): List<SongDomain> {
        if (dao.getSongsByWord(word).isEmpty()) {
            try {
                return service.getSongs(word).body()!!.response.hits.map {
                    SongDomain(
                        title = it.result.title,
                        thumbnailUrl = it.result.thumbnailUrl
                    )
                }
            } catch (e: Exception) {

            }
        } else {
            return dao.getSongsByWord(word).map { SongDomain(title = it.title, thumbnailUrl = it.thumbnailUrl) }
        }

        return emptyList()
    }

    override suspend fun addSongs(songs: List<SongDomain>) {
        songs.map {
            dao.insert(
                SongEntity(
                    word = it.word,
                    title = it.title,
                    thumbnailUrl = it.thumbnailUrl
                )
            )
        }
    }
}
