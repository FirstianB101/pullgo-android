package com.ich.pullgo.presentation.main.teacher_main.manage_classroom.manage_classroom_details.manage_people

import com.ich.pullgo.domain.model.Student
import com.ich.pullgo.domain.model.Teacher

sealed class ManageClassroomManagePeopleState {
    object Loading: ManageClassroomManagePeopleState()
    object Normal: ManageClassroomManagePeopleState()
    data class Error(val message: String): ManageClassroomManagePeopleState()
    data class GetStudents(val students: List<Student>): ManageClassroomManagePeopleState()
    data class GetTeachers(val teachers: List<Teacher>): ManageClassroomManagePeopleState()
    object KickPeople: ManageClassroomManagePeopleState()
}