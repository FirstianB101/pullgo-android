package com.ich.pullgo.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ich.pullgo.R
import com.ich.pullgo.data.api.OnTeacherClickListener
import com.ich.pullgo.databinding.LayoutTeacherItemNoButtonBinding
import com.ich.pullgo.domain.model.Teacher

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