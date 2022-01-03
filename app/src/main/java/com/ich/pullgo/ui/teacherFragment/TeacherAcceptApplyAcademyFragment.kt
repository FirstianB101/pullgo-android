package com.ich.pullgo.ui.teacherFragment

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.ich.pullgo.application.PullgoApplication
import com.ich.pullgo.data.adapter.StudentApplyAdapter
import com.ich.pullgo.data.adapter.TeacherApplyAdapter
import com.ich.pullgo.data.api.OnStudentClickListener
import com.ich.pullgo.data.api.OnTeacherClickListener
<<<<<<< HEAD:app/src/main/java/com/ich/pullgo/ui/teacherFragment/TeacherAcceptApplyAcademyFragment.kt
import com.ich.pullgo.data.models.Academy
import com.ich.pullgo.data.models.Student
import com.ich.pullgo.data.models.Teacher
import com.ich.pullgo.data.utils.Status
import com.ich.pullgo.databinding.FragmentAcceptApplyAcademyBinding
import com.ich.pullgo.ui.dialog.FragmentShowStudentInfoDialog
import com.ich.pullgo.ui.dialog.FragmentShowTeacherInfoDialog
import com.ich.pullgo.ui.dialog.TwoButtonDialog
=======
import com.ich.pullgo.data.utils.Status
import com.ich.pullgo.databinding.FragmentAcceptApplyAcademyBinding
import com.ich.pullgo.domain.model.Academy
import com.ich.pullgo.domain.model.Student
import com.ich.pullgo.domain.model.Teacher
import com.ich.pullgo.ui.dialog.FragmentShowStudentInfoDialog
import com.ich.pullgo.ui.dialog.FragmentShowTeacherInfoDialog
>>>>>>> ich:app/src/main/java/com/harry/pullgo/ui/teacherFragment/TeacherAcceptApplyAcademyFragment.kt
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TeacherAcceptApplyAcademyFragment: Fragment() {
    private val binding by lazy{FragmentAcceptApplyAcademyBinding.inflate(layoutInflater)}

    @Inject
    lateinit var app: PullgoApplication

    private val viewModel: TeacherAcceptApplyAcademyViewModel by viewModels()

    private var selectedAcademy: Academy? = null

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)

        initialize()
        initViewModel()

        return binding.root
    }

    private fun initialize(){
        binding.recyclerViewAcceptApplyAcademy.layoutManager = LinearLayoutManager(requireContext())

        binding.tabLayoutAcceptApplyAcademy.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
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

    private fun refreshAdapter(isTeacher: Boolean){
        if(isTeacher)
            viewModel.requestGetTeachers(selectedAcademy?.id!!)
        else
            viewModel.requestGetStudents(selectedAcademy?.id!!)
    }

    private fun initViewModel(){
        viewModel.studentsAppliedAcademy.observe(requireActivity()){
            when(it.status){
                Status.SUCCESS ->{
                    displayStudents()
                }
                Status.LOADING -> {}
                Status.ERROR -> {
                    Toast.makeText(requireContext(),"${it.data}(${it.message})",Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.teacherAppliedAcademy.observe(requireActivity()){
            when(it.status){
                Status.SUCCESS ->{
                    displayTeachers()
                }
                Status.LOADING -> {}
                Status.ERROR -> {
                    Toast.makeText(requireContext(),"${it.data}(${it.message})",Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.academyRepositories.observe(requireActivity()){
            when(it.status){
                Status.SUCCESS ->{
                    setSpinnerItems()
                }
                Status.LOADING -> {}
                Status.ERROR -> {
                    Toast.makeText(requireContext(),"${it.data}(${it.message})",Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.acceptOrDenyMessage.observe(requireActivity()){
            when(it.status){
                Status.SUCCESS ->{
                    Toast.makeText(requireContext(),"${it.data}",Toast.LENGTH_SHORT).show()
                    when(it.data){
                        "해당 학생의 요청을 승인하였습니다" -> viewModel.requestGetStudents(selectedAcademy?.id!!)
                        "해당 선생님의 요청을 승인하였습니다" -> viewModel.requestGetTeachers(selectedAcademy?.id!!)
                        "해당 학생의 요청이 삭제되었습니다" -> refreshAdapter(false)
                        "해당 선생님의 요청이 삭제되었습니다" -> refreshAdapter(true)
                    }
                }
                Status.LOADING -> {}
                Status.ERROR -> {
                    Toast.makeText(requireContext(),"${it.data}(${it.message})",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.requestTeacherAcademies(app.loginUser.teacher?.id!!)
    }

    override fun onPause() {
        super.onPause()
        changeLayoutVisibility(false)
    }

    private fun setSpinnerItems(){
        val academies = viewModel.academyRepositories.value?.data!!

        val adapter: ArrayAdapter<Academy> = ArrayAdapter(requireContext(),R.layout.simple_spinner_dropdown_item,academies)
        binding.spinnerAcceptApplyAcademy.adapter = adapter

        binding.spinnerAcceptApplyAcademy.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                changeLayoutVisibility(true)

                selectedAcademy = academies[position]
                viewModel.requestGetStudents(academies[position].id!!)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        if(academies.isEmpty())
            binding.textViewAcceptApplyAcademyNoAcademy.visibility = View.VISIBLE
    }

    private fun displayStudents(){
        val data = viewModel.studentsAppliedAcademy.value?.data

        val adapter = data?.let {
            StudentApplyAdapter(it,true)
        }

        if (adapter != null) {
            adapter.studentClickListener = object: OnStudentClickListener {
                override fun onBackgroundClick(view: View, student: Student?) {
                   FragmentShowStudentInfoDialog(student!!).show(parentFragmentManager, FragmentShowStudentInfoDialog.TAG_STUDENT_INFO_DIALOG)
                }

                override fun onApplyButtonClick(view: View, student: Student?) {
                    val dialog = TwoButtonDialog(requireContext())
                    dialog.leftClickListener = object: TwoButtonDialog.TwoButtonDialogLeftClickListener{
                        override fun onLeftClicked() {
                            viewModel.acceptStudentApplyAcademy(selectedAcademy?.id!!,student?.id!!)
                        }
                    }
                    dialog.start("학원 가입 승인","${student?.account?.fullName} 학생을 학원에 등록하시겠습니까?","가입 승인","취소")
                }

                override fun onRemoveButtonClick(view: View, student: Student?) {
                    val dialog = TwoButtonDialog(requireContext())
                    dialog.leftClickListener = object: TwoButtonDialog.TwoButtonDialogLeftClickListener{
                        override fun onLeftClicked() {
                            viewModel.denyStudentApplyAcademy(selectedAcademy?.id!!,student?.id!!)
                        }
                    }
                    dialog.start("학원 가입 거절","${student?.account?.fullName} 학생의 요청을 거절하시겠습니까?","가입 거절","취소")
                }
            }
        }
        binding.recyclerViewAcceptApplyAcademy.adapter = adapter

        showNoResultText(data?.isEmpty() == true)
    }

    private fun displayTeachers(){
        val data = viewModel.teacherAppliedAcademy.value?.data

        val adapter = data?.let {
            TeacherApplyAdapter(it,true)
        }

        if (adapter != null) {
            adapter.teacherClickListener = object: OnTeacherClickListener {
                override fun onBackgroundClick(view: View, teacher: Teacher?) {
                    FragmentShowTeacherInfoDialog(teacher!!).show(parentFragmentManager, FragmentShowStudentInfoDialog.TAG_STUDENT_INFO_DIALOG)
                }

                override fun onApplyButtonClick(view: View, teacher: Teacher?) {
                    viewModel.acceptTeacherApplyAcademy(selectedAcademy?.id!!,teacher?.id!!)
                }

                override fun onRemoveButtonClick(view: View, teacher: Teacher?) {
                    viewModel.denyTeacherApplyAcademy(selectedAcademy?.id!!,teacher?.id!!)
                }
            }
        }
        binding.recyclerViewAcceptApplyAcademy.adapter = adapter

        showNoResultText(data?.isEmpty() == true)
    }

    private fun changeLayoutVisibility(isLayoutVisible: Boolean){
        if(isLayoutVisible){
            val anim = AnimationUtils.loadAnimation(requireContext(), com.ich.pullgo.R.anim.alpha)
            binding.tabLayoutAcceptApplyAcademy.visibility = View.VISIBLE
            binding.frameLayoutAcceptApplyAcademy.visibility = View.VISIBLE

            binding.tabLayoutAcceptApplyAcademy.startAnimation(anim)
            binding.frameLayoutAcceptApplyAcademy.startAnimation(anim)
        }else{
            binding.tabLayoutAcceptApplyAcademy.visibility = View.GONE
            binding.frameLayoutAcceptApplyAcademy.visibility = View.GONE
        }
    }

    private fun showNoResultText(isEmpty: Boolean){
        if(isEmpty)
            binding.textViewAcceptApplyAcademyNoResult.visibility = View.VISIBLE
        else
            binding.textViewAcceptApplyAcademyNoResult.visibility = View.GONE
    }
}