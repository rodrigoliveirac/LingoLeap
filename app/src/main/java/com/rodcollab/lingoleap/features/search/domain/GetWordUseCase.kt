package com.rodcollab.lingoleap.features.search.domain

import com.rodcollab.lingoleap.features.search.Word

interface GetWordUseCase {
    suspend operator fun invoke(query: String) : List<Word>

}
