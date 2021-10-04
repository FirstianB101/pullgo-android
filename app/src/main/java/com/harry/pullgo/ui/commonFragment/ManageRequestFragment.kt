package com.harry.pullgo.ui.commonFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.harry.pullgo.data.adapter.AcademyRequestAdapter
import com.harry.pullgo.data.adapter.ClassroomRequestAdapter
import com.harry.pullgo.data.api.OnAcademyRequestListener
import com.harry.pullgo.data.api.OnClassroomRequestListener
import com.harry.pullgo.data.models.Academy
import com.harry.pullgo.data.models.Classroom
import com.harry.pullgo.data.objects.LoginInfo
import com.harry.pullgo.data.repository.ManageRequestRepository
import com.harry.pullgo.databinding.FragmentManageRequestBinding

class ManageRequestFragment(private val isTeacher: Boolean): Fragment() {
    private val binding by lazy { FragmentManageRequestBinding.inflate(layoutInflater) }

    private val viewModel: ManageRequestViewModel by viewModels {
        ManageRequestViewModelFactory(ManageRequestRepository(requireContext()))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)

        initialize()
        initViewModel()

        return binding.root
    }

    private fun initialize() {
        binding.switchManageRequest.setOnCheckedChangeListener { _, isChecked ->
            binding.textViewManageRequestSwitch.text = if (isChecked) "반 요청" else "학원 요청"
            refreshAdapter(isChecked)
        }
    }

    private fun refreshAdapter(isClassroom: Boolean) {
        if (isClassroom) {
            if (isTeacher)
                viewModel.getTeacherApplyingClassroom(LoginInfo.user?.teacher?.id!!)
            else
                viewModel.getStudentApplyingClassroom(LoginInfo.user?.student?.id!!)
        } else {
            if (isTeacher)
                viewModel.getTeacherApplyingAcademy(LoginInfo.user?.teacher?.id!!)
            else
                viewModel.getStudentApplyingAcademy(LoginInfo.user?.student?.id!!)
        }
    }

    private fun initViewModel() {
        viewModel.applyingAcademyRepository.observe(requireActivity()) {
            displayAcademyRequests()
        }

        viewModel.applyingClassroomRepository.observe(requireActivity()) {
            displayClassroomRequests()
        }

        viewModel.removeRequestMessage.observe(requireActivity()) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            when (it) {
                "학원 가입 요청을 제거했습니다" -> refreshAdapter(true)
                "반 가입 요청을 제거했습니다" -> refreshAdapter(false)
            }
        }

        refreshAdapter(false)
    }

    private fun displayAcademyRequests() {
        val data = viewModel.applyingAcademyRepository.value

        val adapter = data?.let {
            AcademyRequestAdapter(it)
        }

        if (adapter != null) {
            adapter.itemClickListener = object: OnAcademyRequestListener{
                override fun onAcademyClick(view: View, academy: Academy?) {
                   // val dialog = FragmentShowStudentInfoDialog(student!!)
                    //dialog.show(parentFragmentManager, FragmentShowStudentInfoDialog.TAG_STUDENT_INFO_DIALOG)
                }

                override fun onRemoveRequest(view: View, academy: Academy?) {
                    if(isTeacher)
                        viewModel.removeTeacherApplyingAcademy(LoginInfo.user?.teacher?.id!!,academy?.id!!)
                    else
                        viewModel.removeStudentApplyingAcademy(LoginInfo.user?.student?.id!!,academy?.id!!)
                }
            }
        }
        binding.recyclerViewManageRequest.adapter = adapter

        showNoResultText(data?.isEmpty() == true)
    }

    private fun displayClassroomRequests() {
        val data = viewModel.applyingClassroomRepository.value

        val adapter = data?.let {
            ClassroomRequestAdapter(it)
        }

        if(adapter != null){
            adapter.itemClickListener = object: OnClassroomRequestListener{
                override fun onClassroomClick(view: View, classroom: Classroom?) {

                }

                override fun onRemoveRequest(view: View, classroom: Classroom?) {
                    if(isTeacher)
                        viewModel.removeTeacherApplyingClassroom(LoginInfo.user?.teacher?.id!!,classroom?.id!!)
                    else
                        viewModel.removeStudentApplyingClassroom(LoginInfo.user?.student?.id!!,classroom?.id!!)
                }
            }
        }
        binding.recyclerViewManageRequest.adapter = adapter

        showNoResultText(data?.isEmpty() == true)
    }

    private fun showNoResultText(isEmpty: Boolean) {
        if (isEmpty)
            binding.textViewManageRequestNoResult.visibility = View.VISIBLE
        else
            binding.textViewManageRequestNoResult.visibility = View.GONE
    }
}
