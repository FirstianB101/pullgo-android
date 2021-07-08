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

    @GET("teachers/")
    suspend fun getAcademiesByTeacherAppliedAcademyId(@Query("appliedAcademyId")teacherId:Long): Response<List<Academy>>

    @PATCH("teachers/{id}")
    fun changeTeacherInfo(@Path("id")id:Long, @Body teacher: Teacher):Call<Teacher>


    @GET("academies/")
    fun getAcademyList(): Call<List<Academy>>

    @GET("academies/")
    suspend fun getAcademiesByName(@Query("nameLike")name:String): Response<List<Academy>>

    @GET("academies/")
    fun getOwnedAcademy(@Query("ownerId")id:Long): Call<List<Academy>>


    @GET("students/{id}")
    suspend fun getStudent(@Path("id")id:Long): Response<Student>

    @GET("students/")
    suspend fun getAcademiesByStudentAppliedAcademyId(@Query("appliedAcademyId")studentId:Long): Response<List<Academy>>

    @POST("students/")
    fun createStudent(@Body student: Student):Call<Student>

    @POST("students/{studentId}/apply-academy")
    fun sendStudentApplyAcademyRequest(@Path("studentId") studentId : Long, @Body academyId : Long): Call<Unit>

    @PATCH("students/{id}")
    fun changeStudentInfo(@Path("id")id:Long, @Body student: Student):Call<Student>
}