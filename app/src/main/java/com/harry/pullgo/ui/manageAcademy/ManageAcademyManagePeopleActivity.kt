package com.harry.pullgo.ui.manageAcademy

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.harry.pullgo.application.PullgoApplication
import com.harry.pullgo.data.adapter.StudentManageAdapter
import com.harry.pullgo.data.adapter.TeacherManageAdapter
import com.harry.pullgo.data.api.OnDataChangedListener
import com.harry.pullgo.data.api.OnStudentClickListener
import com.harry.pullgo.data.api.OnTeacherClickListener
import com.harry.pullgo.data.models.Student
import com.harry.pullgo.data.models.Teacher
import com.harry.pullgo.data.utils.Status
import com.harry.pullgo.databinding.ActivityManageAcademyManagePeopleBinding
import com.harry.pullgo.ui.dialog.FragmentShowStudentInfoDialog
import com.harry.pullgo.ui.dialog.FragmentShowTeacherInfoDialog
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ManageAcademyManagePeopleActivity: AppCompatActivity(), OnDataChangedListener {
    private val binding by lazy{ActivityManageAcademyManagePeopleBinding.inflate(layoutInflater)}

    @Inject
    lateinit var app: PullgoApplication

    private val viewModel: ManageAcademyManagePeopleViewModel by viewModels()

    private val selectedAcademyId by lazy{ intent.getLongExtra("selectedAcademyId",-1L) }
    private val selectedAcademyName by lazy{ intent.getStringExtra("selectedAcademyName") }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initialize()
        initViewModel()
    }

    private fun initialize(){
        binding.recyclerViewManageAcademyManagePeople.layoutManager = LinearLayoutManager(this)

        binding.switchManageAcademyManagePeople.setOnCheckedChangeListener { _, isChecked ->
            binding.textViewManageAcademyManagePeopleSwitch.text = if(isChecked) "선생님" else "학생"
            refreshAdapter(isChecked)
        }
    }

    private fun initViewModel(){
        viewModel.studentsAtAcademyRepository.observe(this){
            when(it.status){
                Status.SUCCESS -> {
                    displayStudents()
                }
                Status.LOADING -> {}
                Status.ERROR -> {
                    Toast.makeText(this,"학원 정보를 불러오지 못했습니다(${it.message})",Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.teachersAtAcademyRepository.observe(this){
            when(it.status){
                Status.SUCCESS -> {
                    displayTeachers()
                }
                Status.LOADING -> {}
                Status.ERROR -> {
                    Toast.makeText(this,"학원 정보를 불러오지 못했습니다(${it.message})",Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.getStudentsAtAcademy(selectedAcademyId)
    }

    private fun displayStudents(){
        val data = viewModel.studentsAtAcademyRepository.value?.data

        val adapter = data?.let {
            StudentManageAdapter(it)
        }

        if (adapter != null) {
            adapter.studentClickListener = object: OnStudentClickListener {
                override fun onBackgroundClick(view: View, student: Student?) {
                    FragmentShowStudentInfoDialog(student!!)
                        .show(supportFragmentManager,FragmentShowStudentInfoDialog.TAG_STUDENT_INFO_DIALOG)
                }

                override fun onApplyButtonClick(view: View, student: Student?) {
                }

                override fun onRemoveButtonClick(view: View, student: Student?) {
                    FragmentKickStudentDialog(student!!,selectedAcademyName,selectedAcademyId)
                        .show(supportFragmentManager, FragmentKickStudentDialog.TAG_KICK_STUDENT_DIALOG)
                }
            }
        }
        binding.recyclerViewManageAcademyManagePeople.adapter = adapter

        app.dismissLoadingDialog()

        showNoResultText(data?.isEmpty() == true)
    }

    private fun displayTeachers(){
        val data = viewModel.teachersAtAcademyRepository.value?.data

        val adapter = data?.let {
            TeacherManageAdapter(it)
        }

        if (adapter != null) {
            adapter.teacherClickListener = object: OnTeacherClickListener {
                override fun onBackgroundClick(view: View, teacher: Teacher?) {
                    FragmentShowTeacherInfoDialog(teacher!!)
                        .show(supportFragmentManager,FragmentShowTeacherInfoDialog.TAG_TEACHER_INFO_DIALOG)
                }

                override fun onApplyButtonClick(view: View, teacher: Teacher?) {
                }

                override fun onRemoveButtonClick(view: View, teacher: Teacher?) {
                    FragmentKickTeacherDialog(teacher!!,selectedAcademyName,selectedAcademyId)
                        .show(supportFragmentManager, FragmentKickTeacherDialog.TAG_KICK_TEACHER_DIALOG)
                }
            }
        }
        binding.recyclerViewManageAcademyManagePeople.adapter = adapter

        app.dismissLoadingDialog()

        showNoResultText(data?.isEmpty() == true)
    }

    private fun refreshAdapter(isTeacher: Boolean){
        app.showLoadingDialog(supportFragmentManager)
        if(isTeacher)
            viewModel.getTeachersAtAcademy(selectedAcademyId)
        else
            viewModel.getStudentsAtAcademy(selectedAcademyId)
    }

    private fun showNoResultText(isEmpty: Boolean){
        if(isEmpty)
            binding.textViewManagePeopleNoPeople.visibility = View.VISIBLE
        else
            binding.textViewManagePeopleNoPeople.visibility = View.GONE
    }

    override fun onChangeData(isTeacher: Boolean) {
        refreshAdapter(isTeacher)
    }
}