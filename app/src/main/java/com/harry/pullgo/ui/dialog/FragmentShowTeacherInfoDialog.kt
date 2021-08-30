package com.harry.pullgo.ui.dialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.harry.pullgo.data.objects.Teacher
import com.harry.pullgo.databinding.DialogManageClassroomTeacherInfoBinding

class FragmentShowTeacherInfoDialog(private val selectedTeacher: Teacher): DialogFragment() {
    private val binding by lazy{ DialogManageClassroomTeacherInfoBinding.inflate(layoutInflater)}

    override fun onStart() {
        super.onStart()

        val dialog = dialog
        if (dialog != null) {
            dialog.window!!.setLayout(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = MaterialAlertDialogBuilder(requireActivity())
        initialize()
        builder.setView(binding.root)

        val _dialog = builder.create()
        _dialog.setCanceledOnTouchOutside(false)
        _dialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        return _dialog
    }

    private fun initialize(){
        binding.textViewManageClassroomTeacherName.text = selectedTeacher.account?.fullName
        binding.textViewManageClassroomTeacherID.text = selectedTeacher.account?.username
        binding.textViewManageClassroomTeacherPhone.text = selectedTeacher.account?.phone

        binding.buttonManageClassroomKickTeacher.visibility = View.GONE
    }

    companion object {
        const val TAG_TEACHER_INFO_DIALOG = "teacher_info_dialog"
    }
}