package com.ich.pullgo.presentation.main.common.components.calendar_screen

import com.ich.pullgo.domain.model.Lesson
import com.prolificinteractive.materialcalendarview.CalendarDay

sealed class CalendarEvent {
    object GetLessonsOnMonth: CalendarEvent()
    data class PatchLesson(val lessonId: Long, val lesson: Lesson): CalendarEvent()
    data class DeleteLesson(val lessonId: Long): CalendarEvent()
    data class CreateLesson(val lesson: Lesson): CalendarEvent()
    object GetClassroomsTeacherApplied: CalendarEvent()
    data class OnCalendarDaySelected(val day: CalendarDay): CalendarEvent()
    data class OnLessonSelected(val lesson: Lesson): CalendarEvent()
    data class GetLessonAcademyClassroomInfo(val academyId: Long, val classroomId: Long): CalendarEvent()
}