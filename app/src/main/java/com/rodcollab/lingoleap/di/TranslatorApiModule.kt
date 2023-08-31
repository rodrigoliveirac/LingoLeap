package com.rodcollab.lingoleap.di

import com.rodcollab.lingoleap.core.networking.dictionary.ApiKeyInterceptor
import com.rodcollab.lingoleap.core.networking.translator.TranslatorApiService
import com.rodcollab.lingoleap.core.networking.translator.TranslatorApiService.Companion.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TranslatorApiModule {

    @Provides
    @Singleton
    fun provideApiKeyInterceptor(): ApiKeyInterceptor {
        return ApiKeyInterceptor()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(apiKeyInterceptor: ApiKeyInterceptor, apiKeyInterceptorLyrics: ApiKeyInterceptorLyricsApi): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(apiKeyInterceptor)
            .addInterceptor(apiKeyInterceptorLyrics)
            .build()
    }

    @Provides
    @Singleton
    fun provideOpenAIApi(okHttpClient: OkHttpClient): TranslatorApiService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(TranslatorApiService.moshi))
            .build()
            .create(TranslatorApiService::class.java)
    }
}
