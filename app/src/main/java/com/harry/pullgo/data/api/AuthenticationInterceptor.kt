package com.harry.pullgo.data.api

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.harry.pullgo.application.PullgoApplication
import com.harry.pullgo.ui.login.LoginActivity
import okhttp3.Interceptor
import okhttp3.Response

class AuthenticationInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val authToken = PullgoApplication.instance!!.loginUser.token

        val builder = original.newBuilder().header("Authorization", "Bearer ${authToken ?: ""}")

        val request = builder.build()
        val response = chain.proceed(request)

//        if(response.code() == 401){
//            val context = PullgoApplication.instance
//            val intent = Intent(context,LoginActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
//            context?.showToast("로그인 정보가 만료되었습니다")
//            context?.startActivity(intent)
//        }
        return response
    }
}