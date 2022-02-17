package com.ich.pullgo.di

import com.ich.pullgo.data.remote.ImageUploadApi
import com.ich.pullgo.data.remote.PullgoApi
import com.ich.pullgo.data.repository.*
import com.ich.pullgo.domain.repository.*
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
    fun provideLoginRepository(@PullgoRetrofitService api: PullgoApi): LoginRepository{
        return LoginRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideSignUpRepository(@PullgoRetrofitService api: PullgoApi): SignUpRepository {
        return SignUpRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideAcademyOwnerRepository(@PullgoRetrofitService api: PullgoApi): AcademyOwnerRepository{
        return AcademyOwnerRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideChangeInfoRepository(@PullgoRetrofitService api: PullgoApi): ChangeInfoRepository {
        return ChangeInfoRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideLessonsRepository(@PullgoRetrofitService api: PullgoApi): LessonsRepository{
        return LessonsRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideManageRequestRepository(@PullgoRetrofitService api: PullgoApi): ManageRequestRepository{
        return ManageRequestRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideApplyClassroomRepository(@PullgoRetrofitService api: PullgoApi): ApplyClassroomRepository{
        return ApplyClassroomRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideAcceptApplyAcademyRepository(@PullgoRetrofitService api: PullgoApi): AcceptApplyAcademyRepository{
        return AcceptApplyAcademyRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideApplyAcademyRepository(@PullgoRetrofitService api: PullgoApi): ApplyAcademyRepository{
        return ApplyAcademyRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideManageAcademyRepository(@PullgoRetrofitService api: PullgoApi): ManageAcademyRepository{
        return ManageAcademyRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideManageClassroomRepository(@PullgoRetrofitService api: PullgoApi): ManageClassroomRepository{
        return ManageClassroomRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideManageQuestionRepository(
        @PullgoRetrofitService pullgoApi: PullgoApi,
        @ImagebbRetrofitService imagebbApi: ImageUploadApi
    ): ManageQuestionRepository{
        return ManageQuestionRepositoryImpl(pullgoApi,imagebbApi)
    }

    @Provides
    @Singleton
    fun provideExamsRepository(@PullgoRetrofitService api: PullgoApi): ExamsRepository{
        return ExamsRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideExamHistoryRepository(@PullgoRetrofitService api: PullgoApi): ExamHistoryRepository{
        return ExamHistoryRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideTakeExamRepository(@PullgoRetrofitService api: PullgoApi): TakeExamRepository{
        return TakeExamRepositoryImpl(api)
    }
}