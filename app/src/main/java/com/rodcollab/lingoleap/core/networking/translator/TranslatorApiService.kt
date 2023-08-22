package com.rodcollab.lingoleap

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface TranslatorApiService {

    @FormUrlEncoded
    @POST("/translate")
    suspend fun translate(
        @Field("target_language") target_language: String,
        @Field("source_language") source_language: String,
        @Field("text") text: String
    ): Response<TranslatorResponse>

    @GET("/getLanguages")
    suspend fun getLanguages(): Response<LanguagesResponse>


    companion object {
        const val BASE_URL = "https://text-translator2.p.rapidapi.com"
        val moshi: Moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }
}

@JsonClass(generateAdapter = true)
data class TranslatorResponse(@Json(name = "data") val data: TranslatedText)

@JsonClass(generateAdapter = true)
data class TranslatedText(@Json(name = "translatedText") val translatedText: String)

@JsonClass(generateAdapter = true)
data class LanguagesResponse(@Json(name = "data") val data: LanguagesData)

@JsonClass(generateAdapter = true)
data class LanguagesData(@Json(name = "languages") val languages: List<LanguageData>)

@JsonClass(generateAdapter = true)
data class LanguageData(
    @Json(name = "code") val code: String,
    @Json(name = "name") val name: String
)