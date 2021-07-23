package com.harry.pullgo.ui.teacherFragment

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TeacherAcceptApplyAcademyFragment: Fragment() {
    private val binding by lazy{FragmentAcceptApplyAcademyBinding.inflate(layoutInflater)}

    private lateinit var viewModel: TeacherAcceptApplyAcademyViewModel
    private lateinit var repository: AcceptApplyAcademyRepository

    private var selectedStudent: Student? = null
    private var selectedTeacher: Teacher? = null
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

    private fun refreshAdapter(isChecked: Boolean){
        if(isChecked)
            viewModel.requestGetTeachers(selectedAcademy?.id!!)
        else
            viewModel.requestGetStudents(selectedAcademy?.id!!)
    }

    private fun initViewModel(){
        repository = AcceptApplyAcademyRepository()

        val factory = TeacherAcceptApplyAcademyViewModelFactory(repository)
        viewModel = ViewModelProvider(this,factory).get(TeacherAcceptApplyAcademyViewModel::class.java)

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
            StudentApplyAdapter(it)
        }

        if (adapter != null) {
            adapter.studentClickListener = object: OnStudentClick{
                override fun onStudentClick(view: View, student: Student?) {
                    selectedStudent = student
                    doStudentApplyProcess()
                }
            }
        }
        binding.recyclerViewAcceptApplyAcademy.adapter = adapter

        showNoResultText(data?.isEmpty() == true)
    }

    private fun displayTeachers(){
        val data = viewModel.teacherAppliedAcademy.value

        val adapter = data?.let {
            TeacherApplyAdapter(it)
        }

        if (adapter != null) {
            adapter.teacherClickListener = object: OnTeacherClick {
                override fun onTeacherClick(view: View, teacher: Teacher?) {
                    selectedTeacher = teacher
                    doTeacherApplyProcess()
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
        val client = RetrofitClient.getApiService()
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
        val client = RetrofitClient.getApiService()
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
}