package com.harry.pullgo.ui.manageClassroom

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.harry.pullgo.data.models.Classroom
import com.harry.pullgo.data.models.Student
import com.harry.pullgo.data.repository.ManageClassroomRepository
import com.harry.pullgo.databinding.DialogManageClassroomStudentInfoBinding
import com.harry.pullgo.ui.dialog.TwoButtonDialog

class FragmentManageClassroomStudentDialog(
    private val selectedStudent: Student,
    private val selectedClassroom: Classroom
): DialogFragment() {
    private val binding by lazy{DialogManageClassroomStudentInfoBinding.inflate(layoutInflater)}

    private val viewModel: ManageClassroomViewModel by activityViewModels{ManageClassroomViewModelFactory(
        ManageClassroomRepository(requireContext())
    )}

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
        setListeners()
        initViewModel()
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
    }

    private fun setListeners(){
        binding.buttonManageClassroomKickStudent.setOnClickListener {
            val dialog = TwoButtonDialog(requireContext())
            dialog.leftClickListener = object: TwoButtonDialog.TwoButtonDialogLeftClickListener {
                override fun onLeftClicked() {
                    viewModel.kickStudentFromClassroom(selectedClassroom.id!!,selectedStudent.id!!)
                }
            }
            dialog.start("학생 제외","${selectedStudent.account?.fullName}(${selectedStudent.account?.username}) 학생을 반에서 제외하시겠습니까?",
                "제외하기","취소")
        }
    }

    private fun initViewModel(){
        viewModel.kickMessage.observe(requireActivity()){
            Toast.makeText(requireContext(),it,Toast.LENGTH_SHORT).show()
            if(it == "해당 학생을 반에서 제외시켰습니다"){
                viewModel.requestGetStudentsAppliedClassroom(selectedClassroom.id!!)
                dismiss()
            }
        }
    }

    companion object {
        const val TAG_MANAGE_STUDENT_DIALOG = "manage_classroom_student_dialog"
    }
}