package com.ich.pullgo.di

import com.ich.pullgo.data.remote.PullgoApi
import com.ich.pullgo.data.repository.LoginRepositoryImpl
import com.ich.pullgo.data.repository.SignUpRepositoryImpl
import com.ich.pullgo.domain.repository.LoginRepository
import com.ich.pullgo.domain.repository.SignUpRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideLoginRepository(@PullgoRetrofitApi api: PullgoApi): LoginRepository{
        return LoginRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideSignUpRepository(@PullgoRetrofitApi api: PullgoApi): SignUpRepository {
        return SignUpRepositoryImpl(api)
    }
}