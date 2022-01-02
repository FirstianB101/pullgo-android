package com.ich.pullgo.ui.manageClassroom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.ich.pullgo.data.models.Exam
import com.ich.pullgo.data.utils.Status
import com.ich.pullgo.databinding.FragmentDetailedManageExamBinding
import com.ich.pullgo.ui.dialog.TwoButtonDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentDetailedManageExam(private val selectedExam: Exam): Fragment(){
    private val binding by lazy {FragmentDetailedManageExamBinding.inflate(layoutInflater)}

    private val viewModel: ManageClassroomViewModel by viewModels()

    interface OnExamStateChangedListener{
        fun onExamChange()
    }

    var examStateChangedListener: OnExamStateChangedListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        viewModel.examMessage.observe(viewLifecycleOwner){
            when(it.status){
                Status.LOADING -> {
                }
                Status.SUCCESS -> {
                    Toast.makeText(requireContext(),"${it.data}",Toast.LENGTH_SHORT).show()
                    examStateChangedListener?.onExamChange()
                }
                Status.ERROR -> {
                    Toast.makeText(requireContext(),"${it.data}(${it.message})",Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.buttonCancelExam.setOnClickListener {
            val dialog = TwoButtonDialog(requireContext())
            dialog.leftClickListener = object: TwoButtonDialog.TwoButtonDialogLeftClickListener {
                override fun onLeftClicked() {
                    viewModel.cancelExam(selectedExam.id!!)
                }
            }
            dialog.start("시험 취소","${selectedExam.name} 시험을 취소하시겠습니까?",
                "취소하기","닫기")
        }

        binding.buttonFinishExam.setOnClickListener {
            val dialog = TwoButtonDialog(requireContext())
            dialog.leftClickListener = object: TwoButtonDialog.TwoButtonDialogLeftClickListener {
                override fun onLeftClicked() {
                    viewModel.finishExam(selectedExam.id!!)
                }
            }
            dialog.start("시험 종료","${selectedExam.name} 시험을 종료하시겠습니까?",
                "종료하기","닫기")
        }

        return binding.root
    }
}