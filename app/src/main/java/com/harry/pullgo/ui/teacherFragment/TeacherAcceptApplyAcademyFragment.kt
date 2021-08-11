package com.harry.pullgo.ui.teacherFragment

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.harry.pullgo.data.adapter.StudentApplyAdapter
import com.harry.pullgo.data.adapter.TeacherApplyAdapter
import com.harry.pullgo.data.api.OnStudentClick
import com.harry.pullgo.data.api.OnTeacherClick
import com.harry.pullgo.data.api.RetrofitClient
import com.harry.pullgo.data.objects.Academy
import com.harry.pullgo.data.objects.LoginInfo
import com.harry.pullgo.data.objects.Student
import com.harry.pullgo.data.objects.Teacher
import com.harry.pullgo.data.repository.AcceptApplyAcademyRepository
import com.harry.pullgo.databinding.FragmentAcceptApplyAcademyBinding
import com.harry.pullgo.ui.dialog.FragmentShowStudentInfoDialog
import com.harry.pullgo.ui.dialog.FragmentShowTeacherInfoDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TeacherAcceptApplyAcademyFragment: Fragment() {
    private val binding by lazy{FragmentAcceptApplyAcademyBinding.inflate(layoutInflater)}

    private val viewModel: TeacherAcceptApplyAcademyViewModel by viewModels{TeacherAcceptApplyAcademyViewModelFactory(AcceptApplyAcademyRepository())}

    private var selectedStudent: Student? = null
    private var selectedTeacher: Teacher? = null
    private var selectedAcademy: Academy? = null

    private val client by lazy{RetrofitClient.getApiService()}

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
            displayStudents()
        }

        viewModel.teacherAppliedAcademy.observe(requireActivity()){
            displayTeachers()
        }

        viewModel.academyRepositories.observe(requireActivity()){
            setSpinnerItems()
        }

        viewModel.requestTeacherAcademies(LoginInfo.loginTeacher?.id!!)
    }

    private fun setSpinnerItems(){
        val academies = viewModel.academyRepositories.value!!

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
        val data = viewModel.studentsAppliedAcademy.value

        val adapter = data?.let {
            StudentApplyAdapter(it,true)
        }

        if (adapter != null) {
            adapter.studentClickListener = object: OnStudentClick {
                override fun onBackgroundClick(view: View, student: Student?) {
                    selectedStudent = student

                    FragmentShowStudentInfoDialog(student!!).show(parentFragmentManager, FragmentShowStudentInfoDialog.TAG_STUDENT_INFO_DIALOG)
                }

                override fun onApplyButtonClick(view: View, student: Student?) {
                    selectedStudent = student
                    doStudentApplyProcess()
                }

                override fun onRemoveButtonClick(view: View, student: Student?) {
                    removeStudentRequest(student!!)
                }
            }
        }
        binding.recyclerViewAcceptApplyAcademy.adapter = adapter

        showNoResultText(data?.isEmpty() == true)
    }

    private fun displayTeachers(){
        val data = viewModel.teacherAppliedAcademy.value

        val adapter = data?.let {
            TeacherApplyAdapter(it,true)
        }

        if (adapter != null) {
            adapter.teacherClickListener = object: OnTeacherClick {
                override fun onBackgroundClick(view: View, teacher: Teacher?) {
                    selectedTeacher = teacher

                    FragmentShowTeacherInfoDialog(teacher!!).show(parentFragmentManager, FragmentShowStudentInfoDialog.TAG_STUDENT_INFO_DIALOG)
                }

                override fun onApplyButtonClick(view: View, teacher: Teacher?) {
                    selectedTeacher = teacher
                    doTeacherApplyProcess()
                }

                override fun onRemoveButtonClick(view: View, teacher: Teacher?) {
                    removeTeacherRequest(teacher!!)
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

    private fun doStudentApplyProcess(){
        client.acceptStudentApplyAcademy(selectedAcademy?.id!!,selectedStudent?.id!!).enqueue(object: Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if(response.isSuccessful){
                    Snackbar.make(binding.root,"요청 승인에 성공하였습니다",Snackbar.LENGTH_SHORT).show()
                    viewModel.requestGetStudents(selectedAcademy?.id!!)
                }else{
                    Snackbar.make(binding.root,"요청 승인에 실패하였습니다",Snackbar.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                Snackbar.make(binding.root,"서버와 연결에 실패했습니다",Snackbar.LENGTH_SHORT).show()
            }
        })
    }

    private fun doTeacherApplyProcess(){
        client.acceptTeacherApplyAcademy(selectedAcademy?.id!!,selectedTeacher?.id!!).enqueue(object: Callback<Unit>{
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if(response.isSuccessful){
                    Snackbar.make(binding.root,"요청 승인에 성공하였습니다",Snackbar.LENGTH_SHORT).show()
                    viewModel.requestGetTeachers(selectedAcademy?.id!!)
                }else{
                    Snackbar.make(binding.root,"요청 승인에 실패하였습니다",Snackbar.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                Snackbar.make(binding.root,"서버와 연결에 실패했습니다",Snackbar.LENGTH_SHORT).show()
            }
        })
    }

    private fun removeStudentRequest(student: Student){
        client.removeStudentAcademyRequest(student.id!!,selectedAcademy?.id!!).enqueue(object: Callback<Unit>{
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if(response.isSuccessful){
                    Snackbar.make(binding.root,"요청이 삭제되었습니다",Snackbar.LENGTH_SHORT).show()
                    refreshAdapter(false)
                }else{
                    Snackbar.make(binding.root,"요청을 삭제하지 못했습니다",Snackbar.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                Snackbar.make(binding.root,"서버와 연결에 실패했습니다",Snackbar.LENGTH_SHORT).show()
            }
        })
    }

    private fun removeTeacherRequest(teacher: Teacher){
        client.removeTeacherAcademyRequest(teacher.id!!,selectedAcademy?.id!!).enqueue(object: Callback<Unit>{
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if(response.isSuccessful){
                    Snackbar.make(binding.root,"요청이 삭제되었습니다",Snackbar.LENGTH_SHORT).show()
                    refreshAdapter(true)
                }else{
                    Snackbar.make(binding.root,"요청을 삭제하지 못했습니다",Snackbar.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                Snackbar.make(binding.root,"서버와 연결에 실패했습니다",Snackbar.LENGTH_SHORT).show()
            }
        })
    }
}