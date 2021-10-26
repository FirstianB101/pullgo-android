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
import com.harry.pullgo.application.PullgoApplication
import com.harry.pullgo.data.api.RetrofitClient
import com.harry.pullgo.data.models.Classroom
import com.harry.pullgo.data.models.Teacher
import com.harry.pullgo.data.repository.ManageClassroomRepository
import com.harry.pullgo.databinding.DialogManageClassroomTeacherInfoBinding
import com.harry.pullgo.ui.dialog.TwoButtonDialog

class FragmentManageClassroomTeacherDialog(
    private val selectedTeacher: Teacher,
    private val selectedClassroom: Classroom
): DialogFragment() {
    private val binding by lazy{ DialogManageClassroomTeacherInfoBinding.inflate(layoutInflater)}

    private val viewModel: ManageClassroomViewModel by activityViewModels{ManageClassroomViewModelFactory(
        ManageClassroomRepository(requireContext(), app.loginUser.token)
    )}

    private val app: PullgoApplication by lazy{requireActivity().application as PullgoApplication }

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
            Toast.makeText(requireContext(),it, Toast.LENGTH_SHORT).show()
            if(it == "해당 선생님을 반에서 제외시켰습니다"){
                viewModel.requestGetTeachersAppliedClassroom(selectedClassroom.id!!)
                dismiss()
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