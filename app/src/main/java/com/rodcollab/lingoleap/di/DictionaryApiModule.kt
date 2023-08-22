package com.rodcollab.lingoleap.di

import com.rodcollab.lingoleap.core.networking.dictionary.DictionaryApiService
import com.rodcollab.lingoleap.core.networking.dictionary.DictionaryApiService.Companion.BASE_URL
import com.rodcollab.lingoleap.core.networking.dictionary.DictionaryApiService.Companion.moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DictionaryApiModule {

    @Singleton
    @Provides
    fun providesDictionaryService() : DictionaryApiService {
        return Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(BASE_URL)
            .build()
            .create(DictionaryApiService::class.java)
    }
}