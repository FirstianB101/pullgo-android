package com.harry.pullgo.ui.manageClassroom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.harry.pullgo.data.adapter.StudentAdapter
import com.harry.pullgo.data.api.OnStudentClickListener
import com.harry.pullgo.data.models.Classroom
import com.harry.pullgo.data.models.Student
import com.harry.pullgo.data.repository.ManageClassroomRepository
import com.harry.pullgo.databinding.FragmentManageClassroomManageStudentBinding

class ManageClassroomPeopleFragment(private val selectedClassroom: Classroom): Fragment() {
    private val binding by lazy{FragmentManageClassroomManageStudentBinding.inflate(layoutInflater)}

    private val viewModel: ManageClassroomViewModel by activityViewModels{
        ManageClassroomViewModelFactory(ManageClassroomRepository(requireContext()))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)

        initialize()

        return binding.root
    }

    private fun initialize(){
        viewModel.studentsAppliedClassroom.observe(viewLifecycleOwner){
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
            studentAdapter.studentClickListenerListener = object: OnStudentClickListener {
                override fun onBackgroundClick(view: View, student: Student?) {
                    FragmentManageClassroomStudentDialog(student!!,selectedClassroom)
                        .show(parentFragmentManager, FragmentManageClassroomStudentDialog.TAG_MANAGE_STUDENT_DIALOG)
                }

                override fun onApplyButtonClick(view: View, student: Student?) {
                }

                override fun onRemoveButtonClick(view: View, student: Student?) {
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