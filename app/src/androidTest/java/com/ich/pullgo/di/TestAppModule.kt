package com.ich.pullgo.di

import com.ich.pullgo.data.repository.FakeUiSignUpRepository
import com.ich.pullgo.domain.repository.SignUpRepository
import com.ich.pullgo.domain.use_case.sign_up.CheckIdExistUseCase
import com.ich.pullgo.domain.use_case.sign_up.CreateStudentUseCase
import com.ich.pullgo.domain.use_case.sign_up.CreateTeacherUseCase
import com.ich.pullgo.domain.use_case.sign_up.SignUpUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier

@Module
@InstallIn(SingletonComponent::class)
object TestAppModule {

    @TestRepository
    @Provides
    fun provideTestSignUpRepository(): SignUpRepository{
        return FakeUiSignUpRepository()
    }

    @TestUseCases
    @Provides
    fun provideTestSignUpUseCases(@TestRepository repository: SignUpRepository): SignUpUseCases{
        return SignUpUseCases(
            checkIdExist = CheckIdExistUseCase(repository),
            createStudent = CreateStudentUseCase(repository),
            createTeacher = CreateTeacherUseCase(repository)
        )
    }
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class TestRepository

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class TestUseCases
