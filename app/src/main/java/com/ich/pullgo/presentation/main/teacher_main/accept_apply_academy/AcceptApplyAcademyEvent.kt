package com.ich.pullgo.presentation.main.teacher_main.accept_apply_academy

import com.ich.pullgo.domain.model.Academy
import com.ich.pullgo.domain.model.Student
import com.ich.pullgo.domain.model.Teacher

sealed class AcceptApplyAcademyEvent {
    data class SelectAcademy(val academy: Academy): AcceptApplyAcademyEvent()
    data class SelectStudent(val student: Student): AcceptApplyAcademyEvent()
    data class SelectTeacher(val teacher: Teacher): AcceptApplyAcademyEvent()
    object GetStudentRequests: AcceptApplyAcademyEvent()
    object GetTeacherRequests: AcceptApplyAcademyEvent()
    object AcceptStudentRequest: AcceptApplyAcademyEvent()
    object AcceptTeacherRequest: AcceptApplyAcademyEvent()
    object DenyStudentRequest: AcceptApplyAcademyEvent()
    object DenyTeacherRequest: AcceptApplyAcademyEvent()
}