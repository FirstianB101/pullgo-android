package com.harry.pullgo.data.repository

import com.harry.pullgo.data.api.RetrofitClient

class ClassroomsRepository {
    private val getClassroomsClient = RetrofitClient.getApiService()

    suspend fun getClassroomsByTeacherId(id: Long) = getClassroomsClient.getClassroomsByTeacherId(id)
}