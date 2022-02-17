package com.ich.pullgo.data.repository

import com.ich.pullgo.data.remote.PullgoApi
import com.ich.pullgo.data.remote.dto.toAcademy
import com.ich.pullgo.di.PullgoRetrofitService
import com.ich.pullgo.domain.model.Academy
import com.ich.pullgo.domain.repository.ApplyAcademyRepository
import javax.inject.Inject

class ApplyAcademyRepositoryImpl @Inject constructor(
    @PullgoRetrofitService private val api: PullgoApi
): ApplyAcademyRepository {
    override suspend fun getAcademies(name: String) = api.getAcademiesByName(name).map{a->a.toAcademy()}

    override suspend fun requestStudentApply(studentId: Long, academyId: Long) = api.sendStudentApplyAcademyRequest(studentId, academyId)
    override suspend fun requestTeacherApply(teacherId: Long, academyId: Long) = api.sendTeacherApplyAcademyRequest(teacherId, academyId)
    override suspend fun createAcademy(academy: Academy) = api.createAcademy(academy).toAcademy()
}