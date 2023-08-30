package com.rodcollab.lingoleap.di

import com.rodcollab.lingoleap.GetWordDetailsUseCase
import com.rodcollab.lingoleap.GetWordDetailsUseCaseImpl
import com.rodcollab.lingoleap.features.search.domain.GetWordUseCase
import com.rodcollab.lingoleap.features.search.domain.GetWordUseCaseImpl
import com.rodcollab.lingoleap.features.word.detail.GetMeaningsUseCase
import com.rodcollab.lingoleap.features.word.detail.GetMeaningsUseCaseImpl
import com.rodcollab.lingoleap.features.word.detail.GetSongsUseCase
import com.rodcollab.lingoleap.features.word.detail.GetSongsUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class UseCasesModule {

    @Singleton
    @Binds
    abstract fun providesGetWordUseCase(impl: GetWordUseCaseImpl): GetWordUseCase

    @Singleton
    @Binds
    abstract fun providesGetWordDetailsUseCase(impl: GetWordDetailsUseCaseImpl): GetWordDetailsUseCase

    @Singleton
    @Binds
    abstract fun providesGetMeaningsUseCase(impl: GetMeaningsUseCaseImpl): GetMeaningsUseCase

    @Singleton
    @Binds
    abstract fun providesGetSongsUseCase(impl: GetSongsUseCaseImpl): GetSongsUseCase
}