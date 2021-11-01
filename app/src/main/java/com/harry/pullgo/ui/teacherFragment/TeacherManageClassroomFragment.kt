package com.harry.pullgo.ui.teacherFragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.harry.pullgo.application.PullgoApplication
import com.harry.pullgo.data.adapter.ManageClassroomAdapter
import com.harry.pullgo.data.api.OnClassroomClickListener
import com.harry.pullgo.data.models.Classroom
import com.harry.pullgo.data.utils.Status
import com.harry.pullgo.databinding.FragmentManageClassroomBinding
import com.harry.pullgo.ui.manageClassroom.FragmentCreateClassroomDialog
import com.harry.pullgo.ui.manageClassroom.ManageClassroomActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TeacherManageClassroomFragment: Fragment() {
    private val binding by lazy{FragmentManageClassroomBinding.inflate(layoutInflater)}

    @Inject
    lateinit var app: PullgoApplication

    private val viewModel: TeacherManageClassroomViewModel by activityViewModels()

    private var selectedClassroom: Classroom? = null
    private var buttonPushed = false

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
            buttonPushed = true
            viewModel.requestGetAcademiesForNewClassroom(app.loginUser.teacher?.id!!)
        }

        binding.floatingActionButtonManageClassroom.setOnClickListener {
            buttonPushed = true
            viewModel.requestGetAcademiesForNewClassroom(app.loginUser.teacher?.id!!)
        }

        startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if(it.resultCode == Activity.RESULT_OK){
                if(it.data?.getStringExtra("finishedFragment") == "editClassroom"){
                    viewModel.requestGetClassrooms(app.loginUser.teacher?.id!!)
                }
            }
        }

        setFragmentResultListener("createNewClassroom"){ _, bundle ->
            if(bundle.getString("isCreated") == "yes"){
                viewModel.requestGetClassrooms(app.loginUser.teacher?.id!!)
            }
        }
    }

    private fun setViewModel(){
        viewModel.selectedClassroom.observe(requireActivity()){
            when(it.status){
                Status.SUCCESS -> {
                    startManageClassroomActivity()
                }
                Status.LOADING -> {}
                Status.ERROR -> {
                    Toast.makeText(requireContext(),"해당 반 정보를 불러올 수 없습니다(${it.message})",Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.getClassroomRepositories.observe(requireActivity()){
            when(it.status){
                Status.SUCCESS -> {
                    displayClassrooms()
                }
                Status.LOADING -> {}
                Status.ERROR -> {
                    Toast.makeText(requireContext(),"반 정보를 불러올 수 없습니다(${it.message})",Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.academiesForSpinnerRepository.observe(requireActivity()){
            when(it.status){
                Status.SUCCESS -> {
                    if(buttonPushed)
                        makeClassroom()
                    buttonPushed = false
                }
                Status.LOADING -> {}
                Status.ERROR -> {
                    Toast.makeText(requireContext(),"학원 정보를 불러올 수 없습니다(${it.message})",Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.requestGetClassrooms(app.loginUser.teacher?.id!!)
    }

    private fun displayClassrooms(){
        val data = viewModel.getClassroomRepositories.value?.data

        val classroomAdapter = data?.let {
            ManageClassroomAdapter(it)
        }

        if(classroomAdapter != null){
            classroomAdapter.itemClickListenerListener = object: OnClassroomClickListener{
                override fun onClassroomClick(view: View, classroom: Classroom?) {
                    selectedClassroom = classroom
                    viewModel.requestGetClassroomById(classroom?.id!!)
                }
            }
        }

        hideLayout(data?.isEmpty() == true)

        binding.recyclerViewManageClassroom.adapter = classroomAdapter
    }

    private fun startManageClassroomActivity(){
        val intent = Intent(requireContext(),ManageClassroomActivity::class.java)

        val classroom = viewModel.selectedClassroom.value?.data

        intent.putExtra("selectedClassroom",classroom)

        startForResult.launch(intent)
    }

    private fun hideLayout(isEmpty: Boolean){
        if(isEmpty){
            binding.layoutManageClassroomNoClassroom.visibility = View.VISIBLE
        }else{
            binding.layoutManageClassroomNoClassroom.visibility = View.GONE
        }
    }

    private fun makeClassroom(){
        val academies = viewModel.academiesForSpinnerRepository.value?.data
        FragmentCreateClassroomDialog(academies!!).show(childFragmentManager,
            FragmentCreateClassroomDialog.TAG_LESSON_INFO_DIALOG)
    }
}