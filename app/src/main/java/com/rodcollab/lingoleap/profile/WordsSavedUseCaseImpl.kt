package com.rodcollab.lingoleap.profile

import com.rodcollab.lingoleap.api.model.InfoWord
import com.rodcollab.lingoleap.saved.WordsSavedRepository
import com.rodcollab.lingoleap.search.DictionaryApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WordsSavedUseCaseImpl(private val wordsSavedRepository: WordsSavedRepository) :
    WordsSavedUseCase {

    private val service by lazy {
        DictionaryApi.retrofitService
    }

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
