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
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.harry.pullgo.application.PullgoApplication
import com.harry.pullgo.data.api.OnClassroomClickListener
import com.harry.pullgo.data.models.Classroom
import com.harry.pullgo.data.repository.ClassroomsRepository
import com.harry.pullgo.databinding.FragmentManageClassroomBinding
import com.harry.pullgo.ui.manageClassroom.FragmentCreateClassroomDialog
import com.harry.pullgo.ui.manageClassroom.ManageClassroomActivity

class TeacherManageClassroomFragment: Fragment() {
    private val binding by lazy{FragmentManageClassroomBinding.inflate(layoutInflater)}

    private val viewModel: ManageClassroomViewModel by activityViewModels{
        ManageClassroomViewModelFactory(ClassroomsRepository(requireContext(), app.loginUser.token))
    }

    private var selectedClassroom: Classroom? = null
    private var buttonPushed = false

    private lateinit var startForResult: ActivityResultLauncher<Intent>

    private val app: PullgoApplication by lazy{requireActivity().application as PullgoApplication }

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
            startManageClassroomActivity()
        }

        viewModel.getClassroomRepositories.observe(requireActivity()){
            displayClassrooms()
        }

        viewModel.academiesForSpinnerRepository.observe(requireActivity()){
            if(buttonPushed)
                makeClassroom()
            buttonPushed = false
        }

        viewModel.requestGetClassrooms(app.loginUser.teacher?.id!!)
        app.showLoadingDialog(childFragmentManager)
    }

    private fun displayClassrooms(){
        val data = viewModel.getClassroomRepositories.value

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
        app.dismissLoadingDialog()
    }

    private fun startManageClassroomActivity(){
        val intent = Intent(requireContext(),ManageClassroomActivity::class.java)

        val classroom = viewModel.selectedClassroom.value

        intent.putExtra("selectedClassroomId",classroom?.id)
        intent.putExtra("selectedClassroomAcademyId",classroom?.academyId)
        intent.putExtra("selectedClassroomName",classroom?.name)

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
        val academies = viewModel.academiesForSpinnerRepository.value
        FragmentCreateClassroomDialog(academies!!).show(childFragmentManager,
            FragmentCreateClassroomDialog.TAG_LESSON_INFO_DIALOG)
    }
}