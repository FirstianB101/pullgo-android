package com.ich.pullgo.data.repository

import com.ich.pullgo.data.remote.PullgoApi
import com.ich.pullgo.data.remote.dto.toAcademy
import com.ich.pullgo.di.PullgoRetrofitService
import com.ich.pullgo.domain.model.Academy
import com.ich.pullgo.domain.repository.AcademyOwnerRepository
import javax.inject.Inject

class AcademyOwnerRepositoryImpl @Inject constructor(
    @PullgoRetrofitService private val api: PullgoApi
): AcademyOwnerRepository{
    override suspend fun getAcademiesTeacherOwned(teacherId: Long): List<Academy>
            = api.getOwnedAcademy(teacherId).map{it.toAcademy()}
}