package com.ich.pullgo.data.repository

<<<<<<< HEAD:app/src/main/java/com/ich/pullgo/data/repository/AppliedAcademyGroupRepository.kt
import com.ich.pullgo.data.api.PullgoService
=======
import com.ich.pullgo.data.remote.PullgoApi
import com.ich.pullgo.data.remote.PullgoService
>>>>>>> ich:app/src/main/java/com/harry/pullgo/data/repository/AppliedAcademyGroupRepository.kt
import com.ich.pullgo.di.PullgoRetrofitService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppliedAcademyGroupRepository @Inject constructor(
    @PullgoRetrofitService private val appliedAcademyClient: PullgoService
){
    suspend fun getStudentAppliedAcademies(id: Long) = appliedAcademyClient.getAcademiesByStudentAppliedAcademyId(id)
    suspend fun getTeacherAppliedAcademies(id: Long) = appliedAcademyClient.getAcademiesByTeacherAppliedAcademyId(id)
}