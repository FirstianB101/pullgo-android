package com.harry.pullgo.data.api

import android.view.View
import com.harry.pullgo.data.objects.Academy
import com.harry.pullgo.data.objects.Classroom
import com.harry.pullgo.data.objects.Lesson

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