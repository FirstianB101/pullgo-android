package com.harry.pullgo.data.repository

import com.harry.pullgo.data.api.RetrofitClient

class ManageClassroomDetailsRepository {
    private val detailsClient = RetrofitClient.getApiService()

    suspend fun getStudentsAppliedClassroom(classroomId: Long) = detailsClient.getStudentsAppliedClassroom(classroomId)
    suspend fun getTeachersAppliedClassroom(classroomId: Long) = detailsClient.getTeachersAppliedClassroom(classroomId)
    suspend fun getStudentsRequestApplyClassroom(classroomId: Long) = detailsClient.getStudentsRequestApplyClassroom(classroomId)
    suspend fun getTeachersRequestApplyClassroom(classroomId: Long) = detailsClient.getTeachersRequestApplyClassroom(classroomId)
}