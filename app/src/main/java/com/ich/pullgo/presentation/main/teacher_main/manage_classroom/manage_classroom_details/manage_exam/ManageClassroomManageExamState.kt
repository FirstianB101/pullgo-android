package com.ich.pullgo.presentation.main.teacher_main.manage_classroom.manage_classroom_details.manage_exam

import com.ich.pullgo.domain.model.AttenderState
import com.ich.pullgo.domain.model.Exam
import com.ich.pullgo.domain.model.Student

sealed class ManageClassroomManageExamState {
    object Loading: ManageClassroomManageExamState()
    object Normal: ManageClassroomManageExamState()
    data class Error(val message: String): ManageClassroomManageExamState()
    data class GetExams(val exams: List<Exam>): ManageClassroomManageExamState()
    data class CreateExam(val exam: Exam): ManageClassroomManageExamState()
    data class EditExam(val exam: Exam): ManageClassroomManageExamState()
    data class GetOneStudent(val student: Student): ManageClassroomManageExamState()
    data class GetOneExam(val exam: Exam): ManageClassroomManageExamState()
    data class GetAttenderStates(val attenderStates: List<AttenderState>): ManageClassroomManageExamState()
    data class GetStudentsInClassroom(val students: List<Student>): ManageClassroomManageExamState()
    object DeleteExam: ManageClassroomManageExamState()
    object FinishExam: ManageClassroomManageExamState()
    object CancelExam: ManageClassroomManageExamState()
}