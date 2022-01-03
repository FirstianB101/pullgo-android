package com.ich.pullgo.domain.repository

import com.ich.pullgo.domain.model.AttenderState
import com.ich.pullgo.domain.model.Student

interface ExamStatusRepository {
    suspend fun getAttenderStatesInExam(examId: Long): List<AttenderState>
    suspend fun getOneStudent(studentId: Long): Student

    suspend fun getStudentsInClassroom(classroomId: Long): List<Student>
}