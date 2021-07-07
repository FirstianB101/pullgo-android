package com.harry.pullgo.data.api

import com.harry.pullgo.data.objects.Academy
import com.harry.pullgo.data.objects.Student
import com.harry.pullgo.data.objects.Teacher
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface RetrofitService {
    @GET("teachers/")
    fun getTeachersList(): Call<List<Teacher>>

    @POST("teachers/")
    fun createTeacher(@Body params: Teacher):Call<Teacher>

    @GET("teachers/{id}")
    suspend fun getTeacher(@Path("id")id:Long):Response<Teacher>


    @GET("academies/")
    fun getAcademyList(): Call<List<Academy>>

    @GET("academies")
    suspend fun getSuchAcademies(@Query("nameLike")name:String): Response<List<Academy>>

    @GET("academies")
    fun getOwnedAcademy(@Query("ownerId")id:Long): Call<List<Academy>>


    @GET("students/{id}")
    suspend fun getStudent(@Path("id")id:Long): Response<Student>

    @POST("students/")
    fun createStudent(@Body params: Student):Call<Student>

    @POST("students/{studentId}/apply-academy")
    fun sendStudentApplyAcademyRequest(@Path("studentId") studentId : Long, @Body academyId : Long): Call<Unit>
}