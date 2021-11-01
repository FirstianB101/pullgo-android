package com.harry.pullgo.ui.manageClassroom

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.harry.pullgo.data.api.OnKickPersonListener
import com.harry.pullgo.data.models.Classroom
import com.harry.pullgo.data.models.Student
import com.harry.pullgo.data.utils.Status
import com.harry.pullgo.databinding.DialogManageClassroomStudentInfoBinding
import com.harry.pullgo.ui.dialog.TwoButtonDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentManageClassroomStudentDialog(
    private val selectedStudent: Student,
    private val selectedClassroom: Classroom,
    private val kickPersonListener: OnKickPersonListener?
): DialogFragment() {
    private val binding by lazy{DialogManageClassroomStudentInfoBinding.inflate(layoutInflater)}

    private val viewModel: ManageClassroomViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = MaterialAlertDialogBuilder(requireActivity())
        initialize()
        setListeners()
        initViewModel()
        builder.setView(binding.root)

        val _dialog = builder.create()
        _dialog.setCanceledOnTouchOutside(false)
        _dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        _dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
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
            showKickStudentDialog()
        }
    }

    private fun initViewModel(){
        viewModel.kickMessage.observe(requireActivity()){
            when(it.status){
                Status.SUCCESS -> {
                    Toast.makeText(requireContext(),"${it.data}",Toast.LENGTH_SHORT).show()
                    viewModel.requestGetStudentsAppliedClassroom(selectedClassroom.id!!)
                    kickPersonListener?.noticeKicked()
                    dismiss()
                }
                Status.LOADING -> {}
                Status.ERROR -> {
                    Toast.makeText(requireContext(),"${it.data}(${it.message})",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showKickStudentDialog(){
        val dialog = TwoButtonDialog(requireContext())
        dialog.leftClickListener = object: TwoButtonDialog.TwoButtonDialogLeftClickListener {
            override fun onLeftClicked() {
                viewModel.kickStudentFromClassroom(selectedClassroom.id!!,selectedStudent.id!!)
            }
        }
        dialog.start("학생 제외","${selectedStudent.account?.fullName}(${selectedStudent.account?.username}) 학생을 반에서 제외하시겠습니까?",
            "제외하기","취소")
    }

    companion object {
        const val TAG_MANAGE_STUDENT_DIALOG = "manage_classroom_student_dialog"
    }
}