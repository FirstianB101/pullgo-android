package com.harry.pullgo.ui.commonFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayout
import com.harry.pullgo.application.PullgoApplication
import com.harry.pullgo.data.adapter.AcademyRequestAdapter
import com.harry.pullgo.data.adapter.ClassroomRequestAdapter
import com.harry.pullgo.data.api.OnAcademyRequestListener
import com.harry.pullgo.data.api.OnClassroomRequestListener
import com.harry.pullgo.data.models.Academy
import com.harry.pullgo.data.models.Classroom
import com.harry.pullgo.data.repository.ManageRequestRepository
import com.harry.pullgo.data.utils.Status
import com.harry.pullgo.databinding.FragmentManageRequestBinding
import com.harry.pullgo.ui.dialog.FragmentShowAcademyInfoDialog
import com.harry.pullgo.ui.dialog.FragmentShowClassroomInfoDialog
import com.harry.pullgo.ui.dialog.TwoButtonDialog
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ManageRequestFragment(private val isTeacher: Boolean): Fragment() {
    private val binding by lazy { FragmentManageRequestBinding.inflate(layoutInflater) }

    @Inject
    lateinit var app: PullgoApplication

    private val viewModel: ManageRequestViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)

        initialize()
        initViewModel()

        return binding.root
    }

    private fun initialize() {
        binding.tabLayoutManageRequest.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    when(tab.position){
                        0 -> {
                            refreshAdapter(false)
                        }
                        1 -> {
                            refreshAdapter(true)
                        }
                    }
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }
        })
    }

    private fun refreshAdapter(isClassroom: Boolean) {
        if (isClassroom) {
            if (isTeacher)
                viewModel.getTeacherApplyingClassroom(app.loginUser.teacher?.id!!)
            else
                viewModel.getStudentApplyingClassroom(app.loginUser.student?.id!!)
        } else {
            if (isTeacher)
                viewModel.getTeacherApplyingAcademy(app.loginUser.teacher?.id!!)
            else
                viewModel.getStudentApplyingAcademy(app.loginUser.student?.id!!)
        }
    }

    private fun initViewModel() {
        viewModel.applyingAcademyRepository.observe(requireActivity()) {
            when(it.status){
                Status.SUCCESS -> {
                    displayAcademyRequests()
                }
                Status.LOADING -> {}
                Status.ERROR -> {
                    Toast.makeText(requireContext(),"학원 목록을 불러올 수 없습니다(${it.message})",Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.applyingClassroomRepository.observe(requireActivity()) {
            when(it.status){
                Status.SUCCESS -> {
                    displayClassroomRequests()
                }
                Status.LOADING -> {}
                Status.ERROR -> {
                    Toast.makeText(requireContext(),"반 목록을 불러올 수 없습니다(${it.message})",Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.removeRequestMessage.observe(requireActivity()) {
            when(it.status){
                Status.SUCCESS -> {
                    Toast.makeText(requireContext(), "${it.data}", Toast.LENGTH_SHORT).show()
                    when (it.data) {
                        "학원 가입 요청을 제거했습니다" -> refreshAdapter(false)
                        "반 가입 요청을 제거했습니다" -> refreshAdapter(true)
                    }
                }
                Status.LOADING -> {}
                Status.ERROR -> {
                    Toast.makeText(requireContext(),"${it.data}(${it.message})",Toast.LENGTH_SHORT).show()
                }
            }
        }

        refreshAdapter(false)
    }

    private fun displayAcademyRequests() {
        val data = viewModel.applyingAcademyRepository.value?.data

        val adapter = data?.let {
            AcademyRequestAdapter(it)
        }

        if (adapter != null) {
            adapter.itemClickListener = object: OnAcademyRequestListener{
                override fun onAcademyClick(view: View, academy: Academy?) {
                    FragmentShowAcademyInfoDialog(academy!!)
                        .show(childFragmentManager,FragmentShowAcademyInfoDialog.TAG_ACADEMY_INFO_DIALOG)
                }

                override fun onRemoveRequest(view: View, academy: Academy?) {
                    showDeleteAcademyRequestDialog(academy)
                }
            }
        }
        binding.recyclerViewManageRequest.adapter = adapter

        showNoResultText(data?.isEmpty() == true)
    }

    private fun displayClassroomRequests() {
        val data = viewModel.applyingClassroomRepository.value?.data

        val adapter = data?.let {
            ClassroomRequestAdapter(it)
        }

        if(adapter != null){
            adapter.itemClickListener = object: OnClassroomRequestListener{
                override fun onClassroomClick(view: View, classroom: Classroom?) {
                    FragmentShowClassroomInfoDialog(classroom!!)
                        .show(childFragmentManager,FragmentShowClassroomInfoDialog.TAG_CLASSROOM_INFO_DIALOG)
                }

                override fun onRemoveRequest(view: View, classroom: Classroom?) {
                    showDeleteClassroomRequestDialog(classroom)
                }
            }
        }
        binding.recyclerViewManageRequest.adapter = adapter

        showNoResultText(data?.isEmpty() == true)
    }

    private fun showDeleteAcademyRequestDialog(academy: Academy?){
        val dialog = TwoButtonDialog(requireContext())
        dialog.leftClickListener = object: TwoButtonDialog.TwoButtonDialogLeftClickListener{
            override fun onLeftClicked() {
                if(isTeacher)
                    viewModel.removeTeacherApplyingAcademy(app.loginUser.teacher?.id!!,academy?.id!!)
                else
                    viewModel.removeStudentApplyingAcademy(app.loginUser.student?.id!!,academy?.id!!)
            }
        }
        dialog.start("요청 삭제","${academy?.name} 학원 요청을 삭제하시겠습니까?",
            "삭제하기","취소")
    }

    private fun showDeleteClassroomRequestDialog(classroom: Classroom?){
        val dialog = TwoButtonDialog(requireContext())
        dialog.leftClickListener = object: TwoButtonDialog.TwoButtonDialogLeftClickListener{
            override fun onLeftClicked() {
                if(isTeacher)
                    viewModel.removeTeacherApplyingClassroom(app.loginUser.teacher?.id!!,classroom?.id!!)
                else
                    viewModel.removeStudentApplyingClassroom(app.loginUser.student?.id!!,classroom?.id!!)
            }
        }
        dialog.start("요청 삭제","${classroom?.name?.split(';')?.get(0)} 반 요청을 삭제하시겠습니까?",
            "삭제하기","취소")
    }

    private fun showNoResultText(isEmpty: Boolean) {
        if (isEmpty)
            binding.textViewManageRequestNoResult.visibility = View.VISIBLE
        else
            binding.textViewManageRequestNoResult.visibility = View.GONE
    }
}
