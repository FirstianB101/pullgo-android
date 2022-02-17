package com.ich.pullgo.domain.repository

import com.ich.pullgo.domain.model.*
import retrofit2.Response

interface ManageClassroomRepository {
    suspend fun getClassroomsTeacherApplied(teacherId: Long): List<Classroom>
    suspend fun getAcademiesTeacherApplied(teacherId: Long): List<Academy>
    suspend fun getStudentsAppliedClassroom(classroomId: Long): List<Student>
    suspend fun getTeachersAppliedClassroom(classroomId: Long): List<Teacher>
    suspend fun getStudentsRequestApplyClassroom(classroomId: Long): List<Student>
    suspend fun getTeachersRequestApplyClassroom(classroomId: Long): List<Teacher>
    suspend fun getExamsWithinClassroom(classroomId: Long): List<Exam>
    suspend fun getFinishedExams(classroomId: Long): List<Exam>
    suspend fun getCancelledExams(classroomId: Long): List<Exam>

    suspend fun createClassroom(classroom: Classroom): Classroom
    suspend fun kickStudentFromClassroom(classroomId: Long, studentId: Long): Response<Unit>
    suspend fun kickTeacherFromClassroom(classroomId: Long, teacherId: Long): Response<Unit>
    suspend fun editClassroom(classroomId: Long, classroom: Classroom): Classroom
    suspend fun deleteClassroom(classroomId: Long): Response<Unit>
    suspend fun createExam(exam: Exam): Exam
    suspend fun deleteExam(examId: Long): Response<Unit>
    suspend fun editExam(examId: Long, exam: Exam): Exam
    suspend fun cancelExam(examId: Long): Response<Unit>
    suspend fun finishExam(examId: Long): Response<Unit>


    suspend fun acceptStudent(classroomId: Long, studentId: Long): Response<Unit>
    suspend fun acceptTeacher(classroomId: Long, teacherId: Long): Response<Unit>
    suspend fun denyStudent(studentId: Long, classroomId: Long): Response<Unit>
    suspend fun denyTeacher(teacherId: Long, classroomId: Long): Response<Unit>

    suspend fun getAttenderStatesInExam(examId: Long): List<AttenderState>
    suspend fun getOneStudent(studentId: Long): Student
    suspend fun getOneExam(examId: Long): Exam
}