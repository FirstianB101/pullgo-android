package com.ich.pullgo.ui.manageClassroom

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.ich.pullgo.data.api.OnKickPersonListener
<<<<<<< HEAD:app/src/main/java/com/ich/pullgo/ui/manageClassroom/FragmentManageClassroomTeacherDialog.kt
import com.ich.pullgo.data.models.Classroom
import com.ich.pullgo.data.models.Teacher
import com.ich.pullgo.data.utils.Status
import com.ich.pullgo.databinding.DialogManageClassroomTeacherInfoBinding
=======
import com.ich.pullgo.data.utils.Status
import com.ich.pullgo.databinding.DialogManageClassroomTeacherInfoBinding
import com.ich.pullgo.domain.model.Classroom
import com.ich.pullgo.domain.model.Teacher
>>>>>>> ich:app/src/main/java/com/harry/pullgo/ui/manageClassroom/FragmentManageClassroomTeacherDialog.kt
import com.ich.pullgo.ui.dialog.TwoButtonDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentManageClassroomTeacherDialog(
    private val selectedTeacher: Teacher,
    private val selectedClassroom: Classroom,
    private val kickPersonListener: OnKickPersonListener?
): DialogFragment() {
    private val binding by lazy{ DialogManageClassroomTeacherInfoBinding.inflate(layoutInflater)}

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
        binding.textViewManageClassroomTeacherName.text = selectedTeacher.account?.fullName
        binding.textViewManageClassroomTeacherID.text = selectedTeacher.account?.username
        binding.textViewManageClassroomTeacherPhone.text = selectedTeacher.account?.phone
    }

    private fun setListeners(){
        binding.buttonManageClassroomKickTeacher.setOnClickListener {
            showKickTeacherDialog()
        }
    }

    private fun initViewModel(){
        viewModel.kickMessage.observe(requireActivity()){
            when(it.status){
                Status.SUCCESS -> {
                    Toast.makeText(requireContext(),"${it.data}",Toast.LENGTH_SHORT).show()
                    viewModel.requestGetTeachersAppliedClassroom(selectedClassroom.id!!)
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

    private fun showKickTeacherDialog(){
        val dialog = TwoButtonDialog(requireContext())
        dialog.leftClickListener = object: TwoButtonDialog.TwoButtonDialogLeftClickListener {
            override fun onLeftClicked() {
                viewModel.kickTeacherFromClassroom(selectedClassroom.id!!,selectedTeacher.id!!)
            }
        }
        dialog.start("선생님 제외","${selectedTeacher.account?.fullName}(${selectedTeacher.account?.username}) 선생님을 반에서 제외하시겠습니까?",
            "제외하기","취소")
    }

    companion object {
        const val TAG_MANAGE_TEACHER_DIALOG = "manage_classroom_teacher_dialog"
    }
}