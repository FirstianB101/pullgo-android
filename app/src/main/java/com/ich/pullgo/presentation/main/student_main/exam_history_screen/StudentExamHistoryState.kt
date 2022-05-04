package com.ich.pullgo.presentation.main.student_main.exam_history_screen

import com.ich.pullgo.domain.model.AttenderState
import com.ich.pullgo.domain.model.Exam

data class StudentExamHistoryState(
    val isLoading: Boolean = false,
    val allExams: List<Exam> = emptyList(),
    val takenExams: List<Exam> = emptyList(),
    val selectedExam: Exam? = null,
    val attenderStates: List<AttenderState> = emptyList()
)