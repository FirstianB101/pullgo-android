package com.harry.pullgo.ui.manageAcademy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.harry.pullgo.data.adapter.StudentManageAdapter
import com.harry.pullgo.data.adapter.TeacherManageAdapter
import com.harry.pullgo.data.api.OnDataChangedListener
import com.harry.pullgo.data.api.OnStudentClickListener
import com.harry.pullgo.data.api.OnTeacherClickListener
import com.harry.pullgo.data.models.Student
import com.harry.pullgo.data.models.Teacher
import com.harry.pullgo.data.repository.ManageAcademyRepository
import com.harry.pullgo.databinding.ActivityManageAcademyManagePeopleBinding
import com.harry.pullgo.ui.dialog.FragmentShowStudentInfoDialog
import com.harry.pullgo.ui.dialog.FragmentShowTeacherInfoDialog

class ManageAcademyManagePeopleActivity: AppCompatActivity(), OnDataChangedListener {
    private val binding by lazy{ActivityManageAcademyManagePeopleBinding.inflate(layoutInflater)}

    private val viewModel: ManageAcademyManagePeopleViewModel by viewModels{
        ManageAcademyManagePeopleViewModelFactory(ManageAcademyRepository())
    }

    private val selectedAcademyId by lazy{intent.getLongExtra("selectedAcademyId",-1L)}
    private val selectedAcademyName by lazy{intent.getStringExtra("selectedAcademyName")}

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

        supportFragmentManager.setFragmentResultListener("isKickedPerson",this){ _, bundle ->
            if(bundle.getString("isKicked") == "yes"){
                refreshAdapter(false)
                refreshAdapter(true)
            }
        }
    }

    private fun initViewModel(){
        viewModel.studentsAtAcademyRepository.observe(this){
            displayStudents()
        }

        viewModel.teachersAtAcademyRepository.observe(this){
            displayTeachers()
        }

        viewModel.getStudentsAtAcademy(selectedAcademyId)
    }

    private fun displayStudents(){
        val data = viewModel.studentsAtAcademyRepository.value

        val adapter = data?.let {
            StudentManageAdapter(it)
        }

        if (adapter != null) {
            adapter.studentClickListenerListener = object: OnStudentClickListener {
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

        showNoResultText(data?.isEmpty() == true)
    }

    private fun displayTeachers(){
        val data = viewModel.teachersAtAcademyRepository.value

        val adapter = data?.let {
            TeacherManageAdapter(it)
        }

        if (adapter != null) {
            adapter.teacherClickListenerListener = object: OnTeacherClickListener {
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

        showNoResultText(data?.isEmpty() == true)
    }

    private fun refreshAdapter(isTeacher: Boolean){
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

    override fun onChangeData(isTeacher: Boolean, isChanged: Boolean) {
        if(isChanged){
            refreshAdapter(isTeacher)
        }
    }
}