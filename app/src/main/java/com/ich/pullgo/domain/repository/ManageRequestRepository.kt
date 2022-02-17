package com.ich.pullgo.domain.repository

import com.ich.pullgo.domain.model.Academy
import com.ich.pullgo.domain.model.Classroom
import com.ich.pullgo.domain.model.Teacher
import retrofit2.Response

interface ManageRequestRepository {
    suspend fun getAcademiesTeacherApplying(teacherId: Long): List<Academy>
    suspend fun getClassroomsTeacherApplying(teacherId: Long): List<Classroom>
    suspend fun getAcademiesStudentApplying(studentId: Long): List<Academy>
    suspend fun getClassroomsStudentApplying(studentId: Long): List<Classroom>

    suspend fun removeTeacherApplyingAcademy(teacherId: Long, academyId: Long): Response<Unit>
    suspend fun removeTeacherApplyingClassroom(teacherId: Long, classroomId: Long): Response<Unit>
    suspend fun removeStudentApplyingAcademy(studentId: Long, academyId: Long): Response<Unit>
    suspend fun removeStudentApplyingClassroom(studentId: Long, classroomId: Long): Response<Unit>

    suspend fun getOwnerOfAcademy(ownerId: Long): Teacher
    suspend fun getAcademyOfClassroom(academyId: Long): Academy
}