package com.harry.pullgo.ui.takeExam

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.harry.pullgo.R
import com.harry.pullgo.data.models.Question
import com.harry.pullgo.databinding.FragmentQuestionBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentQuestion(
    private val selectedQuestion: Question,
    private val questionNum: Int,
    ): Fragment() {
    private val binding by lazy{FragmentQuestionBinding.inflate(layoutInflater)}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        initialize()

        return binding.root
    }

    private fun initialize(){
        binding.textViewQuizTitle.text = "$questionNum. ${selectedQuestion.content}"

        Glide.with(this)
            .load(selectedQuestion.pictureUrl)
            .error(R.drawable.image_load_error)
            .into(binding.imageViewQuiz)
    }
}