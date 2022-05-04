package com.ich.pullgo.presentation.main.student_main.exam_list_screen

import com.ich.pullgo.domain.model.AttenderState
import com.ich.pullgo.domain.model.Exam

data class StudentExamListState(
    val isLoading: Boolean = false,
    val allExams: List<Exam> = emptyList(),
    val filteredExams: List<Exam> = emptyList(),
    val selectedExam: Exam? = null,
    val attenderStates: List<AttenderState> = emptyList(),
    val examState: AttenderState? = null
)