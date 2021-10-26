package com.harry.pullgo.data.repository

import android.content.Context
import com.harry.pullgo.data.api.RetrofitClient
import com.harry.pullgo.data.api.RetrofitService

class ApplyClassroomRepository(context: Context, token: String?) {
    private val applyClassroomClient = RetrofitClient.getApiService(RetrofitService::class.java,token,context)

    suspend fun getAcademiesStudentApplied(id: Long) = applyClassroomClient.getAcademiesStudentApplied(id)
    suspend fun getAcademiesTeacherApplied(id: Long) = applyClassroomClient.getAcademiesTeacherApplied(id)
    suspend fun getClassroomsByNameAndAcademyID(id: Long, name: String) = applyClassroomClient.getClassroomsByNameAndAcademyId(id,name)
    fun studentApplyClassroom(studentId: Long, classroomId: Long) =
        applyClassroomClient.sendStudentApplyClassroomRequest(studentId,classroomId)
    fun teacherApplyClassroom(teacherId: Long, classroomId: Long) =
        applyClassroomClient.sendTeacherApplyClassroomRequest(teacherId,classroomId)
}