package com.ich.pullgo.domain.repository

import com.ich.pullgo.domain.model.Academy
import com.ich.pullgo.domain.model.Student
import com.ich.pullgo.domain.model.Teacher

interface AcceptApplyAcademyRepository{
    suspend fun getStudentsAppliedAcademy(id: Long): List<Student>
    suspend fun getTeachersAppliedAcademy(id: Long): List<Teacher>
    suspend fun getAcademiesOfTeacher(id: Long): List<Academy>

    suspend fun acceptStudentApply(academyId: Long, studentId: Long)
    suspend fun acceptTeacherApply(academyId: Long, teacherId: Long)
    suspend fun denyStudentApply(studentId: Long, academyId: Long)
    suspend fun denyTeacherApply(teacherId: Long, academyId: Long)
}