package com.ich.pullgo.data.remote

import com.ich.pullgo.data.remote.dto.UserDto
import com.ich.pullgo.data.remote.dto.*
import com.ich.pullgo.domain.model.*
import retrofit2.http.*

interface PullgoApi {

    @POST("auth/token/")
    suspend fun getToken(@Body account: Account): UserDto

    @GET("auth/me/")
    suspend fun authorizeUser(): UserDto


    @POST("teachers/")
    suspend fun createTeacher(@Body teacher: Teacher): TeacherDto

    @POST("teachers/{id}/apply-classroom/")
    suspend fun sendTeacherApplyClassroomRequest(@Path("id")teacherId: Long, @Body classroomId: Long)

    @POST("teachers/{id}/apply-academy/")
    suspend fun sendTeacherApplyAcademyRequest(@Path("id")teacherId: Long, @Body academyId: Long)

    @PATCH("teachers/{id}/")
    suspend fun changeTeacherInfo(@Path("id")teacherId:Long, @Body teacher: Teacher): TeacherDto

    @POST("teachers/{id}/remove-applied-academy/")
    suspend fun removeTeacherAcademyRequest(@Path("id")teacherId: Long, @Body academyId: Long)

    @POST("teachers/{id}/remove-applied-classroom/")
    suspend fun removeTeacherClassroomRequest(@Path("id")teacherId: Long, @Body classroomId: Long)

    @GET("teachers/{id}")
    suspend fun getOneTeacher(@Path("id")teacherId: Long): TeacherDto

    @GET("teachers/{username}/exists")
    suspend fun teacherUsernameExists(@Path("username")username: String): ExistDto

    @GET("teachers/")
    suspend fun getAcademiesByTeacherAppliedAcademyId(@Query("appliedAcademyId")academyId:Long): List<AcademyDto>

    @GET("teachers/")
    suspend fun getTeachersRequestApplyAcademy(@Query("appliedAcademyId")academyId: Long): List<TeacherDto>

    @GET("teachers/")
    suspend fun getTeachersRequestApplyClassroom(@Query("appliedClassroomId")academyId: Long): List<TeacherDto>

    @GET("teachers/")
    suspend fun getTeachersAppliedClassroom(@Query("classroomId")classroomId: Long): List<TeacherDto>

    @GET("teachers/")
    suspend fun getTeachersSuchAcademy(@Query("academyId")academyId: Long): List<TeacherDto>

    @GET("academy/classrooms/")
    suspend fun getClassroomsByTeacherId(@Query("teacherId")teacherId:Long): List<ClassroomDto>

    @GET("academy/classroom/lessons/")
    suspend fun getLessonsByTeacherId(@Query("teacherId")teacherId:Long): List<LessonDto>


    @GET("academies/{id}")
    suspend fun getOneAcademy(@Path("id")academyId: Long): AcademyDto

    @GET("academies/")
    suspend fun getAcademiesByName(@Query("nameLike")name: String): List<AcademyDto>

    @GET("academies/")
    suspend fun getOwnedAcademy(@Query("ownerId")teacherId: Long): List<AcademyDto>

    @GET("academies/")
    suspend fun getOwnedAcademyByResponse(@Query("ownerId")teacherId: Long): List<AcademyDto>

    @GET("academy/classroom/lessons/")
    suspend fun getTeacherLessonsByDate(@Query("teacherId")teacherId: Long, @Query("sinceDate")sinceDate: String,
                                        @Query("untilDate")untilDate:String, @Query("size")size: Int): List<LessonDto>
    @GET("academies/")
    suspend fun getAcademiesTeacherApplying(@Query("applyingTeacherId")teacherId: Long): List<AcademyDto>

    @GET("academy/classrooms/")
    suspend fun getClassroomsTeacherApplying(@Query("applyingTeacherId")teacherId: Long): List<ClassroomDto>

    @POST("teachers/{id}/remove-applied-academy/")
    suspend fun removeTeacherAppliedAcademy(@Path("id")teacherId: Long, @Body academyId: Long)

    @POST("teachers/{id}/remove-applied-classroom/")
    suspend fun removeTeacherAppliedClassroom(@Path("id")teacherId: Long, @Body classroomId: Long)


    @GET("academies/")
    suspend fun getAcademiesStudentApplied(@Query("studentId")studentId: Long): List<AcademyDto>

    @GET("academies/")
    suspend fun getAcademiesTeacherApplied(@Query("teacherId")teacherId: Long): List<AcademyDto>

    @POST("academies/{id}/kick-student/")
    suspend fun kickStudent(@Path("id")academyId: Long, @Body studentId: Long)

    @POST("academies/{id}/kick-teacher/")
    suspend fun kickTeacher(@Path("id")academyId: Long, @Body teacherId: Long)

    @POST("academies/")
    suspend fun createAcademy(@Body academy: Academy): AcademyDto

    @PATCH("academies/{id}/")
    suspend fun editAcademy(@Path("id")academyId: Long, @Body academy: Academy): AcademyDto

    @DELETE("academies/{id}/")
    suspend fun deleteAcademy(@Path("id")academyId: Long)


    @POST("students/")
    suspend fun createStudent(@Body student: Student): StudentDto

    @POST("students/{id}/apply-academy/")
    suspend fun sendStudentApplyAcademyRequest(@Path("id")studentId: Long, @Body academyId: Long)

    @POST("students/{id}/apply-classroom/")
    suspend fun sendStudentApplyClassroomRequest(@Path("id")studentId: Long, @Body classroomId: Long)

    @POST("academy/classroom/lessons/")
    suspend fun createLesson(@Body lesson: Lesson): LessonDto

    @PATCH("students/{id}/")
    suspend fun changeStudentInfo(@Path("id")studentId: Long, @Body student: Student): StudentDto

    @POST("students/{id}/remove-applied-academy/")
    suspend fun removeStudentAcademyRequest(@Path("id")studentId: Long, @Body academyId: Long)

    @POST("students/{id}/remove-applied-classroom/")
    suspend fun removeStudentClassroomRequest(@Path("id")studentId: Long, @Body classroomId: Long)


    @GET("students/{id}")
    suspend fun getOneStudent(@Path("id")studentId: Long): StudentDto

    @GET("students/{username}/exists")
    suspend fun studentUsernameExists(@Path("username")username: String): ExistDto

    @GET("students/")
    suspend fun getAcademiesByStudentAppliedAcademyId(@Query("appliedAcademyId")academyId: Long): List<AcademyDto>

    @GET("students/")
    suspend fun getStudentsRequestApplyAcademy(@Query("appliedAcademyId")academyId: Long, @Query("sort")sortBy: String): List<StudentDto>

    @GET("students/")
    suspend fun getStudentsRequestApplyClassroom(@Query("appliedClassroomId")classroomId: Long): List<StudentDto>

    @GET("students/")
    suspend fun getStudentsAppliedClassroom(@Query("classroomId")classroomId: Long): List<StudentDto>

    @GET("students/")
    suspend fun getStudentsSuchAcademy(@Query("academyId")academyId: Long): List<StudentDto>

    @GET("academy/classroom/lessons/")
    suspend fun getLessonsByStudentId(@Query("studentId")studentId: Long): List<LessonDto>

    @GET("academy/classrooms/")
    suspend fun getClassroomsByNameAndAcademyId(@Query("academyId")academyId: Long,@Query("nameLike")name: String): List<ClassroomDto>

    @GET("academy/classroom/lessons/")
    suspend fun getStudentLessonsByDate(@Query("studentId")studentId: Long, @Query("sinceDate")sinceDate: String,
                                        @Query("untilDate")untilDate:String, @Query("size")size: Int): List<LessonDto>

    @DELETE("academy/classrooms/{id}/")
    suspend fun deleteClassroom(@Path("id")classroomId: Long)

    @PATCH("academy/classrooms/{id}/")
    suspend fun editClassroom(@Path("id")classroomId: Long, @Body classroom: Classroom): ClassroomDto

    @GET("academies/")
    suspend fun getAcademiesStudentApplying(@Query("applyingStudentId")studentId: Long): List<AcademyDto>

    @GET("academy/classrooms/")
    suspend fun getClassroomsStudentApplying(@Query("applyingStudentId")studentId: Long): List<ClassroomDto>

    @POST("students/{id}/remove-applied-academy/")
    suspend fun removeStudentAppliedAcademy(@Path("id")studentId: Long, @Body academyId: Long)

    @POST("students/{id}/remove-applied-classroom/")
    suspend fun removeStudentAppliedClassroom(@Path("id")studentId: Long, @Body classroomId: Long)


    @POST("academies/{id}/accept-student/")
    suspend fun acceptStudentApplyAcademy(@Path("id")academyId: Long,@Body studentId: Long)

    @POST("academies/{id}/accept-teacher/")
    suspend fun acceptTeacherApplyAcademy(@Path("id")academyId: Long, @Body teacherId: Long)

    @POST("academy/classrooms/{id}/accept-student/")
    suspend fun acceptStudentApplyClassroom(@Path("id")classroomId: Long, @Body studentId: Long)

    @POST("academy/classrooms/{id}/accept-teacher/")
    suspend fun acceptTeacherApplyClassroom(@Path("id")classroomId: Long, @Body teacherId: Long)

    @POST("academy/classrooms/{id}/kick-student/")
    suspend fun kickStudentFromClassroom(@Path("id")classroomId: Long, @Body studentId: Long)

    @POST("academy/classrooms/{id}/kick-teacher/")
    suspend fun kickTeacherFromClassroom(@Path("id")classroomId: Long, @Body teacherId: Long)


    @GET("academy/classrooms/{id}/")
    suspend fun getClassroomById(@Path("id")classroomId: Long): ClassroomDto

    @GET("academies/{id}/")
    suspend fun getAcademyById(@Path("id")academyId: Long): AcademyDto

    @POST("academy/classrooms/")
    suspend fun createClassroom(@Body newClassroom: Classroom): ClassroomDto


    @PATCH("academy/classroom/lessons/{id}/")
    suspend fun patchLessonInfo(@Path("id")lessonId: Long, @Body lesson: Lesson): LessonDto

    @DELETE("academy/classroom/lessons/{id}/")
    suspend fun deleteLesson(@Path("id")lessonId: Long)


    @GET("exams/{id}")
    suspend fun getOneExam(@Path("id")examId: Long): ExamDto

    @GET("exams/")
    suspend fun getSortedStudentExams(@Query("studentId")studentId: Long, @Query("sort")sort: String, @Query("size")size: Int): List<ExamDto>

    @GET("exams/")
    suspend fun getStudentExamsDesc(@Query("id,desc")studentId: Long, @Query("sort")sort: String, @Query("size")size: Int): List<ExamDto>

    @GET("exams/")
    suspend fun getClassroomExams(@Query("classroomId")classroomId: Long, @Query("size")size: Int): List<ExamDto>

    @GET("exams/")
    suspend fun getClassroomFinishedExams(@Query("classroomId")classroomId: Long,
                                          @Query("finished")fin: String, @Query("size")size: Int): List<ExamDto>

    @GET("exams/")
    suspend fun getClassroomCancelledExams(@Query("classroomId")classroomId: Long,
                                           @Query("cancelled")can: String, @Query("size")size: Int): List<ExamDto>


    @POST("exams/")
    suspend fun createExam(@Body exam: Exam)

    @DELETE("exams/{id}")
    suspend fun removeExam(@Path("id")examId: Long)

    @PATCH("exams/{id}")
    suspend fun editExam(@Path("id")examId: Long, @Body exam: Exam): ExamDto

    @POST("exams/{id}/cancel")
    suspend fun cancelExam(@Path("id")examId: Long)

    @POST("exams/{id}/finish")
    suspend fun finishExam(@Path("id")examId: Long)


    @GET("exam/questions/{id}")
    suspend fun getOneQuestion(@Path("id")questionId: Long): QuestionDto

    @GET("exam/questions")
    suspend fun getQuestionsSuchExam(@Query("examId")examId: Long, @Query("size")size: Int): List<QuestionDto>

    @POST("exam/questions")
    suspend fun createQuestion(@Body question: Question): QuestionDto

    @PATCH("exam/questions/{id}")
    suspend fun editQuestion(@Path("id")questionId: Long, @Body question: Question): QuestionDto

    @DELETE("exam/questions/{id}")
    suspend fun deleteQuestion(@Path("id")questionId: Long)


    @GET("exam/attender-states/{id}")
    suspend fun getOneAttenderState(@Path("id")attenderStateId: Long): AttenderStateDto

    @GET("exam/attender-states")
    suspend fun getStudentAttenderStates(@Query("studentId")studentId: Long, @Query("size")size: Int): List<AttenderStateDto>

    @GET("exam/attender-states")
    suspend fun getExamAttenderStates(@Query("examId")examId: Long, @Query("size")size: Int): List<AttenderStateDto>

    @POST("exam/attender-states")
    suspend fun createAttenderState(@Body state: CreateAttender): AttenderStateDto

    @POST("exam/attender-states/{id}/submit")
    suspend fun submitAttenderState(@Path("id")stateId: Long)


    @PUT("exam/attender-state/{attenderStateId}/answers/{questionId}")
    suspend fun saveAttenderAnswer(@Path("attenderStateId")attenderStateId: Long, @Path("questionId")questionId: Long
                                   ,@Body answer: Answer): AttenderAnswerDto

    @GET("exam/attender-state/answers")
    suspend fun getAttenderAnswers(@Query("attenderStateId")attenderStateId: Long, @Query("size")size: Int, @Query("sort")param: String?): List<AttenderAnswerDto>
}