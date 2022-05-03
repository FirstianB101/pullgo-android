package com.ich.pullgo.presentation.main.teacher_main.manage_classroom.manage_classroom_details.manage_exam

import com.ich.pullgo.domain.model.Exam

sealed class ManageClassroomManageExamEvent{
    data class SelectExam(val exam: Exam): ManageClassroomManageExamEvent()
    object GetAllExams: ManageClassroomManageExamEvent()
    object GetFinishedExams: ManageClassroomManageExamEvent()
    object GetCancelledExams: ManageClassroomManageExamEvent()
    data class CreateExam(val newExam: Exam): ManageClassroomManageExamEvent()
    data class EditExam(val examId: Long, val exam: Exam): ManageClassroomManageExamEvent()
    data class GetStudentStateMap(val examId: Long): ManageClassroomManageExamEvent()
    object DeleteExam: ManageClassroomManageExamEvent()
    object FinishExam: ManageClassroomManageExamEvent()
    object CancelExam: ManageClassroomManageExamEvent()
}
