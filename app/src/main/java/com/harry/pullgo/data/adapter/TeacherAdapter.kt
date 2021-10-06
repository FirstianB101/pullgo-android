package com.harry.pullgo.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.harry.pullgo.R
import com.harry.pullgo.data.api.OnTeacherClickListener
import com.harry.pullgo.data.models.Teacher
import com.harry.pullgo.databinding.LayoutTeacherItemNoButtonBinding

class TeacherAdapter (private val dataSet: List<Teacher>):
    RecyclerView.Adapter<TeacherAdapter.ViewHolder>(){
    var teacherClickListener: OnTeacherClickListener? = null

    class ViewHolder(val binding: LayoutTeacherItemNoButtonBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view= LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_teacher_item_no_button,parent,false)
        return ViewHolder(LayoutTeacherItemNoButtonBinding.bind(view))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.teacher = dataSet[position]
        holder.itemView.setOnClickListener {
            teacherClickListener?.onBackgroundClick(holder.itemView,dataSet[position])
        }
    }

    override fun getItemCount(): Int= dataSet.size
}