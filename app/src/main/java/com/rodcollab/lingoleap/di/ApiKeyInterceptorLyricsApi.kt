package com.rodcollab.lingoleap.di

import com.rodcollab.lingoleap.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterceptorLyricsApi : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val originalRequest = chain.request()
        val newRequest = originalRequest.newBuilder()
            .addHeader("Authorization", "Bearer " + BuildConfig.API_KEY_GENIUS)
            .build()

        return chain.proceed(newRequest)
    }
}
