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
    fun onStudentClick(view: View, student: Student?)
}

interface OnStudentAcceptAcademy{
    fun onStudentAcceptAcademy(view: View, student: Student?)
}

interface OnTeacherAcceptAcademy{
    fun onTeacherAcceptAcademy(view: View, teacher: Teacher?)
}