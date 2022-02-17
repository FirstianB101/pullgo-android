package com.ich.pullgo.data.remote

import com.ich.pullgo.application.PullgoApplication
import okhttp3.Interceptor
import okhttp3.Response

class AuthenticationInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val authToken = PullgoApplication.instance?.getLoginUser()?.token

        val builder = original.newBuilder().header("Authorization", "Bearer ${authToken ?: ""}")

        val request = builder.build()

        return chain.proceed(request)
    }
}