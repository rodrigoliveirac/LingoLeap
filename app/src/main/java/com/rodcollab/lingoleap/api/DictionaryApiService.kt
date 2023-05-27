package com.rodcollab.lingoleap.api

import com.rodcollab.lingoleap.api.model.InfoWord
import com.squareup.moshi.Moshi
import retrofit2.Retrofit
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()))
    .baseUrl("BASE_URL")
    .build()

interface DictionaryApiService {
    @GET("{word}")
    suspend fun getWord(@Path("word") word: String?): List<InfoWord>

    companion object {
        const val BASE_URL = "https://api.dictionaryapi.dev/api/v2/entries/en/"
        val moshi: Moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }
}