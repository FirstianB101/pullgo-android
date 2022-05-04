package com.ich.pullgo.presentation.main.student_main.exam_history_screen

import com.ich.pullgo.domain.model.Exam

sealed class StudentExamHistoryEvent{
    data class SelectExam(val exam: Exam): StudentExamHistoryEvent()
    object GetExamsByName: StudentExamHistoryEvent()
    object GetExamsByBeginDate: StudentExamHistoryEvent()
    object GetExamsByEndDate: StudentExamHistoryEvent()
    object StartReviewing: StudentExamHistoryEvent()
}
