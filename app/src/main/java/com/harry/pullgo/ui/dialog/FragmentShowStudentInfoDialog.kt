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
import com.harry.pullgo.data.models.Student
import com.harry.pullgo.databinding.DialogManageClassroomStudentInfoBinding

class FragmentShowStudentInfoDialog(private val selectedStudent: Student): DialogFragment() {
    private val binding by lazy{DialogManageClassroomStudentInfoBinding.inflate(layoutInflater)}

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = MaterialAlertDialogBuilder(requireActivity())
        initialize()
        builder.setView(binding.root)

        val _dialog = builder.create()
        _dialog.setCanceledOnTouchOutside(false)
        _dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        _dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        return _dialog
    }

    private fun initialize(){
        binding.textViewManageClassroomStudentName.text = selectedStudent.account?.fullName
        binding.textViewManageClassroomStudentID.text = selectedStudent.account?.username
        binding.textViewManageClassroomStudentParentPhone.text = selectedStudent.parentPhone
        binding.textViewManageClassroomStudentPhone.text = selectedStudent.account?.phone
        binding.textViewManageClassroomStudentSchool.text = selectedStudent.schoolName
        binding.textViewManageClassroomStudentYear.text = selectedStudent.schoolYear.toString()

        binding.buttonManageClassroomKickStudent.visibility = View.GONE
    }

    companion object {
        const val TAG_STUDENT_INFO_DIALOG = "student_info_dialog"
    }
}