package com.harry.pullgo

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat

class TabButton : AppCompatButton {
    constructor(context: Context) : super(context) {
        init(context)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    private fun init(context: Context) {
        val resources = resources
        setBackgroundColor(Color.WHITE)
        setTextColor(ContextCompat.getColor(context,R.color.main_color))
    }
}