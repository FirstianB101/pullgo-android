package com.ich.pullgo.data.repository

import com.ich.pullgo.data.remote.PullgoApi
import com.ich.pullgo.di.PullgoRetrofitService
import javax.inject.Inject

class ManageRequestRepositoryImpl @Inject constructor(
    @PullgoRetrofitService private val api: PullgoApi
) {
    suspend fun getAcademiesTeacherApplying(teacherId: Long) = api.getAcademiesTeacherApplying(teacherId)
    suspend fun getClassroomsTeacherApplying(teacherId: Long) = api.getClassroomsTeacherApplying(teacherId)
    suspend fun getAcademiesStudentApplying(studentId: Long) = api.getAcademiesStudentApplying(studentId)
    suspend fun getClassroomsStudentApplying(studentId: Long) = api.getClassroomsStudentApplying(studentId)

    suspend fun removeTeacherAppliedAcademy(teacherId: Long, academyId: Long) = api.removeTeacherAppliedAcademy(teacherId, academyId)
    suspend fun removeTeacherAppliedClassroom(teacherId: Long, classroomId: Long) = api.removeTeacherAppliedClassroom(teacherId, classroomId)
    suspend fun removeStudentAppliedAcademy(studentId: Long, academyId: Long) = api.removeStudentAppliedAcademy(studentId, academyId)
    suspend fun removeStudentAppliedClassroom(studentId: Long, classroomId: Long) = api.removeStudentAppliedClassroom(studentId, classroomId)
}