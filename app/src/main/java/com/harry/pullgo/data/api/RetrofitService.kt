package com.harry.pullgo.data.api

import com.harry.pullgo.data.objects.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface RetrofitService {

    @POST("teachers/")
    fun createTeacher(@Body params: Teacher):Call<Teacher>

    @POST("teachers/{id}/apply-classroom")
    fun sendTeacherApplyClassroomRequest(@Path("id")teacherId: Long, @Body classroomId: Long): Call<Unit>

    @POST("teachers/{id}/apply-academy")
    fun sendTeacherApplyAcademyRequest(@Path("id")teacherId: Long, @Body academyId: Long): Call<Unit>

    @PATCH("teachers/{id}")
    fun changeTeacherInfo(@Path("id")id:Long, @Body teacher: Teacher):Call<Teacher>

    @GET("teachers/{id}")
    suspend fun getTeacher(@Path("id")id:Long):Response<Teacher>

    @GET("teachers/")
    suspend fun getAcademiesByTeacherAppliedAcademyId(@Query("appliedAcademyId")teacherId:Long): Response<List<Academy>>

    @GET("teachers")
    suspend fun getTeachersRequestApplyAcademy(@Query("appliedAcademyId")id: Long): Response<List<Teacher>>

    @GET("teachers")
    suspend fun getTeachersRequestApplyClassroom(@Query("appliedClassroomId")id: Long): Response<List<Teacher>>

    @GET("teachers")
    suspend fun getTeachersAppliedClassroom(@Query("classroomId")id: Long): Response<List<Teacher>>

    @GET("academy/classrooms/")
    suspend fun getClassroomsByTeacherId(@Query("teacherId")id:Long): Response<List<Classroom>>

    @GET("academy/classroom/lessons")
    suspend fun getLessonsByTeacherId(@Query("teacherId")id:Long): Response<List<Lesson>>


    @GET("academies/")
    suspend fun getAcademiesByName(@Query("nameLike")name:String): Response<List<Academy>>

    @GET("academies/")
    fun getOwnedAcademy(@Query("ownerId")id:Long): Call<List<Academy>>

    @GET("academy/classroom/lessons")
    suspend fun getTeacherLessonsByDate(@Query("teacherId")id: Long, @Query("sinceDate")sinceDate: String,
                                        @Query("untilDate")untilDate:String): Response<List<Lesson>>


    @GET("academies/")
    suspend fun getAcademiesStudentApplied(@Query("studentId")id: Long): Response<List<Academy>>

    @GET("academies/")
    suspend fun getAcademiesTeacherApplied(@Query("teacherId")id: Long): Response<List<Academy>>


    @POST("students/")
    fun createStudent(@Body student: Student):Call<Student>

    @POST("students/{id}/apply-academy")
    fun sendStudentApplyAcademyRequest(@Path("id")studentId: Long, @Body academyId: Long): Call<Unit>

    @POST("students/{id}/apply-classroom")
    fun sendStudentApplyClassroomRequest(@Path("id")studentId: Long, @Body classroomId: Long): Call<Unit>

    @POST("academy/classroom/lessons")
    fun createLesson(@Body lesson: Lesson): Call<Lesson>

    @PATCH("students/{id}")
    fun changeStudentInfo(@Path("id")id: Long, @Body student: Student):Call<Student>

    @GET("students/{id}")
    suspend fun getStudent(@Path("id")id: Long): Response<Student>

    @GET("students/")
    suspend fun getAcademiesByStudentAppliedAcademyId(@Query("appliedAcademyId")studentId: Long): Response<List<Academy>>

    @GET("students")
    suspend fun getStudentsRequestApplyAcademy(@Query("appliedAcademyId")id: Long, @Query("sort")sortBy: String): Response<List<Student>>

    @GET("students")
    suspend fun getStudentsRequestApplyClassroom(@Query("appliedClassroomId")id: Long): Response<List<Student>>

    @GET("students")
    suspend fun getStudentsAppliedClassroom(@Query("classroomId")id: Long): Response<List<Student>>

    @GET("academy/classroom/lessons")
    suspend fun getLessonsByStudentId(@Query("studentId")id: Long): Response<List<Lesson>>

    @GET("academy/classrooms")
    suspend fun getClassroomsByNameAndAcademyId(@Query("academyId")id: Long,@Query("nameLike")name: String): Response<List<Classroom>>

    @GET("academy/classroom/lessons")
    suspend fun getStudentLessonsByDate(@Query("studentId")id: Long, @Query("sinceDate")sinceDate: String,
                                        @Query("untilDate")untilDate:String): Response<List<Lesson>>

    @DELETE("academy/classrooms/{id}")
    fun deleteClassroom(@Path("id")id: Long): Call<Unit>

    @PATCH("academy/classrooms/{id}")
    fun editClassroom(@Path("id")id: Long, @Body classroom: Classroom): Call<Classroom>


    @POST("academies/{id}/accept-student")
    fun acceptStudentApplyAcademy(@Path("id")academyId: Long,@Body studentId: Long): Call<Unit>

    @POST("academies/{id}/accept-teacher")
    fun acceptTeacherApplyAcademy(@Path("id")academyId: Long, @Body teacherId: Long): Call<Unit>

    @POST("academy/classrooms/{id}/accept-student")
    fun acceptStudentApplyClassroom(@Path("id")classroomId: Long, @Body studentId: Long): Call<Unit>

    @POST("academy/classrooms/{id}/accept-teacher")
    fun acceptTeacherApplyClassroom(@Path("id")classroomId: Long, @Body teacherId: Long): Call<Unit>

    @POST("academy/classrooms/{id}/kick-student")
    fun kickStudentFromClassroom(@Path("id")classroomId: Long, @Body studentId: Long): Call<Unit>

    @POST("academy/classrooms/{id}/kick-teacher")
    fun kickTeacherFromClassroom(@Path("id")classroomId: Long, @Body teacherId: Long): Call<Unit>
}