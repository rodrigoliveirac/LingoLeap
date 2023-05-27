package com.rodcollab.lingoleap.collections.saved.domain

import com.rodcollab.lingoleap.profile.SavedWordItemState

interface WordsSavedUseCase {

    suspend operator fun invoke(): List<SavedWordItemState>
}
