package com.ich.pullgo.domain.repository

import com.ich.pullgo.domain.model.Classroom
import com.ich.pullgo.domain.model.Exam
import com.ich.pullgo.domain.model.Student
import com.ich.pullgo.domain.model.Teacher

interface ManageClassroomRepository {
    suspend fun getStudentsAppliedClassroom(classroomId: Long): List<Student>
    suspend fun getTeachersAppliedClassroom(classroomId: Long): List<Teacher>
    suspend fun getStudentsRequestApplyClassroom(classroomId: Long): List<Student>
    suspend fun getTeachersRequestApplyClassroom(classroomId: Long): List<Teacher>
    suspend fun getExamsWithinClassroom(classroomId: Long): List<Exam>
    suspend fun getFinishedExams(classroomId: Long): List<Exam>
    suspend fun getCancelledExams(classroomId: Long): List<Exam>

    suspend fun createClassroom(classroom: Classroom): Classroom
    suspend fun kickStudentFromClassroom(classroomId: Long, studentId: Long)
    suspend fun kickTeacherFromClassroom(classroomId: Long, teacherId: Long)
    suspend fun editClassroom(classroomId: Long, classroom: Classroom): Classroom
    suspend fun deleteClassroom(classroomId: Long)
    suspend fun createExam(exam: Exam)
    suspend fun removeExam(examId: Long)
    suspend fun editExam(examId: Long, exam: Exam): Exam
    suspend fun cancelExam(examId: Long)
    suspend fun finishExam(examId: Long)
    suspend fun getOneExam(examId: Long): Exam

    suspend fun acceptStudent(classroomId: Long, studentId: Long)
    suspend fun acceptTeacher(classroomId: Long, teacherId: Long)
    suspend fun denyStudent(studentId: Long, classroomId: Long)
    suspend fun denyTeacher(teacherId: Long, classroomId: Long)
}