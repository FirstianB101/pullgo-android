package com.harry.pullgo.data.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.harry.pullgo.R
import com.harry.pullgo.data.api.OnTeacherClick
import com.harry.pullgo.data.objects.Teacher

class TeacherApplyAdapter(private val dataSet: List<Teacher>):
    RecyclerView.Adapter<TeacherApplyAdapter.ViewHolder>(){
    var teacherClickListener: OnTeacherClick? = null
    var applyButtonClickListener: OnTeacherClick? = null

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val textViewName: TextView = view.findViewById(R.id.textViewStudentItemName)
        val textViewSchool: TextView = view.findViewById(R.id.textViewStudentItemSchoolName)
        val textViewYear: TextView = view.findViewById(R.id.textViewStudentItemYear)
        val button: Button = view.findViewById(R.id.buttonStudentItemApply)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view= LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_student_apply_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textViewName.text = dataSet[position].account?.fullName
        holder.textViewSchool.text = dataSet[position].account?.username
        holder.textViewYear.text = ""
        holder.itemView.setOnClickListener {
            teacherClickListener?.onTeacherClick(holder.itemView,dataSet[position])
        }
        holder.button.setOnClickListener {
            applyButtonClickListener?.onTeacherClick(holder.itemView,dataSet[position])
        }
    }

    override fun getItemCount(): Int= dataSet.size ?: 0
}