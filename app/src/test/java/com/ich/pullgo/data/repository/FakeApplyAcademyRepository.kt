package com.ich.pullgo.data.repository

import com.ich.pullgo.domain.model.Academy
import com.ich.pullgo.domain.repository.ApplyAcademyRepository
import retrofit2.Response

class FakeApplyAcademyRepository: ApplyAcademyRepository {

    val academies = mutableListOf<Academy>()

    override suspend fun getAcademies(name: String): List<Academy> {
        return academies
    }

    override suspend fun requestStudentApply(studentId: Long, academyId: Long): Response<Unit> {
        return Response.success(Unit)
    }

    override suspend fun requestTeacherApply(teacherId: Long, academyId: Long): Response<Unit> {
        return Response.success(Unit)
    }

    override suspend fun createAcademy(academy: Academy): Academy {
        academies.add(academy)
        return academy
    }
}