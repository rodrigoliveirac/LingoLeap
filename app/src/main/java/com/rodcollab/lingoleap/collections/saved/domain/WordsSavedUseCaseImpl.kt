package com.rodcollab.lingoleap.collections.saved.domain

import com.rodcollab.lingoleap.core.networking.dictionary.DictionaryApiService
import com.rodcollab.lingoleap.core.networking.dictionary.model.InfoWord
import com.rodcollab.lingoleap.features.profile.SavedWordItemState
import com.rodcollab.lingoleap.collections.saved.repository.WordsSavedRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WordsSavedUseCaseImpl @Inject constructor(private val service: DictionaryApiService, private val wordsSavedRepository: WordsSavedRepository) :
    WordsSavedUseCase {

    override suspend fun invoke(): List<SavedWordItemState> {
        return wordsSavedRepository.getSavedWords().map {
            val arrayInformation = withContext(Dispatchers.IO) {
                service.getWord(it.name)
            }

            SavedWordItemState(
                name = it.name,
                meaning = arrayInformation[0].meanings[0].definitions[0].definition.toString(),
                audio = getAudio(arrayInformation)
            )
        }
    }

    private suspend fun getAudio(arrayInformation : List<InfoWord>) : String {
        var audio = ""
        withContext(Dispatchers.IO) {
            arrayInformation[0].phonetics.onEach {
                if(it.audio?.isNotBlank() == true) {
                    audio = it.audio
                }
            }
        }
        return audio
    }
}
