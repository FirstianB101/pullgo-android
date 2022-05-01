package com.ich.pullgo.presentation.main.teacher_main.manage_classroom.manage_classroom_details.manage_people

import com.ich.pullgo.domain.model.Student
import com.ich.pullgo.domain.model.Teacher

sealed class ManageClassroomManagePeopleEvent {
    data class GetStudentsInClassroom(val selectedClassroomId: Long): ManageClassroomManagePeopleEvent()
    data class GetTeachersInClassroom(val selectedClassroomId: Long): ManageClassroomManagePeopleEvent()
    data class SelectTeacher(val teacher: Teacher): ManageClassroomManagePeopleEvent()
    data class SelectStudent(val student: Student): ManageClassroomManagePeopleEvent()
    data class KickStudent(val selectedClassroomId: Long): ManageClassroomManagePeopleEvent()
    data class KickTeacher(val selectedClassroomId: Long): ManageClassroomManagePeopleEvent()
}