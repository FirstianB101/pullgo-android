package com.harry.pullgo.data.api

import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.harry.pullgo.ui.login.LoginActivity
import okhttp3.Interceptor
import okhttp3.Response

class AuthenticationInterceptor(private var authToken: String?, val context: Context): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        val builder = original.newBuilder().header("Authorization", "Bearer $authToken")

        val request = builder.build()
        val response = chain.proceed(request)

        if(response.code() == 401){
            val intent = Intent(context,LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            Toast.makeText(context,"로그인 정보가 만료되었습니다",Toast.LENGTH_SHORT).show()
            context.startActivity(intent)
        }
        return response
    }
}