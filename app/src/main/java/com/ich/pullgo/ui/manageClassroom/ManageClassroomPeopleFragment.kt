package com.ich.pullgo.ui.manageClassroom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayout
import com.ich.pullgo.application.PullgoApplication
import com.ich.pullgo.data.adapter.StudentAdapter
import com.ich.pullgo.data.adapter.TeacherAdapter
import com.ich.pullgo.data.api.OnKickPersonListener
import com.ich.pullgo.data.api.OnStudentClickListener
import com.ich.pullgo.data.api.OnTeacherClickListener
<<<<<<< HEAD:app/src/main/java/com/ich/pullgo/ui/manageClassroom/ManageClassroomPeopleFragment.kt
import com.ich.pullgo.data.models.Classroom
import com.ich.pullgo.data.models.Student
import com.ich.pullgo.data.models.Teacher
import com.ich.pullgo.data.utils.Status
import com.ich.pullgo.databinding.FragmentManageClassroomManagePeopleBinding
=======
import com.ich.pullgo.data.utils.Status
import com.ich.pullgo.databinding.FragmentManageClassroomManagePeopleBinding
import com.ich.pullgo.domain.model.Classroom
import com.ich.pullgo.domain.model.Student
import com.ich.pullgo.domain.model.Teacher
>>>>>>> ich:app/src/main/java/com/harry/pullgo/ui/manageClassroom/ManageClassroomPeopleFragment.kt
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
        binding.tabLayoutManageClassroomManagePeople.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    when(tab.position){
                        0 -> {
                            refreshAdapter(false)
                        }
                        1 -> {
                            refreshAdapter(true)
                        }
                    }
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                onTabSelected(tab)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }
        })
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
    }

    override fun onResume() {
        val tab = binding.tabLayoutManageClassroomManagePeople.getTabAt(0)
        tab?.select()
        refreshAdapter(false)
        super.onResume()
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
                FragmentManageClassroomStudentDialog(student!!,selectedClassroom,object: OnKickPersonListener {
                    override fun noticeKicked() {
                        refreshAdapter(false)
                    }
                }).show(childFragmentManager, FragmentManageClassroomStudentDialog.TAG_MANAGE_STUDENT_DIALOG)
            }

            override fun onApplyButtonClick(view: View, student: Student?) {
            }

            override fun onRemoveButtonClick(view: View, student: Student?) {
            }
        }

        changeVisibilityIfNoResult(students.isEmpty())

        binding.recyclerViewManagePeople.adapter = studentAdapter
    }

    private fun displayTeachers(teachers: List<Teacher>){
        val teacherAdapter = TeacherAdapter(teachers)

        teacherAdapter.teacherClickListener = object: OnTeacherClickListener {
            override fun onBackgroundClick(view: View, teacher: Teacher?) {
                FragmentManageClassroomTeacherDialog(teacher!!,selectedClassroom,object: OnKickPersonListener{
                    override fun noticeKicked() {
                        refreshAdapter(true)
                    }
                }).show(childFragmentManager, FragmentManageClassroomTeacherDialog.TAG_MANAGE_TEACHER_DIALOG)
            }

            override fun onApplyButtonClick(view: View, teacher: Teacher?) {
            }

            override fun onRemoveButtonClick(view: View, teacher: Teacher?) {
            }
        }

        changeVisibilityIfNoResult(teachers.isEmpty())

        binding.recyclerViewManagePeople.adapter = teacherAdapter
    }

    private fun changeVisibilityIfNoResult(isEmpty: Boolean){
        val tabPosition = binding.tabLayoutManageClassroomManagePeople.selectedTabPosition
        if(isEmpty && tabPosition == 1){
            binding.textViewManagePeopleNoTeacher.visibility = View.VISIBLE
        }else if(isEmpty && tabPosition == 0){
            binding.textViewManagePeopleNoStudent.visibility = View.VISIBLE
        }else{
            binding.textViewManagePeopleNoStudent.visibility = View.GONE
            binding.textViewManagePeopleNoTeacher.visibility = View.GONE
        }
    }
}