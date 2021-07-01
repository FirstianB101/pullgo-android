package com.harry.pullgo.interfaces

import com.harry.pullgo.objects.Academy
import com.harry.pullgo.objects.Student
import com.harry.pullgo.objects.Teacher
import retrofit2.Call
import retrofit2.http.*

interface RetrofitService {
    @GET("teachers/")
    fun getTeachersList(): Call<List<Teacher>>

    @POST("teachers/")
    fun createTeacher(@Body params: Teacher):Call<Teacher>

    @GET("teachers/{id}")
    fun getTeacher(@Path("id")id:Long):Call<Teacher>


    @GET("academies/")
    fun getAcademyList(): Call<List<Academy>>

    @GET("academies")
    fun getSuchAcademies(@Query("nameLike")name:String): Call<List<Academy>>

    @GET("academies")
    fun getOwnedAcademy(@Query("ownerId")id:Long): Call<List<Academy>>


    @GET("students/{id}")
    fun getStudent(@Path("id")id:Long):Call<Student>

    @POST("students/")
    fun createStudent(@Body params: Student):Call<Student>

    @POST("students/{studentId}/apply-academy")
    fun sendStudentApplyAcademyRequest(@Path("studentId") studentId : Long, @Body academyId : Long): Call<Unit>
}