package com.ich.pullgo.presentation.main.common.components.calendar_screen

import com.ich.pullgo.domain.model.Academy
import com.ich.pullgo.domain.model.Classroom
import com.ich.pullgo.domain.model.Lesson
import com.prolificinteractive.materialcalendarview.CalendarDay

data class CalendarLessonState(
    val isLoading: Boolean = false,
    val lessonsOnMonth: List<Lesson> = emptyList(),
    val lessonsOnDate: List<Lesson> = emptyList(),
    val selectedDate: CalendarDay? = null,
    val selectedLesson: Lesson? = null,
    val appliedClassrooms: List<Classroom> = emptyList(),
    val academyInfo: Academy? = null,
    val classroomInfo: Classroom? = null
)