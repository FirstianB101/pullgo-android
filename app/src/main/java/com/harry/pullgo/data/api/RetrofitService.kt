package com.harry.pullgo.data.api

import com.harry.pullgo.data.objects.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface RetrofitService {

    @POST("teachers/")
    fun createTeacher(@Body params: Teacher):Call<Teacher>

    @PATCH("teachers/{id}")
    fun changeTeacherInfo(@Path("id")id:Long, @Body teacher: Teacher):Call<Teacher>

    @GET("teachers/{id}")
    suspend fun getTeacher(@Path("id")id:Long):Response<Teacher>

    @GET("teachers/")
    suspend fun getAcademiesByTeacherAppliedAcademyId(@Query("appliedAcademyId")teacherId:Long): Response<List<Academy>>

    @GET("academy/classrooms/")
    suspend fun getClassroomsByTeacherId(@Query("teacherId")id:Long): Response<List<Classroom>>

    @GET("academy/classroom/lessons")
    suspend fun getLessonsByTeacherId(@Query("teacherId")id:Long): Response<List<Lesson>>


    @GET("academies/")
    suspend fun getAcademiesByName(@Query("nameLike")name:String): Response<List<Academy>>

    @GET("academies/")
    fun getOwnedAcademy(@Query("ownerId")id:Long): Call<List<Academy>>

    @GET("academy/classroom/lessons")
    suspend fun getLessonsByDatesBetween(@Query("sinceDate")sinceDate: String,
                                         @Query("untilDate")untilDate:String): Response<List<Lesson>>


    @POST("students/")
    fun createStudent(@Body student: Student):Call<Student>

    @POST("students/{studentId}/apply-academy")
    fun sendStudentApplyAcademyRequest(@Path("studentId") studentId : Long, @Body academyId : Long): Call<Unit>

    @PATCH("students/{id}")
    fun changeStudentInfo(@Path("id")id:Long, @Body student: Student):Call<Student>

    @POST("academy/classroom/lessons")
    fun createLesson(@Body lesson: Lesson): Call<Lesson>

    @GET("students/{id}")
    suspend fun getStudent(@Path("id")id:Long): Response<Student>

    @GET("students/")
    suspend fun getAcademiesByStudentAppliedAcademyId(@Query("appliedAcademyId")studentId:Long): Response<List<Academy>>

    @GET("academy/classroom/lessons")
    suspend fun getLessonsByStudentId(@Query("studentId")id:Long): Response<List<Lesson>>
}