package com.harry.pullgo.data.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.harry.pullgo.R
import com.harry.pullgo.data.models.AttenderState
import com.harry.pullgo.data.models.Student
import com.harry.pullgo.data.utils.ExamProgress
import com.harry.pullgo.databinding.LayoutExamStatusItemBinding

class ExamStatusAdapter(
    private val dataSet: List<AttenderState>,
    private val studentInfos: List<Student>,
    private val context: Context
    ):
    RecyclerView.Adapter<ExamStatusAdapter.ViewHolder>() {

    class ViewHolder(val binding: LayoutExamStatusItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_exam_status_item,parent,false)

        return ViewHolder(LayoutExamStatusItemBinding.bind(view))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.textViewStudentNameExamStatus.text = studentInfos[position].account?.fullName.toString()
        holder.binding.textViewSchoolNameExamStatus.text = studentInfos[position].schoolName.toString()
        holder.binding.textViewGradeExamStatus.text = "${studentInfos[position].schoolYear?.plus(1).toString()}학년"

        holder.binding.textViewStudentScoreExamStatus.text = "${dataSet[position].score}점 / 100점"
        holder.binding.textViewStatusExamStatus.text =
            when(dataSet[position].progress){
                ExamProgress.ABSENCE -> {
                    holder.binding.textViewStatusExamStatus.setTextColor(ContextCompat.getColor(context,R.color.material_700_red))
                    "미응시"
                }
                ExamProgress.COMPLETE -> {
                    holder.binding.textViewStatusExamStatus.setTextColor(ContextCompat.getColor(context,R.color.material_700_green))
                    "응시 완료"
                }
                ExamProgress.ONGOING -> {
                    holder.binding.textViewStatusExamStatus.setTextColor(ContextCompat.getColor(context,android.R.color.holo_orange_dark))
                    "응시중"
                }
                else -> "오류"
            }
    }

    override fun getItemCount() = dataSet.size
}