package com.rodcollab.lingoleap

import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        val originalRequest = chain.request()
        val newRequest = originalRequest.newBuilder()
            .addHeader("X-RapidAPI-Key",BuildConfig.API_KEY)
            .build()

        return chain.proceed(newRequest)
    }
}