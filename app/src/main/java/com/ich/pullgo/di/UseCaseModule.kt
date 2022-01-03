package com.ich.pullgo.di

import com.ich.pullgo.domain.repository.SignUpRepository
import com.ich.pullgo.domain.use_case.sign_up.CheckIdExistUseCase
import com.ich.pullgo.domain.use_case.sign_up.CreateStudentUseCase
import com.ich.pullgo.domain.use_case.sign_up.CreateTeacherUseCase
import com.ich.pullgo.domain.use_case.sign_up.SignUpUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideSignUpUseCases(repository: SignUpRepository): SignUpUseCases{
        return SignUpUseCases(
            createStudent = CreateStudentUseCase(repository),
            createTeacher = CreateTeacherUseCase(repository),
            checkIdExist = CheckIdExistUseCase(repository)
        )
    }
}