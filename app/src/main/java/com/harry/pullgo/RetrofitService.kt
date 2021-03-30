package com.harry.pullgo

import retrofit2.Call
import retrofit2.http.*

interface RetrofitService {
    @GET("teachers/")
    fun getTeachersList(): Call<List<Teacher>>

    @POST("teachers/")
    fun createTeacher(@Body params: Teacher):Call<Teacher>

    @GET("academies/")
    fun getAcademyList(): Call<List<Academy>>

    @GET("academies")
    fun getSuchAcademies(@Query("nameLike")name:String): Call<List<Academy>>

    @POST("students/")
    fun createStudent(@Body params: Student):Call<Student>

    @POST("students/{studentId}/apply-academy")
    fun sendStudentApplyAcademyRequest(@Path("studentId") studentId:Long,@Body academyId:Long): Call<Unit>
}