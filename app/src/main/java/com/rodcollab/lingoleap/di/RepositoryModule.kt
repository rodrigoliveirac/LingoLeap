package com.rodcollab.lingoleap.di

import com.rodcollab.lingoleap.WordDetailsRepository
import com.rodcollab.lingoleap.WordDetailsRepositoryImpl
import com.rodcollab.lingoleap.features.history.repository.SearchHistory
import com.rodcollab.lingoleap.features.history.repository.SearchHistoryImpl
import com.rodcollab.lingoleap.features.word.translation.TranslationRepository
import com.rodcollab.lingoleap.features.word.translation.TranslationRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun providesSearchHistoryImpl(impl: SearchHistoryImpl): SearchHistory

    @Singleton
    @Binds
    abstract fun providesTranslationImpl(impl: TranslationRepositoryImpl): TranslationRepository

    @Singleton
    @Binds
    abstract fun providesWordDetailsRepositoryImpl(impl: WordDetailsRepositoryImpl): WordDetailsRepository

}