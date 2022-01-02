package com.ich.pullgo.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ich.pullgo.R
import com.ich.pullgo.data.models.Question
import com.ich.pullgo.data.utils.ListCompareUtil
import com.ich.pullgo.databinding.LayoutExamResultItemBinding

class ExamResultAdapter(
    private val attenderAnswersMap: Map<Long,List<Int>>,
    private val questions: List<Question>
    ): RecyclerView.Adapter<ExamResultAdapter.ViewHolder>() {

    class ViewHolder(val binding: LayoutExamResultItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_exam_result_item,parent,false)

        return ViewHolder(LayoutExamResultItemBinding.bind(view))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.textViewExamResultQuestionNum.text = "${position+1}번."

        val attenderAnswer = attenderAnswersMap[questions[position].id]

        val isCorrect = if(attenderAnswer == null) false
                        else ListCompareUtil.isEqualList(attenderAnswer,questions[position].answer!!)

        if(isCorrect)
            holder.binding.imageViewExamResultQuestionNum.setImageResource(R.drawable.ic_outline_circle_24)
        else
            holder.binding.imageViewExamResultQuestionNum.setImageResource(R.drawable.wrong)
    }

    override fun getItemCount() = questions.size
}