<<<<<<< HEAD:app/src/main/java/com/ich/pullgo/data/api/ImageUploadService.kt
package com.ich.pullgo.data.api

import com.ich.pullgo.data.models.ImageUploadResponse
=======
package com.ich.pullgo.data.remote

import com.ich.pullgo.data.remote.dto.ImageUploadResponseDto
import com.ich.pullgo.domain.model.ImageUploadResponse
>>>>>>> ich:app/src/main/java/com/ich/pullgo/data/remote/ImageUploadApi.kt
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ImageUploadApi {
    @Multipart
    @POST("/1/upload")
    suspend fun requestUpload(@Query("key")key: String, @Part("image") image: RequestBody): Response<ImageUploadResponse>
}