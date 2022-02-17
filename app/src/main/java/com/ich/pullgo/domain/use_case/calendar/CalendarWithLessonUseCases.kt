package com.ich.pullgo.domain.use_case.calendar

data class CalendarWithLessonUseCases(
    val getAcademySuchLesson: GetAcademySuchLessonUseCase,
    val getClassroomSuchLesson: GetClassroomSuchLessonUseCase,
    val getStudentLessonsOnDate: GetStudentLessonsOnDateUseCase,
    val getStudentLessonsOnMonth: GetStudentLessonsOnMonthUseCase,
    val getTeacherLessonsOnDate: GetTeacherLessonsOnDateUseCase,
    val getTeacherLessonsOnMonth: GetTeacherLessonsOnMonthUseCase,
    val getClassroomsTeacherApplied: GetTeacherClassroomsUseCase,
    val patchLessonInfo: PatchLessonInfoUseCase,
    val deleteLesson: DeleteLessonUseCase,
    val createLesson: CreateLessonUseCase
)