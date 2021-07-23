package com.harry.pullgo.ui.manageClassroomDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.harry.pullgo.data.api.OnStudentAcceptAcademy
import com.harry.pullgo.data.api.OnTeacherAcceptAcademy
import com.harry.pullgo.data.objects.Classroom
import com.harry.pullgo.data.objects.Student
import com.harry.pullgo.data.objects.Teacher
import com.harry.pullgo.data.repository.ManageClassroomDetailsRepository
import com.harry.pullgo.databinding.FragmentManageClassroomManageRequestsBinding
import com.harry.pullgo.data.adapter.TeacherAcceptApplyAcademyAdapter
import com.harry.pullgo.data.adapter.TeacherAcceptApplyAcademyAdapterForTeacher

class ManageRequestsFragment(private val selectedClassroom: Classroom ): Fragment() {
    private val binding by lazy{FragmentManageClassroomManageRequestsBinding.inflate(layoutInflater)}

    private lateinit var viewModel: ManageClassroomDetailsViewModel
    private lateinit var repository: ManageClassroomDetailsRepository

    private var selectedStudent: Student? = null
    private var selectedTeacher: Teacher? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)

        initialize()
        initViewModel()

        return binding.root
    }

    private fun initialize(){
        binding.recyclerViewManageClassroomRequest.layoutManager = LinearLayoutManager(requireContext())

        binding.switchManageClassroomRequest.setOnCheckedChangeListener { _, isChecked ->
            binding.textViewManageClassroomRequestSwitch.text = if(isChecked) "선생님" else "학생"
            refreshAdapter(isChecked)
        }
    }

    private fun refreshAdapter(isChecked: Boolean){
        if(isChecked)
            viewModel.requestGetStudentsRequestApplyClassroom(selectedClassroom.id!!)
        else
            viewModel.requestGetTeachersRequestApplyClassroom(selectedClassroom.id!!)
    }

    private fun initViewModel(){
        repository = ManageClassroomDetailsRepository()

        val factory = ManageClassroomDetailsViewModelFactory(repository)
        viewModel = ViewModelProvider(this,factory).get(ManageClassroomDetailsViewModel::class.java)

        viewModel.studentsAppliedClassroom.observe(requireActivity()){
            displayStudentRequests()
        }

        viewModel.teachersAppliedClassroom.observe(requireActivity()){
            displayTeacherRequests()
        }

        refreshAdapter(false)
    }

    private fun displayStudentRequests(){
        val data = viewModel.studentsRequestApplyClassroom.value

        val adapter = data?.let {
            TeacherAcceptApplyAcademyAdapter(it)
        }

        if (adapter != null) {
            adapter.buttonAcceptAcademyListener = object: OnStudentAcceptAcademy {
                override fun onStudentAcceptAcademy(view: View, student: Student?) {
                    selectedStudent = student
                    Snackbar.make(binding.root,"선텍: ${student?.account?.fullName}",Snackbar.LENGTH_SHORT).show()
                }
            }
        }
        binding.recyclerViewManageClassroomRequest.adapter = adapter

        showNoResultText(data?.isEmpty() == true)
    }

    private fun displayTeacherRequests(){
        val data = viewModel.teachersRequestApplyClassroom.value

        val adapter = data?.let {
            TeacherAcceptApplyAcademyAdapterForTeacher(it)
        }

        if (adapter != null) {
            adapter.buttonAcceptAcademyListener = object: OnTeacherAcceptAcademy {
                override fun onTeacherAcceptAcademy(view: View, teacher: Teacher?) {
                    selectedTeacher = teacher
                    Snackbar.make(binding.root,"선텍: ${teacher?.account?.fullName}",Snackbar.LENGTH_SHORT).show()
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