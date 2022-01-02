package com.ich.pullgo.data.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ich.pullgo.R
import com.ich.pullgo.data.models.AttenderState
import com.ich.pullgo.data.models.Student
import com.ich.pullgo.data.utils.ExamProgress
import com.ich.pullgo.databinding.LayoutExamStatusItemBinding

class ExamStatusAdapter(
    private val students: List<Student>,
    private val statesMap: Map<Long,AttenderState>,
    private val context: Context
    ):
    RecyclerView.Adapter<ExamStatusAdapter.ViewHolder>() {

    class ViewHolder(val binding: LayoutExamStatusItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_exam_status_item,parent,false)

        return ViewHolder(LayoutExamStatusItemBinding.bind(view))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.textViewStudentNameExamStatus.text = students[position].account?.fullName.toString()
        holder.binding.textViewSchoolNameExamStatus.text = students[position].schoolName.toString()
        holder.binding.textViewGradeExamStatus.text = "${students[position].schoolYear?.plus(1).toString()}학년"

        val state = statesMap[students[position].id]
        holder.binding.textViewStatusExamStatus.text =
            when(state?.progress){
                ExamProgress.COMPLETE -> {
                    holder.binding.textViewStatusExamStatus.setTextColor(ContextCompat.getColor(context,R.color.material_700_green))
                    holder.binding.textViewStudentScoreExamStatus.text = "${state.score ?: 0}점 / 100점"
                    "응시 완료"
                }
                ExamProgress.ONGOING -> {
                    holder.binding.textViewStatusExamStatus.setTextColor(ContextCompat.getColor(context,android.R.color.holo_orange_dark))
                    "응시중"
                }
                else ->{
                    holder.binding.textViewStatusExamStatus.setTextColor(ContextCompat.getColor(context,R.color.material_700_red))
                    "미응시"
                }
            }

    }

    override fun getItemCount() = students.size
}