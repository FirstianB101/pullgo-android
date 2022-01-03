package com.ich.pullgo.domain.repository

import com.ich.pullgo.domain.model.Academy
import com.ich.pullgo.domain.model.Classroom

interface ManageRequestRepository {
    suspend fun getAcademiesTeacherApplying(teacherId: Long): List<Academy>
    suspend fun getClassroomsTeacherApplying(teacherId: Long): List<Classroom>
    suspend fun getAcademiesStudentApplying(studentId: Long): List<Academy>
    suspend fun getClassroomsStudentApplying(studentId: Long): List<Classroom>

    suspend fun removeTeacherAppliedAcademy(teacherId: Long, academyId: Long)
    suspend fun removeTeacherAppliedClassroom(teacherId: Long, classroomId: Long)
    suspend fun removeStudentAppliedAcademy(studentId: Long, academyId: Long)
    suspend fun removeStudentAppliedClassroom(studentId: Long, classroomId: Long)
}