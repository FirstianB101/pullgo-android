package com.ich.pullgo.domain.use_case.manage_academy

data class ManageAcademyUseCases(
    val getOwnedAcademies: GetOwnedAcademiesUseCase,
    val editAcademy: EditAcademyUseCase,
    val deleteAcademy: DeleteAcademyUseCase,
    val getStudents: GetStudentsInAcademyUseCase,
    val getTeachers: GetTeachersInAcademyUseCase,
    val kickTeacher: KickTeacherUseCase,
    val kickStudent: KickStudentUseCase
)