package com.harry.pullgo.data.api

import com.harry.pullgo.data.models.ImageUploadResponse
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ImageUploadService {
    @Multipart
    @POST("/1/upload")
    suspend fun requestUpload(@Query("key")key: String, @Part("image") image: RequestBody): Response<ImageUploadResponse>
}