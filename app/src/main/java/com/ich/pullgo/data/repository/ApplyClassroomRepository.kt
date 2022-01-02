package com.ich.pullgo.data.repository

import com.ich.pullgo.data.api.PullgoService
import com.ich.pullgo.di.PullgoRetrofitService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApplyClassroomRepository @Inject constructor(
    @PullgoRetrofitService private val applyClassroomClient: PullgoService
) {
    suspend fun getAcademiesStudentApplied(id: Long) = applyClassroomClient.getAcademiesStudentApplied(id)
    suspend fun getAcademiesTeacherApplied(id: Long) = applyClassroomClient.getAcademiesTeacherApplied(id)
    suspend fun getClassroomsByNameAndAcademyID(id: Long, name: String) = applyClassroomClient.getClassroomsByNameAndAcademyId(id,name)
    suspend fun studentApplyClassroom(studentId: Long, classroomId: Long) =
        applyClassroomClient.sendStudentApplyClassroomRequest(studentId,classroomId)
    suspend fun teacherApplyClassroom(teacherId: Long, classroomId: Long) =
        applyClassroomClient.sendTeacherApplyClassroomRequest(teacherId,classroomId)
}