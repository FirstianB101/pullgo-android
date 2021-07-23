package com.harry.pullgo.data.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.harry.pullgo.R
import com.harry.pullgo.data.api.OnClassroomClick
import com.harry.pullgo.data.objects.Classroom

class ClassroomAdapter(private val dataSet: List<Classroom>)
    : RecyclerView.Adapter<ClassroomAdapter.ViewHolder>(){

    var itemClickListener: OnClassroomClick? = null

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val textViewName: TextView = view.findViewById(R.id.textViewApplyClassroomName)
        val textViewTeacherName: TextView = view.findViewById(R.id.textViewApplyClassroomTeacherName)
        val textViewLessonDate: TextView = view.findViewById(R.id.textViewApplyClassroomLessonDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view= LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_apply_classroom_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val name = dataSet[position].name.toString()

        val information = name.split(';')

        holder.textViewName.text = information[0]
        holder.textViewTeacherName.text = "${information[1]} 선생님"
        holder.textViewLessonDate.text = information[2]
        holder.itemView.setOnClickListener {
            itemClickListener?.onClassroomClick(holder.itemView, dataSet[position])
        }
    }

    override fun getItemCount(): Int = dataSet.size
}