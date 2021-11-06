package com.harry.pullgo.data.api

import com.harry.pullgo.data.models.*
import retrofit2.Response
import retrofit2.http.*

interface PullgoService {

    @POST("auth/token/")
    suspend fun getToken(@Body account: Account): Response<User>

    @GET("auth/me/")
    suspend fun authorizeUser(): Response<User>


    @POST("teachers/")
    suspend fun createTeacher(@Body teacher: Teacher): Response<Teacher>

    @POST("teachers/{id}/apply-classroom/")
    suspend fun sendTeacherApplyClassroomRequest(@Path("id")teacherId: Long, @Body classroomId: Long): Response<Unit>

    @POST("teachers/{id}/apply-academy/")
    suspend fun sendTeacherApplyAcademyRequest(@Path("id")teacherId: Long, @Body academyId: Long): Response<Unit>

    @PATCH("teachers/{id}/")
    suspend fun changeTeacherInfo(@Path("id")teacherId:Long, @Body teacher: Teacher): Response<Teacher>

    @POST("teachers/{id}/remove-applied-academy/")
    suspend fun removeTeacherAcademyRequest(@Path("id")teacherId: Long, @Body academyId: Long): Response<Unit>

    @POST("teachers/{id}/remove-applied-classroom/")
    suspend fun removeTeacherClassroomRequest(@Path("id")teacherId: Long, @Body classroomId: Long): Response<Unit>

    @GET("teachers/{username}/exists")
    suspend fun teacherUsernameExists(@Path("username")username: String): Response<Exist>

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
    suspend fun getOwnedAcademyByResponse(@Query("ownerId")teacherId: Long): Response<List<Academy>>

    @GET("academy/classroom/lessons/")
    suspend fun getTeacherLessonsByDate(@Query("teacherId")teacherId: Long, @Query("sinceDate")sinceDate: String,
                                        @Query("untilDate")untilDate:String, @Query("size")size: Int): Response<List<Lesson>>
    @GET("academies/")
    suspend fun getTeacherApplyingAcademies(@Query("applyingTeacherId")teacherId: Long): Response<List<Academy>>

    @GET("academy/classrooms/")
    suspend fun getTeacherApplyingClassrooms(@Query("applyingTeacherId")teacherId: Long): Response<List<Classroom>>

    @POST("teachers/{id}/remove-applied-academy/")
    suspend fun removeTeacherAppliedAcademy(@Path("id")teacherId: Long, @Body academyId: Long): Response<Unit>

    @POST("teachers/{id}/remove-applied-classroom/")
    suspend fun removeTeacherAppliedClassroom(@Path("id")teacherId: Long, @Body classroomId: Long): Response<Unit>


    @GET("academies/")
    suspend fun getAcademiesStudentApplied(@Query("studentId")studentId: Long): Response<List<Academy>>

    @GET("academies/")
    suspend fun getAcademiesTeacherApplied(@Query("teacherId")teacherId: Long): Response<List<Academy>>

    @POST("academies/{id}/kick-student/")
    suspend fun kickStudent(@Path("id")academyId: Long, @Body studentId: Long): Response<Unit>

    @POST("academies/{id}/kick-teacher/")
    suspend fun kickTeacher(@Path("id")academyId: Long, @Body teacherId: Long): Response<Unit>

    @POST("academies/")
    suspend fun createAcademy(@Body academy: Academy): Response<Academy>

    @PATCH("academies/{id}/")
    suspend fun editAcademy(@Path("id")academyId: Long, @Body academy: Academy): Response<Academy>

    @DELETE("academies/{id}/")
    suspend fun deleteAcademy(@Path("id")academyId: Long): Response<Unit>


    @POST("students/")
    suspend fun createStudent(@Body student: Student): Response<Student>

    @POST("students/{id}/apply-academy/")
    suspend fun sendStudentApplyAcademyRequest(@Path("id")studentId: Long, @Body academyId: Long): Response<Unit>

    @POST("students/{id}/apply-classroom/")
    suspend fun sendStudentApplyClassroomRequest(@Path("id")studentId: Long, @Body classroomId: Long): Response<Unit>

    @POST("academy/classroom/lessons/")
    suspend fun createLesson(@Body lesson: Lesson): Response<Lesson>

    @PATCH("students/{id}/")
    suspend fun changeStudentInfo(@Path("id")studentId: Long, @Body student: Student): Response<Student>

    @POST("students/{id}/remove-applied-academy/")
    suspend fun removeStudentAcademyRequest(@Path("id")studentId: Long, @Body academyId: Long): Response<Unit>

    @POST("students/{id}/remove-applied-classroom/")
    suspend fun removeStudentClassroomRequest(@Path("id")studentId: Long, @Body classroomId: Long): Response<Unit>


    @GET("students/{username}/exists")
    suspend fun studentUsernameExists(@Path("username")username: String): Response<Exist>

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
                                        @Query("untilDate")untilDate:String, @Query("size")size: Int): Response<List<Lesson>>

    @DELETE("academy/classrooms/{id}/")
    suspend fun deleteClassroom(@Path("id")classroomId: Long): Response<Unit>

    @PATCH("academy/classrooms/{id}/")
    suspend fun editClassroom(@Path("id")classroomId: Long, @Body classroom: Classroom): Response<Classroom>

    @GET("academies/")
    suspend fun getStudentApplyingAcademies(@Query("applyingStudentId")studentId: Long): Response<List<Academy>>

    @GET("academy/classrooms/")
    suspend fun getStudentApplyingClassrooms(@Query("applyingStudentId")studentId: Long): Response<List<Classroom>>

    @POST("students/{id}/remove-applied-academy/")
    suspend fun removeStudentAppliedAcademy(@Path("id")studentId: Long, @Body academyId: Long): Response<Unit>

    @POST("students/{id}/remove-applied-classroom/")
    suspend fun removeStudentAppliedClassroom(@Path("id")studentId: Long, @Body classroomId: Long): Response<Unit>


    @POST("academies/{id}/accept-student/")
    suspend fun acceptStudentApplyAcademy(@Path("id")academyId: Long,@Body studentId: Long): Response<Unit>

    @POST("academies/{id}/accept-teacher/")
    suspend fun acceptTeacherApplyAcademy(@Path("id")academyId: Long, @Body teacherId: Long): Response<Unit>

    @POST("academy/classrooms/{id}/accept-student/")
    suspend fun acceptStudentApplyClassroom(@Path("id")classroomId: Long, @Body studentId: Long): Response<Unit>

    @POST("academy/classrooms/{id}/accept-teacher/")
    suspend fun acceptTeacherApplyClassroom(@Path("id")classroomId: Long, @Body teacherId: Long): Response<Unit>

    @POST("academy/classrooms/{id}/kick-student/")
    suspend fun kickStudentFromClassroom(@Path("id")classroomId: Long, @Body studentId: Long): Response<Unit>

    @POST("academy/classrooms/{id}/kick-teacher/")
    suspend fun kickTeacherFromClassroom(@Path("id")classroomId: Long, @Body teacherId: Long): Response<Unit>


    @GET("academy/classrooms/{id}/")
    suspend fun getClassroomById(@Path("id")classroomId: Long): Response<Classroom>

    @GET("academies/{id}/")
    suspend fun getAcademyById(@Path("id")academyId: Long): Response<Academy>

    @POST("academy/classrooms/")
    suspend fun createClassroom(@Body newClassroom: Classroom): Response<Classroom>


    @PATCH("academy/classroom/lessons/{id}/")
    suspend fun patchLessonInfo(@Path("id")lessonId: Long, @Body lesson: Lesson): Response<Lesson>

    @DELETE("academy/classroom/lessons/{id}/")
    suspend fun deleteLesson(@Path("id")lessonId: Long): Response<Unit>


    @GET("exams/")
    suspend fun getSortedStudentExams(@Query("studentId")studentId: Long, @Query("sort")sort: String): Response<List<Exam>>

    @GET("exams/")
    suspend fun getStudentExamsDesc(@Query("id,desc")studentId: Long, @Query("sort")sort: String): Response<List<Exam>>

    @GET("exams")
    suspend fun getClassroomExams(@Query("classroomId")classroomId: Long): Response<List<Exam>>


    @POST("exams/")
    suspend fun createExam(@Body exam: Exam): Response<Unit>

    @DELETE("exams/{id}")
    suspend fun removeExam(@Path("id")examId: Long): Response<Unit>

    @POST("exams/{id}/cancel")
    suspend fun cancelExam(@Path("id")examId: Long): Response<Unit>

    @POST("exams/{id}/finish")
    suspend fun finishExam(@Path("id")examId: Long): Response<Unit>


    @GET("exam/questions/{id}")
    suspend fun getOneQuestion(@Path("id")questionId: Long): Response<Question>

    @GET("exam/questions")
    suspend fun getQuestionsSuchExam(@Query("examId")examId: Long): Response<List<Question>>

    @POST("exam/questions")
    suspend fun createQuestion(@Body question: Question): Response<Question>

    @PATCH("exam/questions/{id}")
    suspend fun editQuestion(@Path("id")questionId: Long, @Body question: Question): Response<Question>

    @DELETE("exam/questions/{id}")
    suspend fun deleteQuestion(@Path("id")questionId: Long): Response<Unit>
}