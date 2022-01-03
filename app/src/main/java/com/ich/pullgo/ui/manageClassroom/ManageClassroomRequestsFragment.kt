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
import com.ich.pullgo.data.adapter.StudentApplyAdapter
import com.ich.pullgo.data.adapter.TeacherApplyAdapter
import com.ich.pullgo.data.api.OnStudentClickListener
import com.ich.pullgo.data.api.OnTeacherClickListener
import com.ich.pullgo.data.utils.Status
import com.ich.pullgo.databinding.FragmentManageClassroomManageRequestsBinding
import com.ich.pullgo.domain.model.Classroom
import com.ich.pullgo.domain.model.Student
import com.ich.pullgo.domain.model.Teacher
import com.ich.pullgo.ui.dialog.FragmentShowStudentInfoDialog
import com.ich.pullgo.ui.dialog.FragmentShowTeacherInfoDialog
import com.ich.pullgo.ui.dialog.TwoButtonDialog
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ManageClassroomRequestsFragment(private val selectedClassroom: Classroom): Fragment() {
    private val binding by lazy{FragmentManageClassroomManageRequestsBinding.inflate(layoutInflater)}

    @Inject
    lateinit var app: PullgoApplication

    private val viewModel: ManageClassroomViewModel by viewModels()

    private var selectedStudent: Student? = null
    private var selectedTeacher: Teacher? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)

        setListeners()
        initViewModel()

        return binding.root
    }

    private fun setListeners(){
        binding.tabLayoutManageClassroomManageRequest.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
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

    override fun onResume() {
        val tab = binding.tabLayoutManageClassroomManageRequest.getTabAt(0)
        tab?.select()
        refreshAdapter(false)
        super.onResume()
    }

    private fun refreshAdapter(isTeacher: Boolean){
        if(isTeacher)
            viewModel.requestGetTeachersRequestApplyClassroom(selectedClassroom.id!!)
        else
            viewModel.requestGetStudentsRequestApplyClassroom(selectedClassroom.id!!)
    }

    private fun initViewModel(){
        viewModel.studentsRequestApplyClassroom.observe(viewLifecycleOwner){
            when(it.status){
                Status.LOADING -> {
                }
                Status.SUCCESS -> {
                    displayStudentRequests()
                }
                Status.ERROR -> {
                    Toast.makeText(requireContext(),"${it.data}(${it.message})", Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.teachersRequestApplyClassroom.observe(viewLifecycleOwner){
            when(it.status){
                Status.LOADING -> {
                }
                Status.SUCCESS -> {
                    displayTeacherRequests()
                }
                Status.ERROR -> {
                    Toast.makeText(requireContext(),"${it.data}(${it.message})", Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.manageStudentRequestMessage.observe(viewLifecycleOwner){
            when(it.status){
                Status.LOADING -> {
                }
                Status.SUCCESS -> {
                    Toast.makeText(requireContext(),"${it.data}",Toast.LENGTH_SHORT).show()
                    refreshAdapter(false)
                }
                Status.ERROR -> {
                    Toast.makeText(requireContext(),"${it.data}(${it.message})", Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.manageTeacherRequestMessage.observe(viewLifecycleOwner){
            when(it.status){
                Status.LOADING -> {
                }
                Status.SUCCESS -> {
                    Toast.makeText(requireContext(),"${it.data}",Toast.LENGTH_SHORT).show()
                    refreshAdapter(true)
                }
                Status.ERROR -> {
                    Toast.makeText(requireContext(),"${it.data}(${it.message})", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun displayStudentRequests(){
        val data = viewModel.studentsRequestApplyClassroom.value?.data

        val adapter = data?.let {
            StudentApplyAdapter(it,true)
        }

        if (adapter != null) {
            adapter.studentClickListener = object: OnStudentClickListener {
                override fun onBackgroundClick(view: View, student: Student?) {
                    FragmentShowStudentInfoDialog(student!!)
                        .show(childFragmentManager, FragmentShowStudentInfoDialog.TAG_STUDENT_INFO_DIALOG)
                }

                override fun onApplyButtonClick(view: View, student: Student?) {
                    selectedStudent = student
                    showAcceptStudentDialog()
                }

                override fun onRemoveButtonClick(view: View, student: Student?) {
                    selectedStudent = student
                    showDenyStudentDialog()
                }
            }
        }
        binding.recyclerViewManageClassroomRequest.adapter = adapter

        showNoResultText(data?.isEmpty() == true)
    }

    private fun showAcceptStudentDialog(){
        val dialog = TwoButtonDialog(requireContext())
        dialog.leftClickListener = object: TwoButtonDialog.TwoButtonDialogLeftClickListener {
            override fun onLeftClicked() {
                viewModel.acceptStudent(selectedClassroom.id!!,selectedStudent?.id!!)
            }
        }
        dialog.start("학생 등록","${selectedStudent?.account?.fullName}(${selectedStudent?.account?.username}) 학생을 반에 등록하시겠습니까?",
            "등록하기","취소")
    }

    private fun showDenyStudentDialog(){
        val dialog = TwoButtonDialog(requireContext())
        dialog.leftClickListener = object: TwoButtonDialog.TwoButtonDialogLeftClickListener {
            override fun onLeftClicked() {
                viewModel.denyStudent(selectedStudent?.id!!,selectedClassroom.id!!)
            }
        }
        dialog.start("등록 거절","${selectedStudent?.account?.fullName}(${selectedStudent?.account?.username}) 학생의 등록 요청을 거절하시겠습니까?",
            "거절하기","취소")
    }

    private fun displayTeacherRequests(){
        val data = viewModel.teachersRequestApplyClassroom.value?.data

        val adapter = data?.let {
            TeacherApplyAdapter(it,true)
        }

        if (adapter != null) {
            adapter.teacherClickListener = object: OnTeacherClickListener{
                override fun onBackgroundClick(view: View, teacher: Teacher?) {
                    FragmentShowTeacherInfoDialog(teacher!!)
                        .show(childFragmentManager, FragmentShowStudentInfoDialog.TAG_STUDENT_INFO_DIALOG)
                }

                override fun onApplyButtonClick(view: View, teacher: Teacher?) {
                    selectedTeacher = teacher
                    showAcceptTeacherDialog()
                }

                override fun onRemoveButtonClick(view: View, teacher: Teacher?) {
                    selectedTeacher = teacher
                    showDenyTeacherDialog()
                }
            }
        }
        binding.recyclerViewManageClassroomRequest.adapter = adapter

        showNoResultText(data?.isEmpty() == true)
    }

    private fun showAcceptTeacherDialog(){
        val dialog = TwoButtonDialog(requireContext())
        dialog.leftClickListener = object: TwoButtonDialog.TwoButtonDialogLeftClickListener {
            override fun onLeftClicked() {
                viewModel.acceptTeacher(selectedClassroom.id!!,selectedTeacher?.id!!)
            }
        }
        dialog.start("선생님 등록","${selectedTeacher?.account?.fullName}(${selectedTeacher?.account?.username}) 선생님을 반에 등록하시겠습니까?",
            "등록하기","취소")
    }

    private fun showDenyTeacherDialog(){
        val dialog = TwoButtonDialog(requireContext())
        dialog.leftClickListener = object: TwoButtonDialog.TwoButtonDialogLeftClickListener {
            override fun onLeftClicked() {
                viewModel.denyTeacher(selectedTeacher?.id!!,selectedClassroom.id!!)
            }
        }
        dialog.start("등록 거절","${selectedTeacher?.account?.fullName}(${selectedTeacher?.account?.username}) 선생님의 등록 요청을 거절하시겠습니까?",
            "거절하기","취소")
    }

    private fun showNoResultText(isEmpty: Boolean){
        if(isEmpty)
            binding.textViewManageClassroomRequestNoResult.visibility = View.VISIBLE
        else
            binding.textViewManageClassroomRequestNoResult.visibility = View.GONE
    }
}