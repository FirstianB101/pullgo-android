package com.ich.pullgo.domain.repository

import com.ich.pullgo.domain.model.Academy

interface FindAcademyRepository{
    suspend fun getAcademies(name: String): List<Academy>

    suspend fun requestStudentApply(studentId: Long, academyId: Long)
    suspend fun requestTeacherApply(teacherId: Long, academyId: Long)
    suspend fun createAcademy(academy: Academy): Academy
}