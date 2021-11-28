package com.harry.pullgo.di

import com.harry.pullgo.data.api.AuthenticationInterceptor
import com.harry.pullgo.data.api.ImageUploadService
import com.harry.pullgo.data.api.PullgoService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object NetworkModule {
    const val BASE_URL = "https://api.pullgo.kr/v1/"
    const val IMAGE_UPLOAD_URL = "https://api.imgbb.com"
    const val IMAGE_UPLOAD_API_KEY = "b3b9649f31a163c6a3d65ecc7949ca6b"

    @Provides
    fun provideAuthInterceptor() = AuthenticationInterceptor()

    @Provides
    @PullgoOkHttpClient
    fun provideAuthInterceptorOkHttpClient(
        authInterceptor: AuthenticationInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()
    }

    @Provides
    @ImagebbOkHttpClient
    fun provideImagebbOkHttpClient(): OkHttpClient{
        return OkHttpClient.Builder().build()
    }

    @Singleton
    @Provides
    @PullgoRetrofit
    fun providePullgoRetrofit(@PullgoOkHttpClient okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .build()

    @Singleton
    @Provides
    @ImagebbRetrofit
    fun provideImagebbRetrofit(@ImagebbOkHttpClient okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(IMAGE_UPLOAD_URL)
        .client(okHttpClient)
        .build()

    @Provides
    @Singleton
    @PullgoRetrofitService
    fun providePullgoService(@PullgoRetrofit retrofit: Retrofit): PullgoService = retrofit.create(PullgoService::class.java)

    @Provides
    @Singleton
    @ImagebbRetrofitService
    fun provideImagebbService(@ImagebbRetrofit retrofit: Retrofit): ImageUploadService = retrofit.create(ImageUploadService::class.java)
}