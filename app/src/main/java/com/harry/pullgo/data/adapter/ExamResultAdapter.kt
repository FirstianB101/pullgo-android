package com.harry.pullgo.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.harry.pullgo.R
import com.harry.pullgo.data.api.OnLessonClickListener
import com.harry.pullgo.data.models.AttenderAnswer
import com.harry.pullgo.data.models.Question
import com.harry.pullgo.data.utils.ListCompareUtil
import com.harry.pullgo.databinding.LayoutExamResultItemBinding

class ExamResultAdapter(
    private val attenderAnswers: List<AttenderAnswer>,
    private val questions: List<Question>
    ): RecyclerView.Adapter<ExamResultAdapter.ViewHolder>() {

    class ViewHolder(val binding: LayoutExamResultItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_exam_result_item,parent,false)

        return ViewHolder(LayoutExamResultItemBinding.bind(view))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.textViewExamResultQuestionNum.text = "${position+1}번."

        val isCorrect = ListCompareUtil.isEqualList(attenderAnswers[position].answer,questions[position].answer!!)

        if(isCorrect)
            holder.binding.imageViewExamResultQuestionNum.setImageResource(R.drawable.ic_outline_circle_24)
        else
            holder.binding.imageViewExamResultQuestionNum.setImageResource(R.drawable.wrong)
    }

    override fun getItemCount() = attenderAnswers.size
}