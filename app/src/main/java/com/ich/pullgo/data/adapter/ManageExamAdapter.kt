package com.ich.pullgo.data.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.ich.pullgo.R
import com.ich.pullgo.data.api.OnExamClickListener
import com.ich.pullgo.data.utils.DurationUtil.Companion.translateDurToMinute
import com.ich.pullgo.data.utils.DurationUtil.Companion.translateISO8601Format
import com.ich.pullgo.databinding.LayoutManageClassroomExamItemBinding
import com.ich.pullgo.domain.model.Exam

class ManageExamAdapter(private val dataSet: List<Exam>):
    RecyclerView.Adapter<ManageExamAdapter.ViewHolder>() {

    var examClickListener: OnExamClickListener? = null

    class ViewHolder(val binding: LayoutManageClassroomExamItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_manage_classroom_exam_item,parent,false)

        return ViewHolder(LayoutManageClassroomExamItemBinding.bind(view))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.exam = dataSet[position]

        val begin = translateISO8601Format(dataSet[position].beginDateTime!!)
        val end = translateISO8601Format(dataSet[position].endDateTime!!)
        holder.binding.examDate = "$begin ~ $end"

        holder.binding.examTime = "제한 시간: ${translateDurToMinute(dataSet[position].timeLimit!!)}분"

        holder.binding.cardViewExamItem.setOnClickListener {
            examClickListener?.onExamClick(it,dataSet[position])
        }

        holder.binding.buttonRemoveExam.setOnClickListener {
            examClickListener?.onRemoveButtonClick(it,dataSet[position])
        }

        holder.binding.buttonExamManageAttenderStatus.setOnClickListener {
            examClickListener?.onTakeExamStatusClick(it,dataSet[position])
        }

        holder.binding.buttonManageQuestions.setOnClickListener {
            examClickListener?.onManageQuestionClick(it,dataSet[position])
        }
    }

    override fun getItemCount() = dataSet.size

    fun isEmptyList() = dataSet.isEmpty()
}