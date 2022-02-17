package com.ich.pullgo.domain.repository

import com.ich.pullgo.domain.model.Academy
import retrofit2.Response

interface ApplyAcademyRepository{
    suspend fun getAcademies(name: String): List<Academy>

    suspend fun requestStudentApply(studentId: Long, academyId: Long): Response<Unit>
    suspend fun requestTeacherApply(teacherId: Long, academyId: Long): Response<Unit>
    suspend fun createAcademy(academy: Academy): Academy
}