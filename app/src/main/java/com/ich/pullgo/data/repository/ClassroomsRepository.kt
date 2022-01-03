package com.ich.pullgo.data.repository

<<<<<<< HEAD:app/src/main/java/com/ich/pullgo/data/repository/ClassroomsRepository.kt
import com.ich.pullgo.data.api.PullgoService
import com.ich.pullgo.data.models.Lesson
import com.ich.pullgo.di.PullgoRetrofitService
=======
import com.ich.pullgo.data.remote.PullgoApi
import com.ich.pullgo.data.remote.PullgoService
import com.ich.pullgo.di.PullgoRetrofitService
import com.ich.pullgo.domain.model.Lesson
>>>>>>> ich:app/src/main/java/com/harry/pullgo/data/repository/ClassroomsRepository.kt
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ClassroomsRepository @Inject constructor(
    @PullgoRetrofitService private val getClassroomsClient: PullgoService
) {
    suspend fun getClassroomById(classroomId: Long) = getClassroomsClient.getClassroomById(classroomId)
    suspend fun getClassroomsByTeacherId(id: Long) = getClassroomsClient.getClassroomsByTeacherId(id)
    suspend fun getAcademiesByTeacherId(id: Long) = getClassroomsClient.getAcademiesTeacherApplied(id)

    suspend fun createNewLesson(lesson: Lesson) = getClassroomsClient.createLesson(lesson)
}