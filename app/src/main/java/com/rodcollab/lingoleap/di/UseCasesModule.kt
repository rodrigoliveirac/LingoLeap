package com.rodcollab.lingoleap.di

import com.rodcollab.lingoleap.features.search.domain.GetWordUseCase
import com.rodcollab.lingoleap.features.search.domain.GetWordUseCaseImpl
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
}