package com.ich.pullgo.data.repository

import com.ich.pullgo.data.remote.PullgoApi
import com.ich.pullgo.data.remote.dto.toAcademy
import com.ich.pullgo.data.remote.dto.toClassroom
import com.ich.pullgo.di.PullgoRetrofitService
import com.ich.pullgo.domain.repository.ApplyClassroomRepository
import javax.inject.Inject

class ApplyClassroomRepositoryImpl @Inject constructor(
    @PullgoRetrofitService private val api: PullgoApi
): ApplyClassroomRepository {
    override suspend fun getAcademiesStudentApplied(id: Long) = api.getAcademiesStudentApplied(id).map{a->a.toAcademy()}
    override suspend fun getAcademiesTeacherApplied(id: Long) = api.getAcademiesTeacherApplied(id).map{a->a.toAcademy()}
    override suspend fun getClassroomsByAcademyIdAndName(id: Long, name: String) = api.getClassroomsByNameAndAcademyId(id,name).map{ c->c.toClassroom()}
    override suspend fun sendStudentApplyClassroomRequest(studentId: Long, classroomId: Long) =
        api.sendStudentApplyClassroomRequest(studentId,classroomId)
    override suspend fun sendTeacherApplyClassroomRequest(teacherId: Long, classroomId: Long) =
        api.sendTeacherApplyClassroomRequest(teacherId,classroomId)
}