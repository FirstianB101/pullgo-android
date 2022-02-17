package com.ich.pullgo.data.repository

import com.ich.pullgo.data.remote.PullgoApi
import com.ich.pullgo.data.remote.dto.toAcademy
import com.ich.pullgo.data.remote.dto.toStudent
import com.ich.pullgo.data.remote.dto.toTeacher
import com.ich.pullgo.di.PullgoRetrofitService
import com.ich.pullgo.domain.model.Academy
import com.ich.pullgo.domain.repository.ManageAcademyRepository
import javax.inject.Inject

class ManageAcademyRepositoryImpl @Inject constructor(
    @PullgoRetrofitService private val api: PullgoApi
): ManageAcademyRepository {
    override suspend fun getAcademiesByOwnerId(teacherId: Long) = api.getOwnedAcademy(teacherId).map{a->a.toAcademy()}
    override suspend fun getTeachersSuchAcademy(academyId: Long) = api.getTeachersSuchAcademy(academyId).map{t->t.toTeacher()}
    override suspend fun getStudentsSuchAcademy(academyId: Long) = api.getStudentsSuchAcademy(academyId).map{s->s.toStudent()}

    override suspend fun kickStudent(academyId: Long, studentId: Long) = api.kickStudent(academyId,studentId)
    override suspend fun kickTeacher(academyId: Long, teacherId: Long) = api.kickTeacher(academyId,teacherId)
    override suspend fun editAcademy(academyId: Long, academy: Academy) = api.editAcademy(academyId, academy).toAcademy()
    override suspend fun deleteAcademy(academyId: Long) = api.deleteAcademy(academyId)
}