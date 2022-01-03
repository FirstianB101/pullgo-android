package com.ich.pullgo.data.repository

import com.ich.pullgo.data.remote.PullgoApi
import com.ich.pullgo.di.PullgoRetrofitService
import javax.inject.Inject

class ApplyClassroomRepositoryImpl @Inject constructor(
    @PullgoRetrofitService private val api: PullgoApi
) {
    suspend fun getAcademiesStudentApplied(id: Long) = api.getAcademiesStudentApplied(id)
    suspend fun getAcademiesTeacherApplied(id: Long) = api.getAcademiesTeacherApplied(id)
    suspend fun getClassroomsByNameAndAcademyID(id: Long, name: String) = api.getClassroomsByNameAndAcademyId(id,name)
    suspend fun studentApplyClassroom(studentId: Long, classroomId: Long) =
        api.sendStudentApplyClassroomRequest(studentId,classroomId)
    suspend fun teacherApplyClassroom(teacherId: Long, classroomId: Long) =
        api.sendTeacherApplyClassroomRequest(teacherId,classroomId)
}