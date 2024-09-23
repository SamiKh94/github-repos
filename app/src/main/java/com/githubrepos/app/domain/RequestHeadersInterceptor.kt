package com.githubrepos.app.domain

import okhttp3.Interceptor
import okhttp3.Response

class RequestHeadersInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder().apply {
            addHeader(AUTHORIZATION, "Bearer $TOKEN")
        }
        return chain.proceed(requestBuilder.build())
    }

    companion object {
        private const val AUTHORIZATION = "Authorization"
        const val TOKEN = "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"
    }
}
