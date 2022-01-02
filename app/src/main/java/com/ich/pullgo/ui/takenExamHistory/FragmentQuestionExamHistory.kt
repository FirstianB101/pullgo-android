package com.ich.pullgo.ui.takenExamHistory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.ich.pullgo.R
import com.ich.pullgo.data.models.Question
import com.ich.pullgo.databinding.FragmentExamHistoryQuestionBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentQuestionExamHistory(
    private val selectedQuestion: Question,
    private val questionNum: Int,
    private val isCorrect: Boolean
    ): Fragment() {
    private val binding by lazy{FragmentExamHistoryQuestionBinding.inflate(layoutInflater)}

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

        if(isCorrect)
            binding.imageViewCheckAnswer.setImageResource(R.drawable.ic_outline_circle_24)
        else
            binding.imageViewCheckAnswer.setImageResource(R.drawable.wrong)
    }
}