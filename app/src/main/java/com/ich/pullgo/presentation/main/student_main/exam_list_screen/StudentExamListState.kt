package com.ich.pullgo.presentation.main.student_main.exam_list_screen

import com.ich.pullgo.domain.model.AttenderState
import com.ich.pullgo.domain.model.Exam

sealed class StudentExamListState {
    object Loading: StudentExamListState()
    object Normal: StudentExamListState()
    data class Error(val message: String): StudentExamListState()
    data class GetExams(val exams: List<Exam>): StudentExamListState()
    data class GetStates(val states: List<AttenderState>): StudentExamListState()
    data class StartExam(val state: AttenderState): StudentExamListState()
}