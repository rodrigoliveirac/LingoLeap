package com.rodcollab.lingoleap.collections.di

import com.rodcollab.lingoleap.collections.saved.domain.WordsSavedUseCase
import com.rodcollab.lingoleap.collections.saved.domain.WordsSavedUseCaseImpl
import com.rodcollab.lingoleap.collections.search.domain.GetWordUseCase
import com.rodcollab.lingoleap.collections.search.domain.GetWordUseCaseImpl
import com.rodcollab.lingoleap.collections.search.domain.SaveWord
import com.rodcollab.lingoleap.collections.search.domain.SaveWordImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class CollectionsModule {

    @Singleton
    @Binds
    abstract fun providesGetWordsSavedUseCase(impl: WordsSavedUseCaseImpl): WordsSavedUseCase

    @Singleton
    @Binds
    abstract fun providesGetWordUseCase(impl: GetWordUseCaseImpl): GetWordUseCase

    @Singleton
    @Binds
    abstract fun providesSaveWord(impl: SaveWordImpl) : SaveWord
}