package com.ich.pullgo.data.repository

import com.ich.pullgo.data.remote.PullgoApi
import com.ich.pullgo.di.PullgoRetrofitService
import com.ich.pullgo.domain.model.Lesson
import javax.inject.Inject

class ClassroomsRepositoryImpl @Inject constructor(
    @PullgoRetrofitService private val api: PullgoApi
) {
    suspend fun getClassroomById(classroomId: Long) = api.getClassroomById(classroomId)
    suspend fun getClassroomsByTeacherId(id: Long) = api.getClassroomsByTeacherId(id)
    suspend fun getAcademiesByTeacherId(id: Long) = api.getAcademiesTeacherApplied(id)

    suspend fun createNewLesson(lesson: Lesson) = api.createLesson(lesson)
}