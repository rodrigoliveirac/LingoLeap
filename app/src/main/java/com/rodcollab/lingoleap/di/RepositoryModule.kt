package com.rodcollab.lingoleap.di

import com.rodcollab.lingoleap.profile.saved.repository.WordsSavedRepository
import com.rodcollab.lingoleap.profile.saved.repository.WordsSavedRepositoryImpl
import com.rodcollab.lingoleap.history.repository.SearchHistory
import com.rodcollab.lingoleap.history.repository.SearchHistoryImpl
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
    abstract fun providesWordsSavedImpl(impl: WordsSavedRepositoryImpl): WordsSavedRepository

    @Singleton
    @Binds
    abstract fun providesSearchHistoryImpl(impl: SearchHistoryImpl) : SearchHistory

}