package com.harry.pullgo.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient{
    private val BASE_URL="https://api.pullgo.kr/v1/"
    private var instance: Retrofit?=null

    private fun getInstance():Retrofit{
        if(instance == null){
            instance = Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build()
        }
        return instance!!
    }

    fun getApiService(): RetrofitService {
        return getInstance().create(RetrofitService::class.java)
    }
}