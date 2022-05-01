package com.ich.pullgo.presentation.main.teacher_main.manage_classroom.utils

import ca.antonious.materialdaypicker.MaterialDayPicker

object WeekdayUtil {
    fun weekdaysToString(weekdays: List<MaterialDayPicker.Weekday>): String{
        val days = StringBuilder()
        if (weekdays.contains(MaterialDayPicker.Weekday.MONDAY)) days.append("월")
        if (weekdays.contains(MaterialDayPicker.Weekday.TUESDAY)) days.append("화")
        if (weekdays.contains(MaterialDayPicker.Weekday.WEDNESDAY)) days.append("수")
        if (weekdays.contains(MaterialDayPicker.Weekday.THURSDAY)) days.append("목")
        if (weekdays.contains(MaterialDayPicker.Weekday.FRIDAY)) days.append("금")
        if (weekdays.contains(MaterialDayPicker.Weekday.SATURDAY)) days.append("토")
        if (weekdays.contains(MaterialDayPicker.Weekday.SUNDAY)) days.append("일")

        return days.toString()
    }

    fun stringToWeekdays(weekdays: String): List<MaterialDayPicker.Weekday>{
        val dayList = mutableListOf<MaterialDayPicker.Weekday>()
        weekdays.forEach { day ->
            when(day){
                '월' -> dayList.add(MaterialDayPicker.Weekday.MONDAY)
                '화' -> dayList.add(MaterialDayPicker.Weekday.TUESDAY)
                '수' -> dayList.add(MaterialDayPicker.Weekday.WEDNESDAY)
                '목' -> dayList.add(MaterialDayPicker.Weekday.THURSDAY)
                '금' -> dayList.add(MaterialDayPicker.Weekday.FRIDAY)
                '토' -> dayList.add(MaterialDayPicker.Weekday.SATURDAY)
                '일' -> dayList.add(MaterialDayPicker.Weekday.SUNDAY)
            }
        }
        return dayList
    }
}