package com.ich.pullgo.data.repository

import com.ich.pullgo.data.remote.PullgoApi
import com.ich.pullgo.di.PullgoRetrofitService
import javax.inject.Inject

class AppliedAcademyGroupRepositoryImpl @Inject constructor(
    @PullgoRetrofitService private val api: PullgoApi
){
    suspend fun getStudentAppliedAcademies(id: Long) = api.getAcademiesByStudentAppliedAcademyId(id)
    suspend fun getTeacherAppliedAcademies(id: Long) = api.getAcademiesByTeacherAppliedAcademyId(id)
}