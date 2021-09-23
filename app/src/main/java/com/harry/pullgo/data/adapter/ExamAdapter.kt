package com.harry.pullgo.data.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.harry.pullgo.R
import com.harry.pullgo.data.api.OnExamClickListener
import com.harry.pullgo.data.models.Exam
import com.harry.pullgo.databinding.LayoutExamItemBinding
import java.text.SimpleDateFormat
import java.time.Duration
import java.util.*

class ExamAdapter(private val dataSet: List<Exam>):
    RecyclerView.Adapter<ExamAdapter.ViewHolder>() {

    var itemClickListenerListener: OnExamClickListener? = null

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

        holder.binding.examTime = "제한 시간: ${translatePTFormat(dataSet[position].timeLimit!!)}분"
    }

    override fun getItemCount() = dataSet.size

    private fun translateISO8601Format(time: String): String{
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.KOREAN)
        val date = format.parse(time)
        val formatter = SimpleDateFormat("MM/dd HH:mm",Locale.KOREAN)
        return formatter.format(date!!)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun translatePTFormat(time: String): String{
        val duration = Duration.parse(time)
        return duration.toMinutes().toString()
    }
}