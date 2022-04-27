package com.ich.pullgo.presentation.main.common.components.calendar_screen.util

import com.ich.pullgo.domain.model.Lesson
import com.prolificinteractive.materialcalendarview.CalendarDay

object CalendarUtils {
    fun makeCalendarDayList(lessons: List<Lesson>): List<CalendarDay> {
        val dates = ArrayList<String>()
        val calList = ArrayList<CalendarDay>()

        for (lesson in lessons) {
            dates.add(lesson.schedule?.date!!)
        }

        var tmp: List<Int>
        for (date in dates) {
            //연도-월-일 형식이며 월은 0부터 시작하므로 1 빼줌
            tmp = date.split('-').map { d -> d.toInt() }
            calList.add(CalendarDay.from(tmp[0], tmp[1] - 1, tmp[2]))
        }

        return calList
    }

    fun applyPatchedLesson(lesson: Lesson, patchedLesson: Lesson){
        lesson.name = patchedLesson.name
        lesson.schedule = patchedLesson.schedule
    }

    fun twoColonFormatToOneColon(time: String): String{
        val arr = time.split(':')
        return "${arr[0]}:${arr[1]}"
    }

    fun oneColonFormatToTwoColon(time: String): String{
        return "${time}:00"
    }
}

fun CalendarDay.toDayString(): String{
    return String.format("%04d-%02d-%02d", this.year, this.month + 1, this.day)
}