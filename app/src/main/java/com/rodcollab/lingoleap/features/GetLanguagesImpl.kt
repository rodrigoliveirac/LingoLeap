package com.rodcollab.lingoleap.features

import com.rodcollab.lingoleap.features.word.detail.LanguageOption
import com.rodcollab.lingoleap.features.word.translation.TranslationRepository
import javax.inject.Inject

class GetLanguagesImpl @Inject constructor(private val repository: TranslationRepository) :
    GetLanguages {
    override suspend fun invoke(): List<LanguageOption> {
        val local = repository.fetchLanguagesFromLocal()
        if (local.isEmpty()) {
            val languages = repository.fetchLanguagesFromApi()
            languages.map { language ->
                repository.insertToLocal(
                    language
                )
            }
        }
        return repository.fetchLanguagesFromLocal()
    }

}