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
import com.harry.pullgo.databinding.FragmentManageClassroomManageRequestsBinding
import com.harry.pullgo.ui.dialog.FragmentManageClassroomStudentRequestDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ManageRequestsFragment(private val selectedClassroom: Classroom): Fragment() {
    private val binding by lazy{FragmentManageClassroomManageRequestsBinding.inflate(layoutInflater)}

    private lateinit var viewModel: ManageClassroomDetailsViewModel

    private var selectedStudent: Student? = null
    private var selectedTeacher: Teacher? = null

    private val client by lazy{RetrofitClient.getApiService()}

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

    private fun refreshAdapter(isTeacher: Boolean){
        if(isTeacher)
            viewModel.requestGetTeachersRequestApplyClassroom(selectedClassroom.id!!)
        else
            viewModel.requestGetStudentsRequestApplyClassroom(selectedClassroom.id!!)
    }

    private fun initViewModel(){
        viewModel = ViewModelProvider(requireActivity()).get(ManageClassroomDetailsViewModel::class.java)

        viewModel.studentsRequestApplyClassroom.observe(requireActivity()){
            displayStudentRequests()
            viewModel.requestGetStudentsAppliedClassroom(selectedClassroom.id!!)
        }

        viewModel.teachersRequestApplyClassroom.observe(requireActivity()){
            displayTeacherRequests()
            viewModel.requestGetTeachersAppliedClassroom(selectedClassroom.id!!)
        }

        binding.recyclerViewManageClassroomRequest.layoutManager = LinearLayoutManager(requireContext())

        refreshAdapter(false)
    }

    private fun displayStudentRequests(){
        val data = viewModel.studentsRequestApplyClassroom.value

        val adapter = data?.let {
            StudentApplyAdapter(it,true)
        }

        if (adapter != null) {
            adapter.studentClickListener = object: OnStudentClick {
                override fun onBackgroundClick(view: View, student: Student?) {
                    selectedStudent = student

                    val dialog = FragmentManageClassroomStudentRequestDialog(student!!)
                    dialog.show(parentFragmentManager, FragmentManageClassroomStudentRequestDialog.TAG_MANAGE_STUDENT_DIALOG)
                }

                override fun onApplyButtonClick(view: View, student: Student?) {
                    selectedStudent = student
                    acceptStudent(student!!)
                }

                override fun onRemoveButtonClick(view: View, student: Student?) {
                    removeStudentRequest(student!!)
                }
            }
        }
        binding.recyclerViewManageClassroomRequest.adapter = adapter

        showNoResultText(data?.isEmpty() == true)
    }

    private fun displayTeacherRequests(){
        val data = viewModel.teachersRequestApplyClassroom.value

        val adapter = data?.let {
            TeacherApplyAdapter(it,true)
        }

        if (adapter != null) {
            adapter.teacherClickListener = object: OnTeacherClick{
                override fun onBackgroundClick(view: View, teacher: Teacher?) {
                }

                override fun onApplyButtonClick(view: View, teacher: Teacher?) {
                    selectedTeacher = teacher
                    acceptTeacher(teacher!!)
                }

                override fun onRemoveButtonClick(view: View, teacher: Teacher?) {
                    removeTeacherRequest(teacher!!)
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
        client.acceptStudentApplyClassroom(selectedClassroom.id!!,student.id!!).enqueue(object: Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if(response.isSuccessful){
                    Snackbar.make(binding.root,"반 등록이 승인되었습니다",Snackbar.LENGTH_SHORT).show()
                    refreshAdapter(false)
                }else{
                    Snackbar.make(binding.root,"반 등록에 실패했습니다",Snackbar.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                Snackbar.make(binding.root,"서버와 연결에 실패했습니다",Snackbar.LENGTH_SHORT).show()
            }
        })
    }

    private fun acceptTeacher(teacher: Teacher){
        client.acceptTeacherApplyClassroom(selectedClassroom.id!!,teacher.id!!).enqueue(object: Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if(response.isSuccessful){
                    Snackbar.make(binding.root,"반 등록이 승인되었습니다",Snackbar.LENGTH_SHORT).show()
                    refreshAdapter(true)
                }else{
                    Snackbar.make(binding.root,"반 등록에 실패했습니다",Snackbar.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                Snackbar.make(binding.root,"서버와 연결에 실패했습니다",Snackbar.LENGTH_SHORT).show()
            }
        })
    }

    private fun removeStudentRequest(student: Student){
        client.removeStudentClassroomRequest(student.id!!,selectedClassroom.id!!).enqueue(object: Callback<Unit>{
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
        client.removeTeacherClassroomRequest(teacher.id!!,selectedClassroom.id!!).enqueue(object: Callback<Unit>{
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