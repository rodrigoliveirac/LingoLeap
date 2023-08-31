package com.rodcollab.lingoleap.di

import com.rodcollab.lingoleap.core.networking.lyrics.LyricsApiService
import com.rodcollab.lingoleap.core.networking.lyrics.LyricsApiService.Companion.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LyricsApiModule {

    @Provides
    @Singleton
    fun provideApiKeyInterceptor(): ApiKeyInterceptorLyricsApi {
        return ApiKeyInterceptorLyricsApi()
    }

    @Provides
    @Singleton
    fun provideLyricsApi(okHttpClient: OkHttpClient) : LyricsApiService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(LyricsApiService.moshi))
            .build()
            .create(LyricsApiService::class.java)
    }
}