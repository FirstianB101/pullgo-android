package com.harry.pullgo.ui.dialog

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.harry.pullgo.data.api.OnDataChanged
import com.harry.pullgo.data.api.RetrofitClient
import com.harry.pullgo.data.objects.Student
import com.harry.pullgo.data.objects.Teacher
import com.harry.pullgo.databinding.DialogKickPersonBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FragmentKickStudentDialog(
    private val selectedStudent: Student,
    private val academyName: String?,
    private val academyId: Long
    ): DialogFragment() {
    private val binding by lazy{DialogKickPersonBinding.inflate(layoutInflater)}

    private var dataChanged: OnDataChanged? = null

    override fun onStart() {
        super.onStart()
        dataChanged = requireActivity() as OnDataChanged

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
        builder.setView(binding.root)

        val _dialog = builder.create()
        _dialog.setCanceledOnTouchOutside(false)
        _dialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

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
            kickStudent()
        }
    }

    private fun kickStudent(){
        RetrofitClient.getApiService().kickStudent(academyId,selectedStudent.id!!).enqueue(object: Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if(response.isSuccessful){
                    Toast.makeText(requireContext(),"학생을 제외했습니다",Toast.LENGTH_SHORT).show()
                    dataChanged?.onChangeData(false,true)
                    dismiss()
                }else{
                    Toast.makeText(requireContext(),"학생을 제외하지 못했습니다",Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                Toast.makeText(requireContext(),"서버와 연결에 실패했습니다",Toast.LENGTH_SHORT).show()
            }
        })
    }

    companion object {
        const val TAG_KICK_STUDENT_DIALOG = "kick_student_dialog"
    }
}

class FragmentKickTeacherDialog(
    private val selectedTeacher: Teacher,
    private val academyName: String?,
    private val academyId: Long
    ): DialogFragment() {
    private val binding by lazy{DialogKickPersonBinding.inflate(layoutInflater)}

    private var dataChanged: OnDataChanged? = null

    override fun onStart() {
        super.onStart()
        dataChanged = requireActivity() as OnDataChanged

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
        builder.setView(binding.root)

        val _dialog = builder.create()
        _dialog.setCanceledOnTouchOutside(false)
        _dialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

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
            kickTeacher()
        }
    }

    private fun kickTeacher(){
        RetrofitClient.getApiService().kickTeacher(academyId,selectedTeacher.id!!).enqueue(object: Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if(response.isSuccessful){
                    Toast.makeText(requireContext(),"선생님을 제외했습니다",Toast.LENGTH_SHORT).show()
                    dataChanged?.onChangeData(false,true)
                    dismiss()
                }else{
                    Toast.makeText(requireContext(),"선생님을 제외하지 못했습니다",Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                Toast.makeText(requireContext(),"서버와 연결에 실패했습니다",Toast.LENGTH_SHORT).show()
            }
        })
    }

    companion object {
        const val TAG_KICK_STUDENT_DIALOG = "kick_student_dialog"
    }
}