package com.harry.pullgo.data.repository

import com.harry.pullgo.data.api.RetrofitClient
import com.harry.pullgo.data.api.RetrofitService
import com.harry.pullgo.data.models.Lesson

class ClassroomsRepository(token: String?) {
    private val getClassroomsClient = RetrofitClient.getApiService(RetrofitService::class.java,token)

    suspend fun getClassroomById(classroomId: Long) = getClassroomsClient.getClassroomById(classroomId)
    suspend fun getClassroomsByTeacherId(id: Long) = getClassroomsClient.getClassroomsByTeacherId(id)
    suspend fun getAcademiesByTeacherId(id: Long) = getClassroomsClient.getAcademiesTeacherApplied(id)

    fun createNewLesson(lesson: Lesson) = getClassroomsClient.createLesson(lesson)
}