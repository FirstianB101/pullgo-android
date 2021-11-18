package com.harry.pullgo.data.repository

import com.harry.pullgo.data.api.PullgoService
import com.harry.pullgo.di.PullgoRetrofitService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppliedAcademyGroupRepository @Inject constructor(
    @PullgoRetrofitService private val appliedAcademyClient: PullgoService
){
    suspend fun getStudentAppliedAcademies(id: Long) = appliedAcademyClient.getAcademiesByStudentAppliedAcademyId(id)
    suspend fun getTeacherAppliedAcademies(id: Long) = appliedAcademyClient.getAcademiesByTeacherAppliedAcademyId(id)
}