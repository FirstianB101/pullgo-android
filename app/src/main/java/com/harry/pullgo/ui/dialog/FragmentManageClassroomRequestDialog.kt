package com.harry.pullgo.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.harry.pullgo.data.api.RetrofitClient
import com.harry.pullgo.data.objects.Classroom
import com.harry.pullgo.data.objects.Student
import com.harry.pullgo.databinding.DialogManageClassroomStudentInfoBinding
import com.harry.pullgo.ui.manageClassroomDetails.ManageClassroomDetailsViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragmentManageClassroomRequestDialog(private val selectedStudent: Student): DialogFragment() {
    private val binding by lazy{DialogManageClassroomStudentInfoBinding.inflate(layoutInflater)}

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
        binding.textViewManageClassroomStudentName.text = selectedStudent.account?.fullName
        binding.textViewManageClassroomStudentID.text = selectedStudent.account?.username
        binding.textViewManageClassroomStudentParentPhone.text = selectedStudent.parentPhone
        binding.textViewManageClassroomStudentPhone.text = selectedStudent.account?.phone
        binding.textViewManageClassroomStudentSchool.text = selectedStudent.schoolName
        binding.textViewManageClassroomStudentYear.text = selectedStudent.schoolYear.toString()

        binding.buttonManageClassroomKickStudent.visibility = View.GONE
    }

    companion object {
        const val TAG_MANAGE_STUDENT_DIALOG = "manage_classroom_request_dialog"
    }
}