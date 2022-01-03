package com.ich.pullgo.domain.repository

import com.ich.pullgo.domain.model.Academy
import com.ich.pullgo.domain.model.Classroom

interface ApplyClassroomRepository {
    suspend fun getAcademiesStudentApplied(id: Long): List<Academy>
    suspend fun getAcademiesTeacherApplied(id: Long): List<Academy>
    suspend fun getClassroomsByNameAndAcademyID(id: Long, name: String): List<Classroom>
    suspend fun studentApplyClassroom(studentId: Long, classroomId: Long)
    suspend fun teacherApplyClassroom(teacherId: Long, classroomId: Long)
}