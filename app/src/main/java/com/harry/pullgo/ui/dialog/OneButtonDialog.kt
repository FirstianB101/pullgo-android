package com.harry.pullgo.ui.dialog

import android.app.Dialog
import android.content.Context
import android.view.Window
import android.widget.Button
import android.widget.TextView
import com.harry.pullgo.R

class OneButtonDialog(context: Context) {
    private val dialog = Dialog(context)
    private lateinit var titleTextView: TextView
    private lateinit var contentTextView: TextView
    private lateinit var centerButton: Button

    interface OneButtonDialogClickListener{
        fun onCenterClicked()
    }

    var centerClickListener : OneButtonDialogClickListener? = null

    fun start(title: String,contents: String, centerButtonContents: String){
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.layout_dialog_one_button)
        dialog.setCancelable(false)

        initialize()

        titleTextView.text=title
        contentTextView.text=contents
        centerButton.text=centerButtonContents

        centerButton.setOnClickListener {
            centerClickListener?.onCenterClicked()

            dialog.dismiss()
        }

        dialog.show()
    }

    private fun initialize(){
        titleTextView = dialog.findViewById(R.id.titleTextViewOneButton)
        contentTextView = dialog.findViewById(R.id.contentTextViewOneButton)
        centerButton = dialog.findViewById(R.id.centerButton)
    }
}