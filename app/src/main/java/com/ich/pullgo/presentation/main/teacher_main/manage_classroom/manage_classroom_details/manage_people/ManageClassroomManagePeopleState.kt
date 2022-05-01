package com.ich.pullgo.presentation.main.teacher_main.manage_classroom.manage_classroom_details.manage_people

import com.ich.pullgo.domain.model.Student
import com.ich.pullgo.domain.model.Teacher

data class ManageClassroomManagePeopleState(
    val isLoading: Boolean = false,
    val studentsInClassroom: List<Student> = emptyList(),
    val selectedStudent: Student? = null,
    val teachersInClassroom: List<Teacher> = emptyList(),
    val selectedTeacher: Teacher? = null
)