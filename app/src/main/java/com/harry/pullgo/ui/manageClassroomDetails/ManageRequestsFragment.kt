package com.harry.pullgo.ui.manageClassroomDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.harry.pullgo.data.adapter.StudentApplyAdapter
import com.harry.pullgo.data.adapter.TeacherApplyAdapter
import com.harry.pullgo.data.api.OnStudentClick
import com.harry.pullgo.data.api.OnTeacherClick
import com.harry.pullgo.data.api.RetrofitClient
import com.harry.pullgo.data.objects.Classroom
import com.harry.pullgo.data.objects.Student
import com.harry.pullgo.data.objects.Teacher
import com.harry.pullgo.data.repository.ManageClassroomDetailsRepository
import com.harry.pullgo.databinding.FragmentManageClassroomManageRequestsBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ManageRequestsFragment(private val selectedClassroom: Classroom): Fragment() {
    private val binding by lazy{FragmentManageClassroomManageRequestsBinding.inflate(layoutInflater)}

    private lateinit var viewModel: ManageClassroomDetailsViewModel

    private var selectedStudent: Student? = null
    private var selectedTeacher: Teacher? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)

        initialize()
        initViewModel()

        return binding.root
    }

    private fun initialize(){
        binding.switchManageClassroomRequest.setOnCheckedChangeListener { _, isChecked ->
            binding.textViewManageClassroomRequestSwitch.text = if(isChecked) "선생님" else "학생"
            refreshAdapter(isChecked)
        }
    }

    private fun refreshAdapter(isChecked: Boolean){
        if(isChecked)
            viewModel.requestGetTeachersRequestApplyClassroom(selectedClassroom.id!!)
        else
            viewModel.requestGetStudentsRequestApplyClassroom(selectedClassroom.id!!)
    }

    private fun initViewModel(){
        viewModel = ViewModelProvider(requireActivity()).get(ManageClassroomDetailsViewModel::class.java)

        viewModel.studentsRequestApplyClassroom.observe(requireActivity()){
            displayStudentRequests()
        }

        viewModel.teachersRequestApplyClassroom.observe(requireActivity()){
            displayTeacherRequests()
        }

        binding.recyclerViewManageClassroomRequest.layoutManager = LinearLayoutManager(requireContext())

        refreshAdapter(false)
    }

    private fun displayStudentRequests(){
        val data = viewModel.studentsRequestApplyClassroom.value

        val adapter = data?.let {
            StudentApplyAdapter(it)
        }

        if (adapter != null) {
            adapter.studentClickListener = object: OnStudentClick {
                override fun onStudentClick(view: View, student: Student?) {
                    selectedStudent = student
                    acceptStudent(student!!)
                }
            }
        }
        binding.recyclerViewManageClassroomRequest.adapter = adapter

        showNoResultText(data?.isEmpty() == true)
    }

    private fun displayTeacherRequests(){
        val data = viewModel.teachersRequestApplyClassroom.value

        val adapter = data?.let {
            TeacherApplyAdapter(it)
        }

        if (adapter != null) {
            adapter.teacherClickListener = object: OnTeacherClick {
                override fun onTeacherClick(view: View, teacher: Teacher?) {
                    selectedTeacher = teacher
                    acceptTeacher(teacher!!)
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

    private fun acceptStudent(student: Student){
        val client = RetrofitClient.getApiService()
        client.acceptStudentApplyClassroom(selectedClassroom.id!!,student.id!!).enqueue(object: Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if(response.isSuccessful){
                    Snackbar.make(binding.root,"반 등록이 승인되었습니다",Snackbar.LENGTH_SHORT).show()
                    refreshAdapter(false)
                }else{
                    Snackbar.make(binding.root,"반 등록이 실패했습니다",Snackbar.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                Snackbar.make(binding.root,"서버와 연결에 실패했습니다",Snackbar.LENGTH_SHORT).show()
            }
        })
    }

    private fun acceptTeacher(teacher: Teacher){
        val client = RetrofitClient.getApiService()
        client.acceptTeacherApplyClassroom(selectedClassroom.id!!,teacher.id!!).enqueue(object: Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if(response.isSuccessful){
                    Snackbar.make(binding.root,"반 등록이 승인되었습니다",Snackbar.LENGTH_SHORT).show()
                    refreshAdapter(true)
                }else{
                    Snackbar.make(binding.root,"반 등록이 실패했습니다",Snackbar.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                Snackbar.make(binding.root,"서버와 연결에 실패했습니다",Snackbar.LENGTH_SHORT).show()
            }
        })
    }
}