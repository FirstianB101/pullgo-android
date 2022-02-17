package com.ich.pullgo.domain.repository

import com.ich.pullgo.domain.model.Academy
import com.ich.pullgo.domain.model.Classroom
import retrofit2.Response

interface ApplyClassroomRepository {
    suspend fun getAcademiesStudentApplied(id: Long): List<Academy>
    suspend fun getAcademiesTeacherApplied(id: Long): List<Academy>
    suspend fun getClassroomsByAcademyIdAndName(id: Long, name: String): List<Classroom>
    suspend fun sendStudentApplyClassroomRequest(studentId: Long, classroomId: Long): Response<Unit>
    suspend fun sendTeacherApplyClassroomRequest(teacherId: Long, classroomId: Long): Response<Unit>
}