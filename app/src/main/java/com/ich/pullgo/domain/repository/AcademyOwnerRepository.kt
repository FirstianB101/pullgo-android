package com.ich.pullgo.domain.repository

import com.ich.pullgo.domain.model.Academy

interface AcademyOwnerRepository {
    suspend fun getAcademiesTeacherOwned(teacherId: Long): List<Academy>
}