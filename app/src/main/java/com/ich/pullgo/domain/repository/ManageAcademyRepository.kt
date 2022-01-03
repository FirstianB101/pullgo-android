package com.ich.pullgo.domain.repository

import com.ich.pullgo.domain.model.Academy
import com.ich.pullgo.domain.model.Student
import com.ich.pullgo.domain.model.Teacher

interface ManageAcademyRepository {
    suspend fun getAcademiesByOwnerId(teacherId: Long): List<Academy>
    suspend fun getTeachersSuchAcademy(academyId: Long): List<Teacher>
    suspend fun getStudentsSuchAcademy(academyId: Long): List<Student>

    suspend fun kickStudent(academyId: Long, studentId: Long)
    suspend fun kickTeacher(academyId: Long, teacherId: Long)
    suspend fun editAcademy(academyId: Long, academy: Academy): Academy
    suspend fun deleteAcademy(academyId: Long)
}