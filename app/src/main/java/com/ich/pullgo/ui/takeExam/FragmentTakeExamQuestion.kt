package com.ich.pullgo.ui.takeExam

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.ich.pullgo.databinding.FragmentTakeExamQuestionBinding
import com.ich.pullgo.domain.model.Question
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentTakeExamQuestion(
    private val selectedQuestion: Question,
    private val questionNum: Int,
    ): Fragment() {
    private val binding by lazy{FragmentTakeExamQuestionBinding.inflate(layoutInflater)}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        initialize()

        return binding.root
    }

    private fun initialize(){
        binding.textViewQuizTitle.text = "$questionNum. ${selectedQuestion.content}"

        Glide.with(this)
            .load(selectedQuestion.pictureUrl)
            .fitCenter()
            .into(binding.imageViewQuiz)
    }
}