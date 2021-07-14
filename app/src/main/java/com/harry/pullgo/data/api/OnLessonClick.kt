package com.harry.pullgo.data.api

import android.view.View
import com.harry.pullgo.data.objects.Lesson

interface OnLessonClick {
    fun onLessonClick(view: View, lesson: Lesson?)
}