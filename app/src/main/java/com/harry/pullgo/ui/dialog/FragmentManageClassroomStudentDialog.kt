package com.harry.pullgo.ui.dialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
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

class FragmentManageClassroomStudentDialog(
    private val selectedStudent: Student,
    private val selectedClassroom: Classroom
): DialogFragment() {
    private val binding by lazy{DialogManageClassroomStudentInfoBinding.inflate(layoutInflater)}

    private val viewModel: ManageClassroomDetailsViewModel by activityViewModels()

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
            kickStudent()
        }
    }

    private fun initViewModel(){

    }

    private fun kickStudent(){
        val client = RetrofitClient.getApiService()
        client.kickStudentFromClassroom(selectedClassroom.id!!, selectedStudent.id!!).enqueue(object: Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if(response.isSuccessful){
                    Toast.makeText(requireContext(),"해당 학생을 반에서 제외시켰습니다",Toast.LENGTH_SHORT).show()
                    viewModel.requestGetStudentsAppliedClassroom(selectedClassroom.id!!)
                    dismiss()
                }else{
                    Toast.makeText(requireContext(),"해당 반에 존재하지 않는 학생입니다",Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                Toast.makeText(requireContext(),"서버와 연결에 실패했습니다",Toast.LENGTH_SHORT).show()
            }
        })
    }

    companion object {
        const val TAG_MANAGE_STUDENT_DIALOG = "manage_classroom_student_dialog"
    }
}