package com.rodcollab.lingoleap.collections.search.domain

import com.rodcollab.lingoleap.search.Word

interface GetWordUseCase {
    suspend operator fun invoke(query: String) : List<Word>

}
