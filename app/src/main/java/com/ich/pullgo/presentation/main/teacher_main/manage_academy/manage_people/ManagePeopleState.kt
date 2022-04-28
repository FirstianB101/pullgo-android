package com.ich.pullgo.presentation.main.teacher_main.manage_academy.manage_people

import com.ich.pullgo.domain.model.Student
import com.ich.pullgo.domain.model.Teacher

data class ManagePeopleState(
    val isLoading: Boolean = false,
    val selectedStudent: Student? = null,
    val selectedTeacher: Teacher? = null,
    val studentsInAcademy: List<Student> = emptyList(),
    val teachersInAcademy: List<Teacher> = emptyList(),
)