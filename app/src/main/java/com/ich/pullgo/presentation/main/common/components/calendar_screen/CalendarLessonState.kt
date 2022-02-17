package com.ich.pullgo.presentation.main.common.components.calendar_screen

import com.ich.pullgo.domain.model.Academy
import com.ich.pullgo.domain.model.Classroom
import com.ich.pullgo.domain.model.Lesson

sealed class CalendarLessonState {
    object Loading : CalendarLessonState()
    object Normal : CalendarLessonState()
    data class Error(val message: String) : CalendarLessonState()
    data class GetAcademy(val academy: Academy) : CalendarLessonState()
    data class GetClassroom(val classroom: Classroom) : CalendarLessonState()
    data class LessonsOnMonth(val lessons: List<Lesson>) : CalendarLessonState()
    data class LessonsOnDate(val lessons: List<Lesson>) : CalendarLessonState()
    data class PatchLesson(val lesson: Lesson) : CalendarLessonState()
    data class CreateLesson(val lesson: Lesson) : CalendarLessonState()
    data class AppliedClassrooms(val classrooms: List<Classroom>): CalendarLessonState()
    object DeleteLesson : CalendarLessonState()
}