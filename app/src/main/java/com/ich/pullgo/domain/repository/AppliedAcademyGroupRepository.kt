package com.ich.pullgo.domain.repository

import com.ich.pullgo.domain.model.Academy

interface AppliedAcademyGroupRepository {
    suspend fun getStudentAppliedAcademies(id: Long): List<Academy>
    suspend fun getTeacherAppliedAcademies(id: Long): List<Academy>
}