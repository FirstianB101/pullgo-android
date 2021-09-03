package com.harry.pullgo.data.api

import android.view.View
import com.harry.pullgo.data.objects.*

interface OnAcademyClickListener {
    fun onAcademyClick(view: View,academy: Academy?)
}

interface OnCheckPwListener {
    fun onPasswordCheck()
}

interface OnLessonClickListener {
    fun onLessonClick(view: View, lesson: Lesson?)
}

interface OnClassroomClickListener{
    fun onClassroomClick(view: View, classroom: Classroom?)
}

interface OnExamClickListener{
    fun onExamClick(view: View, exam: Exam?)
}

interface OnStudentClickListener{
    fun onBackgroundClick(view: View, student: Student?)
    fun onApplyButtonClick(view: View, student: Student?)
    fun onRemoveButtonClick(view: View, student: Student?)
}

interface OnTeacherClickListener{
    fun onBackgroundClick(view: View, teacher: Teacher?)
    fun onApplyButtonClick(view: View, teacher: Teacher?)
    fun onRemoveButtonClick(view: View, teacher: Teacher?)
}

interface OnCalendarResetListener{
    fun onResetCalendar()
}

interface OnDataChangedListener{
    fun onChangeData(isTeacher: Boolean, isChanged: Boolean)
}