package com.rodcollab.lingoleap.api

import com.rodcollab.lingoleap.api.model.InfoWord
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.http.GET
import retrofit2.http.Path

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