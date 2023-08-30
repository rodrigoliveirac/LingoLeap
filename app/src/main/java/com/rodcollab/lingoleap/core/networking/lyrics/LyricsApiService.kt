package com.rodcollab.lingoleap.core.networking.lyrics

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface LyricsApiService {

    @GET("search")
    suspend fun getSongs(@Query("q") word: String): Response<ResponseGenius>

    companion object {
        const val BASE_URL = "https://api.genius.com/"
        val moshi: Moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

}

@JsonClass(generateAdapter = true)
data class ResponseGenius(@Json(name = "response") val response: Hits)

@JsonClass(generateAdapter = true)
data class Hits(@Json(name = "hits") val hits: List<HitData>)

@JsonClass(generateAdapter = true)
data class HitData(@Json(name = "result") val result: InfoHit)

@JsonClass(generateAdapter = true)
data class InfoHit(
    @Json(name = "title") val title: String,
    @Json(name = "song_art_image_thumbnail_url") val thumbnailUrl: String
)