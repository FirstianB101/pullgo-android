package com.harry.pullgo.data.repository

import com.harry.pullgo.data.api.PullgoService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ManageRequestRepository @Inject constructor(
    private val manageRequestClient: PullgoService
) {
    suspend fun getTeacherApplyingAcademies(teacherId: Long) = manageRequestClient.getTeacherApplyingAcademies(teacherId)
    suspend fun getTeacherApplyingClassrooms(teacherId: Long) = manageRequestClient.getTeacherApplyingClassrooms(teacherId)
    suspend fun getStudentApplyingAcademies(studentId: Long) = manageRequestClient.getStudentApplyingAcademies(studentId)
    suspend fun getStudentApplyingClassrooms(studentId: Long) = manageRequestClient.getStudentApplyingClassrooms(studentId)

    fun removeTeacherAppliedAcademy(teacherId: Long, academyId: Long) = manageRequestClient.removeTeacherAppliedAcademy(teacherId, academyId)
    fun removeTeacherAppliedClassroom(teacherId: Long, classroomId: Long) = manageRequestClient.removeTeacherAppliedClassroom(teacherId, classroomId)
    fun removeStudentAppliedAcademy(studentId: Long, academyId: Long) = manageRequestClient.removeStudentAppliedAcademy(studentId, academyId)
    fun removeStudentAppliedClassroom(studentId: Long, classroomId: Long) = manageRequestClient.removeStudentAppliedClassroom(studentId, classroomId)
}