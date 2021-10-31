package com.harry.pullgo.ui.manageClassroom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import com.harry.pullgo.application.PullgoApplication
import com.harry.pullgo.data.adapter.StudentAdapter
import com.harry.pullgo.data.adapter.TeacherAdapter
import com.harry.pullgo.data.api.OnStudentClickListener
import com.harry.pullgo.data.api.OnTeacherClickListener
import com.harry.pullgo.data.models.Classroom
import com.harry.pullgo.data.models.Student
import com.harry.pullgo.data.models.Teacher
import com.harry.pullgo.data.repository.ManageClassroomRepository
import com.harry.pullgo.data.utils.Status
import com.harry.pullgo.databinding.FragmentManageClassroomManagePeopleBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ManageClassroomPeopleFragment(private val selectedClassroom: Classroom): Fragment() {
    private val binding by lazy{FragmentManageClassroomManagePeopleBinding.inflate(layoutInflater)}

    @Inject
    lateinit var app: PullgoApplication

    private val viewModel: ManageClassroomViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)

        initialize()
        initViewModel()

        return binding.root
    }

    private fun initialize(){
        binding.switchManageClassroomPeople.setOnCheckedChangeListener { _, isChecked ->
            binding.textViewManageClassroomPeopleSwitch.text = if(isChecked) "선생님" else "학생"
            refreshAdapter(isChecked)
        }
    }

    private fun initViewModel(){
        viewModel.studentsAppliedClassroom.observe(viewLifecycleOwner){
            when(it.status){
                Status.SUCCESS -> {
                    displayStudents(it.data!!)
                }
                Status.LOADING -> {}
                Status.ERROR -> {
                    Toast.makeText(requireContext(),"${it.data}(${it.message})", Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.teachersAppliedClassroom.observe(viewLifecycleOwner){
            when(it.status){
                Status.SUCCESS -> {
                    displayTeachers(it.data!!)
                }
                Status.LOADING -> {}
                Status.ERROR -> {
                    Toast.makeText(requireContext(),"${it.data}(${it.message})", Toast.LENGTH_SHORT).show()
                }
            }
        }

        refreshAdapter(false)
    }

    private fun refreshAdapter(isTeacher: Boolean){
        if(isTeacher)
            viewModel.requestGetTeachersAppliedClassroom(selectedClassroom.id!!)
        else
            viewModel.requestGetStudentsAppliedClassroom(selectedClassroom.id!!)
    }

    private fun displayStudents(students: List<Student>){
        val studentAdapter = StudentAdapter(students)

        studentAdapter.studentClickListener = object: OnStudentClickListener {
            override fun onBackgroundClick(view: View, student: Student?) {
                FragmentManageClassroomStudentDialog(student!!,selectedClassroom)
                    .show(parentFragmentManager, FragmentManageClassroomStudentDialog.TAG_MANAGE_STUDENT_DIALOG)
            }

            override fun onApplyButtonClick(view: View, student: Student?) {
            }

            override fun onRemoveButtonClick(view: View, student: Student?) {
            }
        }

        changeVisibilityIfNoResult(students.isEmpty())

        binding.recyclerViewManageStudent.adapter = studentAdapter
    }

    private fun displayTeachers(teachers: List<Teacher>){
        val teacherAdapter = TeacherAdapter(teachers)

        teacherAdapter.teacherClickListener = object: OnTeacherClickListener {
            override fun onBackgroundClick(view: View, teacher: Teacher?) {
                FragmentManageClassroomTeacherDialog(teacher!!,selectedClassroom)
                    .show(parentFragmentManager, FragmentManageClassroomTeacherDialog.TAG_MANAGE_TEACHER_DIALOG)
            }

            override fun onApplyButtonClick(view: View, teacher: Teacher?) {
            }

            override fun onRemoveButtonClick(view: View, teacher: Teacher?) {
            }
        }

        changeVisibilityIfNoResult(teachers.isEmpty())

        binding.recyclerViewManageStudent.adapter = teacherAdapter
    }

    private fun changeVisibilityIfNoResult(isEmpty: Boolean){
        val isChecked = binding.switchManageClassroomPeople.isChecked
        if(isEmpty && isChecked){
            binding.textViewManagePeopleNoTeacher.visibility = View.VISIBLE
        }else if(isEmpty && !isChecked){
            binding.textViewManagePeopleNoStudent.visibility = View.VISIBLE
        }else{
            binding.textViewManagePeopleNoStudent.visibility = View.GONE
            binding.textViewManagePeopleNoTeacher.visibility = View.GONE
        }
    }
}