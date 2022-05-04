package com.ich.pullgo.presentation.main.student_main.exam_list_screen

import com.ich.pullgo.domain.model.Exam

sealed class StudentExamListEvent{
    data class SelectExam(val exam: Exam): StudentExamListEvent()
    object GetExamsByName: StudentExamListEvent()
    object GetExamsByBeginDate: StudentExamListEvent()
    object GetExamsByEndDate: StudentExamListEvent()
    object StartTakingExam: StudentExamListEvent()
}
