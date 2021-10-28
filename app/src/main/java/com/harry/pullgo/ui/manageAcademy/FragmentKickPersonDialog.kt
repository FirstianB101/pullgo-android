package com.harry.pullgo.ui.manageAcademy

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.harry.pullgo.application.PullgoApplication
import com.harry.pullgo.data.api.OnDataChangedListener
import com.harry.pullgo.data.models.Student
import com.harry.pullgo.data.models.Teacher
import com.harry.pullgo.data.repository.ManageAcademyRepository
import com.harry.pullgo.databinding.DialogKickPersonBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentKickStudentDialog(private val selectedStudent: Student, private val academyName: String?, val academyId: Long): DialogFragment() {
    private val binding by lazy{DialogKickPersonBinding.inflate(layoutInflater)}

    private var dataChangedListener: OnDataChangedListener? = null

    private val viewModel: ManageAcademyManagePeopleViewModel by viewModels()

    private val app: PullgoApplication by lazy{requireActivity().application as PullgoApplication }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dataChangedListener = activity as OnDataChangedListener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = MaterialAlertDialogBuilder(requireActivity())
        initialize()
        setListeners()
        initViewModel()
        builder.setView(binding.root)

        val _dialog = builder.create()
        _dialog.setCanceledOnTouchOutside(false)
        _dialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        _dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        return _dialog
    }

    private fun initialize(){
        binding.textViewKickPersonInfo.text =
            "${selectedStudent.account?.fullName} (${selectedStudent.account?.username})님을 $academyName 학원에서 제외하시겠습니까?"
    }

    private fun setListeners(){
        binding.buttonDialogKickCancel.setOnClickListener {
            dismiss()
        }

        binding.buttonDialogKick.setOnClickListener {
            viewModel.kickStudent(academyId,selectedStudent.id!!)
        }
    }

    private fun initViewModel(){
        viewModel.kickPersonMessage.observe(requireActivity()){
            Toast.makeText(requireContext(),it,Toast.LENGTH_SHORT).show()
            if(it == "학생을 제외했습니다"){
                dataChangedListener?.onChangeData(false)
                dismiss()
            }
        }
    }

    companion object {
        const val TAG_KICK_STUDENT_DIALOG = "kick_student_dialog"
    }
}

@AndroidEntryPoint
class FragmentKickTeacherDialog(private val selectedTeacher: Teacher, private val academyName: String?, private val academyId: Long): DialogFragment() {
    private val binding by lazy{DialogKickPersonBinding.inflate(layoutInflater)}

    private var dataChangedListener: OnDataChangedListener? = null

    private val app: PullgoApplication by lazy{requireActivity().application as PullgoApplication}

    private val viewModel: ManageAcademyManagePeopleViewModel by viewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dataChangedListener = activity as OnDataChangedListener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = MaterialAlertDialogBuilder(requireActivity())
        initialize()
        setListeners()
        initViewModel()
        builder.setView(binding.root)

        val _dialog = builder.create()
        _dialog.setCanceledOnTouchOutside(false)
        _dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        _dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        return _dialog
    }

    private fun initialize(){
        binding.textViewKickPersonInfo.text =
            "${selectedTeacher.account?.fullName} (${selectedTeacher.account?.username})님을 $academyName 학원에서 제외하시겠습니까?"
    }

    private fun setListeners(){
        binding.buttonDialogKickCancel.setOnClickListener {
            dismiss()
        }

        binding.buttonDialogKick.setOnClickListener {
            viewModel.kickTeacher(academyId,selectedTeacher.id!!)
        }
    }

    private fun initViewModel(){
        viewModel.kickPersonMessage.observe(requireActivity()){
            Toast.makeText(requireContext(),it,Toast.LENGTH_SHORT).show()
            if(it == "선생님을 제외했습니다"){
                dataChangedListener?.onChangeData(true)
                dismiss()
            }
        }
    }

    companion object {
        const val TAG_KICK_TEACHER_DIALOG = "kick_teacher_dialog"
    }
}