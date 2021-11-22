package com.harry.pullgo.ui.takeExam

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.harry.pullgo.data.adapter.ExamResultAdapter
import com.harry.pullgo.data.models.AttenderAnswer
import com.harry.pullgo.data.models.AttenderState
import com.harry.pullgo.data.models.Question
import com.harry.pullgo.data.utils.Status
import com.harry.pullgo.databinding.FragmentExamResultBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentTakeExamResult(
    private val selectedStateId: Long,
    private val questions: List<Question>
    ): Fragment() {
    private val binding by lazy{FragmentExamResultBinding.inflate(layoutInflater)}

    private val viewModel: TakeExamViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        initialize()

        return binding.root
    }

    private fun initialize(){
        binding.buttonTakeExamResult.setOnClickListener {
            requireActivity().finish()
        }

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
                    Toast.makeText(requireContext(),"결과를 불러오지 못했습니다 (${it.message})",Toast.LENGTH_SHORT).show()
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
        binding.recyclerViewExamResult.adapter = ExamResultAdapter(answers,questions)
    }
}