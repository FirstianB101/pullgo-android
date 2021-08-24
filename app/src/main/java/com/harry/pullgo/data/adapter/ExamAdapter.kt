package com.harry.pullgo.data.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.harry.pullgo.R
import com.harry.pullgo.data.api.OnExamClick
import com.harry.pullgo.data.objects.Exam
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class ExamAdapter(private val dataSet: List<Exam>?):
    RecyclerView.Adapter<ExamAdapter.ViewHolder>() {

    var itemClickListener: OnExamClick? = null

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val academyName: TextView = view.findViewById(R.id.textViewExamAcademyName)
        val classroomName: TextView = view.findViewById(R.id.textViewExamClassroomName)
        val examName: TextView = view.findViewById(R.id.textViewExamName)
        val examDate: TextView = view.findViewById(R.id.textViewExamDate)
        val examTime: TextView = view.findViewById(R.id.textViewExamTime)
        val passScore: TextView = view.findViewById(R.id.textViewExamPassScore)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_exam_item,parent,false)

        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.examName.text = dataSet?.get(position)?.name
        holder.passScore.text = "기준: ${dataSet?.get(position)?.passScore.toString()}점"

        val begin = translateISO8601Format(dataSet?.get(position)?.beginDateTime!!)
        val end = translateISO8601Format(dataSet[position].endDateTime!!)
        holder.examDate.text = "$begin ~ $end"
        holder.examTime.text = "제한 시간: ${translatePTFormat(dataSet[position].timeLimit!!)}분"
    }

    override fun getItemCount() = dataSet?.size ?: 0

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