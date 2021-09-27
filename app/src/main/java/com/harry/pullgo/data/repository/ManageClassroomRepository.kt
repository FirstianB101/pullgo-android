package com.harry.pullgo.data.repository

import com.harry.pullgo.data.api.RetrofitClient
import com.harry.pullgo.data.api.RetrofitService
import com.harry.pullgo.data.models.Classroom
import com.harry.pullgo.data.models.Student
import com.harry.pullgo.data.objects.LoginInfo

class ManageClassroomRepository {
    private val manageClassroomClient = RetrofitClient.getApiService(RetrofitService::class.java, LoginInfo.user?.token)

    suspend fun getStudentsAppliedClassroom(classroomId: Long) = manageClassroomClient.getStudentsAppliedClassroom(classroomId)
    suspend fun getTeachersAppliedClassroom(classroomId: Long) = manageClassroomClient.getTeachersAppliedClassroom(classroomId)
    suspend fun getStudentsRequestApplyClassroom(classroomId: Long) = manageClassroomClient.getStudentsRequestApplyClassroom(classroomId)
    suspend fun getTeachersRequestApplyClassroom(classroomId: Long) = manageClassroomClient.getTeachersRequestApplyClassroom(classroomId)

    fun createClassroom(classroom: Classroom) = manageClassroomClient.createClassroom(classroom)
    fun kickStudentFromClassroom(classroomId: Long, studentId: Long) = manageClassroomClient.kickStudentFromClassroom(classroomId, studentId)
    fun editClassroom(classroomId: Long, classroom: Classroom) = manageClassroomClient.editClassroom(classroomId, classroom)
    fun deleteClassroom(classroomId: Long) = manageClassroomClient.deleteClassroom(classroomId)

    fun acceptStudent(classroomId: Long, studentId: Long) = manageClassroomClient.acceptStudentApplyClassroom(classroomId, studentId)
    fun acceptTeacher(classroomId: Long, teacherId: Long) = manageClassroomClient.acceptTeacherApplyClassroom(classroomId, teacherId)
    fun denyStudent(studentId: Long, classroomId: Long) = manageClassroomClient.removeStudentClassroomRequest(studentId, classroomId)
    fun denyTeacher(teacherId: Long, classroomId: Long) = manageClassroomClient.removeTeacherClassroomRequest(teacherId, classroomId)
}