package com.harry.pullgo.ui

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.harry.pullgo.R
import com.harry.pullgo.databinding.FragmentLessonInfoDialogBinding

class FragmentLessonInfoDialog : DialogFragment() {
    private val binding by lazy{FragmentLessonInfoDialogBinding.inflate(layoutInflater)}

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = MaterialAlertDialogBuilder(requireActivity())
        setLessonInformation()
        builder.setView(binding.root)
            .setPositiveButton("확인") { dialog: DialogInterface?, which: Int -> dismiss() }
        return builder.create()
    }

    private fun setLessonInformation() {
        //이곳에서 서버에서 받아온 수업정보 텍스트뷰에 적용
    }

    companion object {
        const val TAG_LESSON_INFO_DIALOG = "lesson_info_dialog"
        val instance: FragmentLessonInfoDialog
            get() = FragmentLessonInfoDialog()
    }
}