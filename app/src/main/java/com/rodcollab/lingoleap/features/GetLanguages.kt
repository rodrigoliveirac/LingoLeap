package com.rodcollab.lingoleap.features

import com.rodcollab.lingoleap.features.word.detail.LanguageOption

interface GetLanguages {
    suspend operator fun invoke(): List<LanguageOption>
}