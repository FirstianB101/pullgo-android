package com.ich.pullgo.presentation.main.teacher_main.accept_apply_academy

import com.ich.pullgo.domain.model.Academy
import com.ich.pullgo.domain.model.Student
import com.ich.pullgo.domain.model.Teacher

sealed class AcceptApplyAcademyState{
    object Normal: AcceptApplyAcademyState()
    object Loading: AcceptApplyAcademyState()
    data class Error(val message: String): AcceptApplyAcademyState()
    data class GetAcademies(val academies: List<Academy>): AcceptApplyAcademyState()
    data class GetStudentRequests(val students: List<Student>): AcceptApplyAcademyState()
    data class GetTeacherRequests(val teachers: List<Teacher>): AcceptApplyAcademyState()
    object AcceptRequest: AcceptApplyAcademyState()
    object DenyRequest: AcceptApplyAcademyState()
}