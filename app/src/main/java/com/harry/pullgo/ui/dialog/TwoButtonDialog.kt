package com.harry.pullgo.ui.dialog

import android.app.Dialog
import android.content.Context
import android.view.Window
import android.widget.Button
import android.widget.TextView
import com.harry.pullgo.R

class TwoButtonDialog(context: Context) {
    private val dialog = Dialog(context)
    private lateinit var titleTextView: TextView
    private lateinit var contentTextView: TextView
    private lateinit var leftButton: Button
    private lateinit var rightButton: Button

    interface TwoButtonDialogLeftClickListener{
        fun onLeftClicked()
    }

    interface TwoButtonDialogRightClickListener{
        fun onRightClicked()
    }

    var leftClickListener : TwoButtonDialogLeftClickListener? = null
    var rightClickListener : TwoButtonDialogRightClickListener? = null

    fun start(title: String, contents: String, leftButtonContents: String, rightButtonContents: String){
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.layout_dialog_two_button)
        dialog.setCancelable(false)

        initialize()

        titleTextView.text=title
        contentTextView.text=contents
        leftButton.text = leftButtonContents
        rightButton.text = rightButtonContents

        leftButton.setOnClickListener {
            leftClickListener?.onLeftClicked()

            dialog.dismiss()
        }

        rightButton.setOnClickListener {
            rightClickListener?.onRightClicked()

            dialog.dismiss()
        }

        dialog.show()
    }

    private fun initialize(){
        titleTextView = dialog.findViewById(R.id.titleTextViewTwoButton)
        contentTextView = dialog.findViewById(R.id.contentTextViewTwoButton)
        leftButton = dialog.findViewById(R.id.leftButtonTwoButton)
        rightButton = dialog.findViewById(R.id.rightButtonTwoButton)
    }
}