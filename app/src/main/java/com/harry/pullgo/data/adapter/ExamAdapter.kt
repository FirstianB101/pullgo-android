package com.harry.pullgo.data.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.harry.pullgo.R
import com.harry.pullgo.data.api.OnExamClickListener
import com.harry.pullgo.data.models.Exam
import com.harry.pullgo.data.utils.DurationUtil.Companion.translateISO8601Format
import com.harry.pullgo.data.utils.DurationUtil.Companion.translateDurToMinute
import com.harry.pullgo.databinding.LayoutExamItemBinding

class ExamAdapter(private val dataSet: List<Exam>):
    RecyclerView.Adapter<ExamAdapter.ViewHolder>() {

    var itemClickListener: OnExamClickListener? = null

    class ViewHolder(val binding: LayoutExamItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_exam_item,parent,false)

        return ViewHolder(LayoutExamItemBinding.bind(view))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.exam = dataSet[position]

        val begin = translateISO8601Format(dataSet[position].beginDateTime!!)
        val end = translateISO8601Format(dataSet[position].endDateTime!!)
        holder.binding.examDate = "$begin ~ $end"

        holder.binding.examTime = "제한 시간: ${translateDurToMinute(dataSet[position].timeLimit!!)}분"

        holder.itemView.setOnClickListener {
            itemClickListener?.onExamClick(it,dataSet[position])
        }
    }

    override fun getItemCount() = dataSet.size
}