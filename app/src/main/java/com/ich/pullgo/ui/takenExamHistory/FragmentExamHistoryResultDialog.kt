package com.ich.pullgo.ui.takenExamHistory

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.ich.pullgo.data.adapter.ExamResultAdapter
<<<<<<< HEAD:app/src/main/java/com/ich/pullgo/ui/takenExamHistory/FragmentExamHistoryResultDialog.kt
import com.ich.pullgo.data.models.AttenderAnswer
import com.ich.pullgo.data.models.Question
import com.ich.pullgo.data.utils.Status
import com.ich.pullgo.databinding.FragmentExamResultBinding
=======
import com.ich.pullgo.data.utils.Status
import com.ich.pullgo.databinding.FragmentExamResultBinding
import com.ich.pullgo.domain.model.AttenderAnswer
import com.ich.pullgo.domain.model.Question
>>>>>>> ich:app/src/main/java/com/harry/pullgo/ui/takenExamHistory/FragmentExamHistoryResultDialog.kt
import com.ich.pullgo.ui.takeExam.TakeExamViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentExamHistoryResultDialog(
    private val selectedStateId: Long,
    private val questions: List<Question>
): DialogFragment() {
    private val binding by lazy {FragmentExamResultBinding.inflate(layoutInflater)}

    private val viewModel: TakeExamViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = MaterialAlertDialogBuilder(requireActivity())

        initialize()

        builder.setView(binding.root)
        val _dialog = builder.create()
        _dialog.setCanceledOnTouchOutside(false)
        _dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)

        return _dialog
    }

    private fun initialize(){
        binding.buttonTakeExamResult.visibility = View.GONE
        binding.textViewExamResultComment.visibility = View.GONE

        viewModel.requestAttenderAnswersInState(selectedStateId)
        viewModel.getOneAttenderState(selectedStateId)
    }

    private fun initViewModel(){
        viewModel.attenderAnswersInState.observe(requireActivity()){
            when(it.status){
                Status.SUCCESS -> {
                    displayResult(it.data!!)
                }
                Status.LOADING -> {}
                Status.ERROR -> {
                    Toast.makeText(requireContext(),"결과를 불러오지 못했습니다 (${it.message})", Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.oneAttenderStateForResult.observe(requireActivity()){
            when(it.status){
                Status.SUCCESS -> {
                    binding.textViewExamResult.text = "${it.data?.score}점 / 100점"
                }
                Status.LOADING -> {}
                Status.ERROR -> {}
            }
        }
    }

    private fun displayResult(answers: List<AttenderAnswer>){
        val answerMap = mutableMapOf<Long,List<Int>>()
        for(answer in answers){
            answerMap[answer.questionId] = answer.answer
        }

        binding.recyclerViewExamResult.adapter = ExamResultAdapter(answerMap,questions)
    }
}