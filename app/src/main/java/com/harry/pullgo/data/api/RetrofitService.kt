package com.harry.pullgo.data.api

import com.harry.pullgo.data.models.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface RetrofitService {

    @POST("auth/token/")
    suspend fun getToken(@Body account: Account): Response<User>

    @GET("auth/me/")
    suspend fun authorizeUser(): Response<User>


    @POST("teachers/")
    fun createTeacher(@Body teacher: Teacher):Call<Teacher>

    @POST("teachers/{id}/apply-classroom/")
    fun sendTeacherApplyClassroomRequest(@Path("id")teacherId: Long, @Body classroomId: Long): Call<Unit>

    @POST("teachers/{id}/apply-academy/")
    fun sendTeacherApplyAcademyRequest(@Path("id")teacherId: Long, @Body academyId: Long): Call<Unit>

    @PATCH("teachers/{id}/")
    fun changeTeacherInfo(@Path("id")teacherId:Long, @Body teacher: Teacher):Call<Teacher>

    @POST("teachers/{id}/remove-applied-academy/")
    fun removeTeacherAcademyRequest(@Path("id")teacherId: Long, @Body academyId: Long): Call<Unit>

    @POST("teachers/{id}/remove-applied-classroom/")
    fun removeTeacherClassroomRequest(@Path("id")teacherId: Long, @Body classroomId: Long): Call<Unit>

    @GET("teachers/")
    suspend fun getAcademiesByTeacherAppliedAcademyId(@Query("appliedAcademyId")academyId:Long): Response<List<Academy>>

    @GET("teachers/")
    suspend fun getTeachersRequestApplyAcademy(@Query("appliedAcademyId")academyId: Long): Response<List<Teacher>>

    @GET("teachers/")
    suspend fun getTeachersRequestApplyClassroom(@Query("appliedClassroomId")academyId: Long): Response<List<Teacher>>

    @GET("teachers/")
    suspend fun getTeachersAppliedClassroom(@Query("classroomId")classroomId: Long): Response<List<Teacher>>

    @GET("teachers/")
    suspend fun getTeachersSuchAcademy(@Query("academyId")academyId: Long): Response<List<Teacher>>

    @GET("academy/classrooms/")
    suspend fun getClassroomsByTeacherId(@Query("teacherId")teacherId:Long): Response<List<Classroom>>

    @GET("academy/classroom/lessons/")
    suspend fun getLessonsByTeacherId(@Query("teacherId")teacherId:Long): Response<List<Lesson>>


    @GET("academies/")
    suspend fun getAcademiesByName(@Query("nameLike")name: String): Response<List<Academy>>

    @GET("academies/")
    suspend fun getOwnedAcademy(@Query("ownerId")teacherId: Long): Response<List<Academy>>

    @GET("academies/")
    fun getOwnedAcademyByCall(@Query("ownerId")teacherId: Long): Call<List<Academy>>

    @GET("academy/classroom/lessons/")
    suspend fun getTeacherLessonsByDate(@Query("teacherId")teacherId: Long, @Query("sinceDate")sinceDate: String,
                                        @Query("untilDate")untilDate:String): Response<List<Lesson>>


    @GET("academies/")
    suspend fun getAcademiesStudentApplied(@Query("studentId")studentId: Long): Response<List<Academy>>

    @GET("academies/")
    suspend fun getAcademiesTeacherApplied(@Query("teacherId")teacherId: Long): Response<List<Academy>>

    @POST("academies/{id}/kick-student/")
    fun kickStudent(@Path("id")academyId: Long, @Body studentId: Long): Call<Unit>

    @POST("academies/{id}/kick-teacher/")
    fun kickTeacher(@Path("id")academyId: Long, @Body teacherId: Long): Call<Unit>

    @POST("academies/")
    fun createAcademy(@Body academy: Academy): Call<Academy>

    @PATCH("academies/{id}/")
    fun editAcademy(@Path("id")academyId: Long, @Body academy: Academy): Call<Academy>

    @DELETE("academies/{id}/")
    fun deleteAcademy(@Path("id")academyId: Long): Call<Unit>


    @POST("students/")
    fun createStudent(@Body student: Student): Call<Student>

    @POST("students/{id}/apply-academy/")
    fun sendStudentApplyAcademyRequest(@Path("id")studentId: Long, @Body academyId: Long): Call<Unit>

    @POST("students/{id}/apply-classroom/")
    fun sendStudentApplyClassroomRequest(@Path("id")studentId: Long, @Body classroomId: Long): Call<Unit>

    @POST("academy/classroom/lessons/")
    fun createLesson(@Body lesson: Lesson): Call<Lesson>

    @PATCH("students/{id}/")
    fun changeStudentInfo(@Path("id")studentId: Long, @Body student: Student):Call<Student>

    @POST("students/{id}/remove-applied-academy/")
    fun removeStudentAcademyRequest(@Path("id")studentId: Long, @Body academyId: Long): Call<Unit>

    @POST("students/{id}/remove-applied-classroom/")
    fun removeStudentClassroomRequest(@Path("id")studentId: Long, @Body classroomId: Long): Call<Unit>


    @GET("students/")
    suspend fun getAcademiesByStudentAppliedAcademyId(@Query("appliedAcademyId")academyId: Long): Response<List<Academy>>

    @GET("students/")
    suspend fun getStudentsRequestApplyAcademy(@Query("appliedAcademyId")academyId: Long, @Query("sort")sortBy: String): Response<List<Student>>

    @GET("students/")
    suspend fun getStudentsRequestApplyClassroom(@Query("appliedClassroomId")classroomId: Long): Response<List<Student>>

    @GET("students/")
    suspend fun getStudentsAppliedClassroom(@Query("classroomId")classroomId: Long): Response<List<Student>>

    @GET("students/")
    suspend fun getStudentsSuchAcademy(@Query("academyId")academyId: Long): Response<List<Student>>

    @GET("academy/classroom/lessons/")
    suspend fun getLessonsByStudentId(@Query("studentId")studentId: Long): Response<List<Lesson>>

    @GET("academy/classrooms/")
    suspend fun getClassroomsByNameAndAcademyId(@Query("academyId")academyId: Long,@Query("nameLike")name: String): Response<List<Classroom>>

    @GET("academy/classroom/lessons/")
    suspend fun getStudentLessonsByDate(@Query("studentId")studentId: Long, @Query("sinceDate")sinceDate: String,
                                        @Query("untilDate")untilDate:String): Response<List<Lesson>>

    @DELETE("academy/classrooms/{id}/")
    fun deleteClassroom(@Path("id")classroomId: Long): Call<Unit>

    @PATCH("academy/classrooms/{id}/")
    fun editClassroom(@Path("id")classroomId: Long, @Body classroom: Classroom): Call<Classroom>


    @POST("academies/{id}/accept-student/")
    fun acceptStudentApplyAcademy(@Path("id")academyId: Long,@Body studentId: Long): Call<Unit>

    @POST("academies/{id}/accept-teacher/")
    fun acceptTeacherApplyAcademy(@Path("id")academyId: Long, @Body teacherId: Long): Call<Unit>

    @POST("academy/classrooms/{id}/accept-student/")
    fun acceptStudentApplyClassroom(@Path("id")classroomId: Long, @Body studentId: Long): Call<Unit>

    @POST("academy/classrooms/{id}/accept-teacher/")
    fun acceptTeacherApplyClassroom(@Path("id")classroomId: Long, @Body teacherId: Long): Call<Unit>

    @POST("academy/classrooms/{id}/kick-student/")
    fun kickStudentFromClassroom(@Path("id")classroomId: Long, @Body studentId: Long): Call<Unit>

    @POST("academy/classrooms/{id}/kick-teacher/")
    fun kickTeacherFromClassroom(@Path("id")classroomId: Long, @Body teacherId: Long): Call<Unit>


    @GET("academy/classrooms/{id}/")
    suspend fun getClassroomById(@Path("id")classroomId: Long): Response<Classroom>

    @GET("academies/{id}/")
    suspend fun getAcademyById(@Path("id")academyId: Long): Response<Academy>

    @POST("academy/classrooms/")
    fun createClassroom(@Body newClassroom: Classroom): Call<Classroom>


    @PATCH("academy/classroom/lessons/{id}/")
    fun patchLessonInfo(@Path("id")lessonId: Long, @Body lesson: Lesson): Call<Lesson>

    @DELETE("academy/classroom/lessons/{id}/")
    fun deleteLesson(@Path("id")lessonId: Long): Call<Unit>


    @GET("exams/")
    suspend fun getSortedStudentExams(@Query("studentId")studentId: Long, @Query("sort")sort: String): Response<List<Exam>>

    @GET("exams/")
    suspend fun getStudentExamsDesc(@Query("id,desc")studentId: Long, @Query("sort")sort: String): Response<List<Exam>>
}