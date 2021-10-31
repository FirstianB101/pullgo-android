package com.harry.pullgo.ui.teacherFragment

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.harry.pullgo.application.PullgoApplication
import com.harry.pullgo.data.adapter.StudentApplyAdapter
import com.harry.pullgo.data.adapter.TeacherApplyAdapter
import com.harry.pullgo.data.api.OnStudentClickListener
import com.harry.pullgo.data.api.OnTeacherClickListener
import com.harry.pullgo.data.models.Academy
import com.harry.pullgo.data.models.Student
import com.harry.pullgo.data.models.Teacher
import com.harry.pullgo.data.utils.Status
import com.harry.pullgo.databinding.FragmentAcceptApplyAcademyBinding
import com.harry.pullgo.ui.dialog.FragmentShowStudentInfoDialog
import com.harry.pullgo.ui.dialog.FragmentShowTeacherInfoDialog
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

        binding.switchAcceptApplyAcademy.setOnCheckedChangeListener { _, isChecked ->
            binding.textViewAcceptApplyAcademySwitch.text = if(isChecked) "선생님" else "학생"
            refreshAdapter(isChecked)
        }
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

        viewModel.requestTeacherAcademies(app.loginUser.teacher?.id!!)
    }

    private fun setSpinnerItems(){
        val academies = viewModel.academyRepositories.value?.data!!

        val adapter: ArrayAdapter<Academy> = ArrayAdapter(requireContext(),R.layout.simple_spinner_dropdown_item,academies)
        binding.spinnerAcceptApplyAcademy.adapter = adapter

        binding.spinnerAcceptApplyAcademy.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(!binding.switchAcceptApplyAcademy.isEnabled)
                    binding.switchAcceptApplyAcademy.isEnabled = true

                selectedAcademy = academies[position]
                viewModel.requestGetStudents(academies[position].id!!)

                initializeSwitch()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        if(academies.isEmpty())
            binding.textViewAcceptApplyAcademyNoAcademy.visibility = View.VISIBLE
    }

    private fun initializeSwitch(){
        binding.switchAcceptApplyAcademy.isChecked = false
        binding.textViewAcceptApplyAcademySwitch.text = "학생"
        refreshAdapter(false)
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
                    viewModel.acceptStudentApplyAcademy(selectedAcademy?.id!!,student?.id!!)
                }

                override fun onRemoveButtonClick(view: View, student: Student?) {
                    viewModel.denyStudentApplyAcademy(selectedAcademy?.id!!,student?.id!!)
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

    private fun showNoResultText(isEmpty: Boolean){
        if(isEmpty)
            binding.textViewAcceptApplyAcademyNoResult.visibility = View.VISIBLE
        else
            binding.textViewAcceptApplyAcademyNoResult.visibility = View.GONE
    }
}