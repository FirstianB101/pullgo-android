package com.harry.pullgo.ui.teacherFragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.harry.pullgo.data.api.OnClassroomClick
import com.harry.pullgo.data.objects.Classroom
import com.harry.pullgo.data.objects.LoginInfo
import com.harry.pullgo.data.repository.ClassroomsRepository
import com.harry.pullgo.databinding.FragmentManageClassroomBinding
import com.harry.pullgo.ui.calendar.CalendarFragment
import com.harry.pullgo.ui.commonFragment.ChangeInfoCheckPwFragment
import com.harry.pullgo.ui.main.TeacherMainActivity
import com.harry.pullgo.ui.manageClassroom.ManageClassroomActivity

class TeacherManageClassroomFragment: Fragment() {
    private val binding by lazy{FragmentManageClassroomBinding.inflate(layoutInflater)}

    private lateinit var viewModel: ManageClassroomViewModel
    private lateinit var repository: ClassroomsRepository

    private var selectedClassroom: Classroom? = null

    private lateinit var startForResult: ActivityResultLauncher<Intent>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)

        initialize()
        setViewModel()

        return binding.root
    }

    private fun initialize(){
        binding.recyclerViewManageClassroom.layoutManager = LinearLayoutManager(requireContext())

        binding.buttonCreateNewClassroom.setOnClickListener {

        }

        binding.floatingActionButtonManageClassroom.setOnClickListener {

        }

        startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if(it.resultCode == Activity.RESULT_OK){
                if(it.data?.getStringExtra("finishedFragment") == "editClassroom"){
                    viewModel.requestGetClassrooms(LoginInfo.loginTeacher?.id!!)
                }
            }
        }
    }

    private fun setViewModel(){
        repository = ClassroomsRepository()

        val viewModelFactory = ManageClassroomViewModelFactory(repository)
        viewModel = ViewModelProvider(requireActivity(),viewModelFactory).get(ManageClassroomViewModel::class.java)

        viewModel.getClassroomRepositories.observe(requireActivity()){
            displayClassrooms()
        }

        viewModel.requestGetClassrooms(LoginInfo.loginTeacher?.id!!)
    }

    private fun displayClassrooms(){
        val data = viewModel.getClassroomRepositories.value

        val classroomAdapter = data?.let {
            ManageClassroomAdapter(it)
        }

        if(classroomAdapter != null){
            classroomAdapter.itemClickListener = object: OnClassroomClick{
                override fun onClassroomClick(view: View, classroom: Classroom?) {
                   startManageClassroomActivity(classroom)
                }
            }
        }

        hideLayout(data?.isEmpty() == true)

        binding.recyclerViewManageClassroom.adapter = classroomAdapter
    }

    private fun startManageClassroomActivity(classroom: Classroom?){
        selectedClassroom = classroom

        val intent = Intent(requireContext(),ManageClassroomActivity::class.java)

        intent.putExtra("selectedClassroomId",selectedClassroom?.id)
        intent.putExtra("selectedClassroomAcademyId",selectedClassroom?.academyId)
        intent.putExtra("selectedClassroomName",selectedClassroom?.name)

        startForResult.launch(intent)
    }

    private fun hideLayout(isEmpty: Boolean){
        if(isEmpty){
            binding.layoutManageClassroomNoClassroom.visibility = View.VISIBLE
        }else{
            binding.layoutManageClassroomNoClassroom.visibility = View.GONE
        }
    }
}