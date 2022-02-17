package com.ich.pullgo.domain.use_case.manage_classroom.manage_people

data class ManageClassroomManagePeopleUseCases(
    val getStudents: GetStudentsInClassroomUseCase,
    val getTeachers: GetTeachersInClassroomUseCase,
    val kickStudent: KickStudentInClassroomUseCase,
    val kickTeacher: KickTeacherInClassroomUseCase
)
