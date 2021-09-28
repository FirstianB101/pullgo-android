package com.harry.pullgo.data.repository

import android.content.Context
import com.harry.pullgo.data.api.RetrofitClient
import com.harry.pullgo.data.api.RetrofitService
import com.harry.pullgo.data.models.Classroom
import com.harry.pullgo.data.models.Lesson
import com.harry.pullgo.data.objects.LoginInfo

class ClassroomsRepository(context: Context) {
    private val getClassroomsClient = RetrofitClient.getApiService(RetrofitService::class.java,LoginInfo.user?.token,context)

    suspend fun getClassroomById(classroomId: Long) = getClassroomsClient.getClassroomById(classroomId)
    suspend fun getClassroomsByTeacherId(id: Long) = getClassroomsClient.getClassroomsByTeacherId(id)
    suspend fun getAcademiesByTeacherId(id: Long) = getClassroomsClient.getAcademiesTeacherApplied(id)

    fun createNewLesson(lesson: Lesson) = getClassroomsClient.createLesson(lesson)
}