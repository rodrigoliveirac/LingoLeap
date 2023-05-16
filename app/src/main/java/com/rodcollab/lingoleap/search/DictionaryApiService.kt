package com.rodcollab.lingoleap.search

import com.rodcollab.lingoleap.api.model.Word
import com.squareup.moshi.Moshi
import retrofit2.Retrofit
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

private const val BASE_URL = "https://api.dictionaryapi.dev/api/v2/entries/en/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface DictionaryApiService {
    @GET("{word}")
    suspend fun getWord(@Path("word") word: String?) : List<Word>
}

object DictionaryApi {
    val retrofitService: DictionaryApiService by lazy { retrofit.create(DictionaryApiService::class.java) }
}