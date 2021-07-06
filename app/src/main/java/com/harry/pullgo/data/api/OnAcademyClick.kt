package com.harry.pullgo.data.api

import android.view.View
import com.harry.pullgo.data.objects.Academy

interface OnAcademyClick {
    fun onAcademyClick(view: View,academy: Academy?)
}