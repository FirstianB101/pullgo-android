package com.harry.pullgo.di

import com.harry.pullgo.application.PullgoApplication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
object tokenModule {

    @Provides
    fun provideToken() = PullgoApplication.instance!!.loginUser.token
}