package com.ich.pullgo.data.repository

import com.ich.pullgo.data.remote.PullgoApi
import com.ich.pullgo.data.remote.dto.toAcademy
import com.ich.pullgo.data.remote.dto.toStudent
import com.ich.pullgo.data.remote.dto.toTeacher
import com.ich.pullgo.di.PullgoRetrofitService
import com.ich.pullgo.domain.model.Teacher
import com.ich.pullgo.domain.repository.AcceptApplyAcademyRepository
import javax.inject.Inject

class AcceptApplyAcademyRepositoryImpl @Inject constructor(
    @PullgoRetrofitService private val api: PullgoApi
): AcceptApplyAcademyRepository {
    override suspend fun getStudentsAppliedAcademy(id: Long) = api.getStudentsRequestApplyAcademy(id,"schoolYear").map{ it.toStudent() }
    override suspend fun getTeachersAppliedAcademy(id: Long): List<Teacher> = api.getTeachersRequestApplyAcademy(id).map { it.toTeacher() }
    override suspend fun getAcademiesOfTeacher(id: Long) = api.getAcademiesTeacherApplied(id).map{ it.toAcademy() }

    override suspend fun acceptStudentApply(academyId: Long, studentId: Long) = api.acceptStudentApplyAcademy(academyId, studentId)
    override suspend fun acceptTeacherApply(academyId: Long, teacherId: Long) = api.acceptTeacherApplyAcademy(academyId, teacherId)
    override suspend fun denyStudentApply(studentId: Long, academyId: Long) = api.removeStudentAcademyRequest(studentId, academyId)
    override suspend fun denyTeacherApply(teacherId: Long, academyId: Long) = api.removeTeacherAcademyRequest(teacherId, academyId)
}