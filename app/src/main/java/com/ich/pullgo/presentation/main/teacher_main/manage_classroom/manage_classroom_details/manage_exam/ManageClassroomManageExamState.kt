package com.ich.pullgo.presentation.main.teacher_main.manage_classroom.manage_classroom_details.manage_exam

import com.ich.pullgo.domain.model.Academy
import com.ich.pullgo.domain.model.AttenderState
import com.ich.pullgo.domain.model.Exam
import com.ich.pullgo.domain.model.Student

data class ManageClassroomManageExamState(
    val isLoading: Boolean = false,
    val exams: List<Exam> = emptyList(),
    val selectedExam: Exam? = null,
    val attenderStates: List<AttenderState> = emptyList(),
    val studentsInClassroom: List<Student> = emptyList(),
    val studentStateMap: Map<Long,AttenderState> = mapOf(),
    val academyInfo: Academy? = null,
    val examInfo: Exam? = null,
    val studentInfo: Student? = null,
)