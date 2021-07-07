package com.harry.pullgo.data.repository

import com.harry.pullgo.data.api.RetrofitClient

class FindAcademyRepository {
    private val findAcademyClient = RetrofitClient.getApiService()

    suspend fun getAcademies(name: String) = findAcademyClient.getSuchAcademies(name)
}