package com.ich.pullgo.domain.repository

import com.ich.pullgo.domain.model.Academy
import com.ich.pullgo.domain.model.Student
import com.ich.pullgo.domain.model.Teacher
import retrofit2.Response

interface AcceptApplyAcademyRepository{
    suspend fun getStudentsApplyingAcademy(academyId: Long): List<Student>
    suspend fun getTeachersApplyingAcademy(academyId: Long): List<Teacher>
    suspend fun getAcademiesOfTeacher(id: Long): List<Academy>

    suspend fun acceptStudentApply(academyId: Long, studentId: Long): Response<Unit>
    suspend fun acceptTeacherApply(academyId: Long, teacherId: Long): Response<Unit>
    suspend fun denyStudentApply(studentId: Long, academyId: Long): Response<Unit>
    suspend fun denyTeacherApply(teacherId: Long, academyId: Long): Response<Unit>
}