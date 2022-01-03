package com.ich.pullgo.data.repository

import com.ich.pullgo.data.remote.PullgoApi
import com.ich.pullgo.data.remote.PullgoService
import com.ich.pullgo.di.PullgoRetrofitService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ManageRequestRepository @Inject constructor(
    @PullgoRetrofitService private val manageRequestClient: PullgoService
) {
    suspend fun getTeacherApplyingAcademies(teacherId: Long) = manageRequestClient.getAcademiesTeacherApplying(teacherId)
    suspend fun getTeacherApplyingClassrooms(teacherId: Long) = manageRequestClient.getClassroomsTeacherApplying(teacherId)
    suspend fun getStudentApplyingAcademies(studentId: Long) = manageRequestClient.getAcademiesStudentApplying(studentId)
    suspend fun getStudentApplyingClassrooms(studentId: Long) = manageRequestClient.getClassroomsStudentApplying(studentId)

    suspend fun removeTeacherAppliedAcademy(teacherId: Long, academyId: Long) = manageRequestClient.removeTeacherAppliedAcademy(teacherId, academyId)
    suspend fun removeTeacherAppliedClassroom(teacherId: Long, classroomId: Long) = manageRequestClient.removeTeacherAppliedClassroom(teacherId, classroomId)
    suspend fun removeStudentAppliedAcademy(studentId: Long, academyId: Long) = manageRequestClient.removeStudentAppliedAcademy(studentId, academyId)
    suspend fun removeStudentAppliedClassroom(studentId: Long, classroomId: Long) = manageRequestClient.removeStudentAppliedClassroom(studentId, classroomId)
}