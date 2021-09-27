package com.harry.pullgo.data.api

import okhttp3.Interceptor
import okhttp3.Response

class AuthenticationInterceptor(private var authToken: String?): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        val builder = original.newBuilder().header("Authorization", "Bearer $authToken")

        val request = builder.build()
        return chain.proceed(request)
    }
}