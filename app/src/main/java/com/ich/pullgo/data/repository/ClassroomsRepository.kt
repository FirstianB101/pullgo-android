package com.ich.pullgo.data.repository

import com.ich.pullgo.data.api.PullgoService
import com.ich.pullgo.di.PullgoRetrofitService
import com.ich.pullgo.domain.model.Lesson
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