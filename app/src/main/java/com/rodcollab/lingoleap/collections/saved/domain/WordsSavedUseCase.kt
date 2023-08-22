package com.rodcollab.lingoleap.collections.saved.domain

import com.rodcollab.lingoleap.features.profile.SavedWordItemState

interface WordsSavedUseCase {

    suspend operator fun invoke(): List<SavedWordItemState>
}
