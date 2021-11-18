package com.harry.pullgo.data.repository

import com.harry.pullgo.data.api.PullgoService
import com.harry.pullgo.data.models.Lesson
import com.harry.pullgo.di.PullgoRetrofitService
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