package com.harry.pullgo.ui.manageClassroom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.harry.pullgo.data.adapter.StudentApplyAdapter
import com.harry.pullgo.data.adapter.TeacherApplyAdapter
import com.harry.pullgo.data.api.OnStudentClickListener
import com.harry.pullgo.data.api.OnTeacherClickListener
import com.harry.pullgo.data.api.RetrofitClient
import com.harry.pullgo.data.models.Classroom
import com.harry.pullgo.data.models.Student
import com.harry.pullgo.data.models.Teacher
import com.harry.pullgo.data.repository.ManageClassroomRepository
import com.harry.pullgo.databinding.FragmentManageClassroomManageRequestsBinding
import com.harry.pullgo.ui.dialog.FragmentShowStudentInfoDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ManageRequestsFragment(private val selectedClassroom: Classroom): Fragment() {
    private val binding by lazy{FragmentManageClassroomManageRequestsBinding.inflate(layoutInflater)}

    private val viewModel: ManageClassroomDetailsViewModel by viewModels{ManageClassroomViewModelFactory(
        ManageClassroomRepository()
    )}

    private var selectedStudent: Student? = null
    private var selectedTeacher: Teacher? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)

        initialize()
        initViewModel()

        return binding.root
    }

    private fun initialize(){
        binding.switchManageClassroomRequest.setOnCheckedChangeListener { _, isChecked ->
            binding.textViewManageClassroomRequestSwitch.text = if(isChecked) "선생님" else "학생"
            refreshAdapter(isChecked)
        }
    }

    private fun refreshAdapter(isTeacher: Boolean){
        if(isTeacher)
            viewModel.requestGetTeachersRequestApplyClassroom(selectedClassroom.id!!)
        else
            viewModel.requestGetStudentsRequestApplyClassroom(selectedClassroom.id!!)
    }

    private fun initViewModel(){
        viewModel.studentsRequestApplyClassroom.observe(requireActivity()){
            displayStudentRequests()
            viewModel.requestGetStudentsAppliedClassroom(selectedClassroom.id!!)
        }

        viewModel.teachersRequestApplyClassroom.observe(requireActivity()){
            displayTeacherRequests()
            viewModel.requestGetTeachersAppliedClassroom(selectedClassroom.id!!)
        }

        viewModel.manageRequestMessage.observe(requireActivity()){
            Toast.makeText(requireContext(),it,Toast.LENGTH_SHORT).show()
            when(it){
                "학생 반 등록이 승인되었습니다", "해당 학생의 요청이 삭제되었습니다" -> refreshAdapter(false)
                "선생님 반 등록이 승인되었습니다", "해당 선생님의 요청이 삭제되었습니다" -> refreshAdapter(true)
            }
        }

        binding.recyclerViewManageClassroomRequest.layoutManager = LinearLayoutManager(requireContext())

        refreshAdapter(false)
    }

    private fun displayStudentRequests(){
        val data = viewModel.studentsRequestApplyClassroom.value

        val adapter = data?.let {
            StudentApplyAdapter(it,true)
        }

        if (adapter != null) {
            adapter.studentClickListenerListener = object: OnStudentClickListener {
                override fun onBackgroundClick(view: View, student: Student?) {
                    selectedStudent = student

                    val dialog = FragmentShowStudentInfoDialog(student!!)
                    dialog.show(parentFragmentManager, FragmentShowStudentInfoDialog.TAG_STUDENT_INFO_DIALOG)
                }

                override fun onApplyButtonClick(view: View, student: Student?) {
                    selectedStudent = student
                    viewModel.acceptStudent(selectedClassroom.id!!,student?.id!!)
                }

                override fun onRemoveButtonClick(view: View, student: Student?) {
                    viewModel.denyStudent(student?.id!!,selectedClassroom.id!!)
                }
            }
        }
        binding.recyclerViewManageClassroomRequest.adapter = adapter

        showNoResultText(data?.isEmpty() == true)
    }

    private fun displayTeacherRequests(){
        val data = viewModel.teachersRequestApplyClassroom.value

        val adapter = data?.let {
            TeacherApplyAdapter(it,true)
        }

        if (adapter != null) {
            adapter.teacherClickListenerListener = object: OnTeacherClickListener{
                override fun onBackgroundClick(view: View, teacher: Teacher?) {
                }

                override fun onApplyButtonClick(view: View, teacher: Teacher?) {
                    selectedTeacher = teacher
                    viewModel.acceptTeacher(selectedClassroom.id!!,teacher?.id!!)
                }

                override fun onRemoveButtonClick(view: View, teacher: Teacher?) {
                    viewModel.denyTeacher(selectedClassroom.id!!,teacher?.id!!)
                }
            }
        }
        binding.recyclerViewManageClassroomRequest.adapter = adapter

        showNoResultText(data?.isEmpty() == true)
    }

    private fun showNoResultText(isEmpty: Boolean){
        if(isEmpty)
            binding.textViewManageClassroomRequestNoResult.visibility = View.VISIBLE
        else
            binding.textViewManageClassroomRequestNoResult.visibility = View.GONE
    }
}