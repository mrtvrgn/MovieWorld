package com.mrtvrgn.practice.movieworld.network.interceptor

import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class AuthInterceptor : Interceptor {
    companion object {
        private const val API_KEY = "01de2fad57f822dea449fd111f409c1b"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestUrl: HttpUrl = request.url
        val url = requestUrl.newBuilder()
            .addQueryParameter("api_key", API_KEY)
            .build()

        val requestBuilder: Request.Builder = request.newBuilder().url(url)
        return chain.proceed(requestBuilder.build())
    }
}