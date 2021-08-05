package com.harry.pullgo.data.api

import android.view.View
import com.harry.pullgo.data.objects.*

interface OnAcademyClick {
    fun onAcademyClick(view: View,academy: Academy?)
}

interface OnCheckPw {
    fun onPasswordCheck()
}

interface OnLessonClick {
    fun onLessonClick(view: View, lesson: Lesson?)
}

interface OnClassroomClick{
    fun onClassroomClick(view: View, classroom: Classroom?)
}

interface OnStudentClick{
    fun onBackgroundClick(view: View, student: Student?)
    fun onApplyButtonClick(view: View, student: Student?)
    fun onRemoveButtonClick(view: View, student: Student?)
}

interface OnTeacherClick{
    fun onBackgroundClick(view: View, teacher: Teacher?)
    fun onApplyButtonClick(view: View, teacher: Teacher?)
    fun onRemoveButtonClick(view: View, teacher: Teacher?)
}

interface OnCalendarReset{
    fun onResetCalendar()
}

interface OnDataChanged{
    fun onChangeData(isTeacher: Boolean, isChanged: Boolean)
}