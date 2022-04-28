package com.ich.pullgo.presentation.main.teacher_main.accept_apply_academy

import com.ich.pullgo.domain.model.Academy
import com.ich.pullgo.domain.model.Student
import com.ich.pullgo.domain.model.Teacher

data class AcceptApplyAcademyState(
    val isLoading: Boolean = false,
    val appliedAcademies: List<Academy> = emptyList(),
    val selectedAcademy: Academy? = null,
    val studentRequests: List<Student> = emptyList(),
    val selectedStudent: Student? = null,
    val teacherRequests: List<Teacher> = emptyList(),
    val selectedTeacher: Teacher? = null
)