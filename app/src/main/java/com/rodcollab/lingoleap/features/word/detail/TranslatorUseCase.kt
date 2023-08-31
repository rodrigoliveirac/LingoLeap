package com.rodcollab.lingoleap.features.word.detail

import com.rodcollab.lingoleap.features.word.translation.TranslationRepository
import javax.inject.Inject

interface TranslatorUseCase {

    suspend operator fun invoke(targetLang:String, text: String) : String

}

class TranslatorUseCaseImpl @Inject constructor(private val repository: TranslationRepository) : TranslatorUseCase {
    override suspend fun invoke(targetLang: String, text: String): String = repository.translate(targetLang, text)
}
