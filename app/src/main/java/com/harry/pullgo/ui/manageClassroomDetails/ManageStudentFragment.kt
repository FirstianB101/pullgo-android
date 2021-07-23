package com.harry.pullgo.ui.manageClassroomDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.harry.pullgo.data.adapter.StudentAdapter
import com.harry.pullgo.data.api.OnStudentClick
import com.harry.pullgo.data.objects.Classroom
import com.harry.pullgo.data.objects.Student
import com.harry.pullgo.databinding.FragmentManageClassroomManageStudentBinding
import com.harry.pullgo.ui.dialog.FragmentManageClassroomStudentDialog

class ManageStudentFragment(private val selectedClassroom: Classroom): Fragment() {
    private val binding by lazy{FragmentManageClassroomManageStudentBinding.inflate(layoutInflater)}

    private lateinit var viewModel: ManageClassroomDetailsViewModel
    private var selectedStudent: Student? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)

        initialize()

        return binding.root
    }

    private fun initialize(){
        viewModel = ViewModelProvider(requireActivity()).get(ManageClassroomDetailsViewModel::class.java)

        viewModel.studentsAppliedClassroom.observe(requireActivity()){
            displayStudents()
        }

        viewModel.requestGetStudentsAppliedClassroom(selectedClassroom.id!!)

        binding.recyclerViewManageStudent.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun displayStudents(){
        val data = viewModel.studentsAppliedClassroom.value

        val studentAdapter = data?.let {
            StudentAdapter(it)
        }

        if (studentAdapter != null) {
            studentAdapter.studentClickListener = object: OnStudentClick {
                override fun onStudentClick(view: View, student: Student?) {
                    selectedStudent = student
                    FragmentManageClassroomStudentDialog(student!!,selectedClassroom)
                        .show(parentFragmentManager,FragmentManageClassroomStudentDialog.TAG_MANAGE_STUDENT_DIALOG)
                }
            }
        }

        changeVisibilityIfNoResult(data?.isEmpty() == true)

        binding.recyclerViewManageStudent.adapter = studentAdapter
    }

    private fun changeVisibilityIfNoResult(isEmpty: Boolean){
        if(isEmpty){
            binding.textViewManageStudentNoResult.visibility = View.VISIBLE
        }else{
            binding.textViewManageStudentNoResult.visibility = View.GONE
        }
    }
}