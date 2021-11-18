package com.harry.pullgo.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class PullgoRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ImagebbRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class PullgoOkHttpClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ImagebbOkHttpClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class PullgoRetrofitService

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ImagebbRetrofitService
