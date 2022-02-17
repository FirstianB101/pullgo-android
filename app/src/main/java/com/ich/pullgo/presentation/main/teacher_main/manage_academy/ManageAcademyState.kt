package com.ich.pullgo.presentation.main.teacher_main.manage_academy

import com.ich.pullgo.domain.model.Academy
import com.ich.pullgo.domain.model.Student
import com.ich.pullgo.domain.model.Teacher

sealed class ManageAcademyState{
    object Loading: ManageAcademyState()
    object Normal: ManageAcademyState()
    data class Error(val message: String): ManageAcademyState()
    data class GetAcademies(val academies: List<Academy>): ManageAcademyState()
    data class GetStudents(val students: List<Student>): ManageAcademyState()
    data class GetTeachers(val teachers: List<Teacher>): ManageAcademyState()
    data class EditAcademy(val academy: Academy): ManageAcademyState()
    object KickUser: ManageAcademyState()
    object DeleteAcademy: ManageAcademyState()
}