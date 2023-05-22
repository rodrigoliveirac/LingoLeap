package com.rodcollab.lingoleap.profile

interface WordsSavedUseCase {

    suspend operator fun invoke(): List<SavedWordItemState>
}
