package com.ich.pullgo.presentation.main.teacher_main.manage_academy.manage_people

import com.ich.pullgo.domain.model.Student
import com.ich.pullgo.domain.model.Teacher
import com.ich.pullgo.presentation.main.teacher_main.manage_academy.ManageAcademyEvent

sealed class ManagePeopleEvent{
    data class SelectStudent(val student: Student): ManagePeopleEvent()
    data class SelectTeacher(val teacher: Teacher): ManagePeopleEvent()
    data class GetStudentsInAcademy(val academyId: Long): ManagePeopleEvent()
    data class GetTeachersInAcademy(val academyId: Long): ManagePeopleEvent()
    data class KickStudent(val academyId: Long): ManagePeopleEvent()
    data class KickTeacher(val academyId: Long): ManagePeopleEvent()
}
