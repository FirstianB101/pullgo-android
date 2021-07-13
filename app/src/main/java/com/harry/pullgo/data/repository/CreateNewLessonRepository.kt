package com.harry.pullgo.data.repository

import com.harry.pullgo.data.api.RetrofitClient
import com.harry.pullgo.data.objects.Lesson

class CreateNewLessonRepository {
    private val createNewLessonClient = RetrofitClient.getApiService()

    suspend fun getClassroomsByTeacherId(id: Long) = createNewLessonClient.getClassroomsByTeacherId(id)
}