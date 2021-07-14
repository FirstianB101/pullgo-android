package com.harry.pullgo.data.repository

import com.harry.pullgo.data.api.RetrofitClient

class LessonsRepository {
    private val lessonClient = RetrofitClient.getApiService()

    suspend fun getStudentLessons(id: Long) = lessonClient.getLessonsByStudentId(id)
    suspend fun getTeacherLessons(id: Long) = lessonClient.getLessonsByTeacherId(id)
}