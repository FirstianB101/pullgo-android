package com.harry.pullgo.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.harry.pullgo.R
import com.harry.pullgo.data.api.OnTeacherClickListener
import com.harry.pullgo.data.models.Teacher
import com.harry.pullgo.databinding.LayoutManageAcademyPeopleItemBinding

class TeacherManageAdapter(private val dataSet: List<Teacher>):
    RecyclerView.Adapter<TeacherManageAdapter.ViewHolder>(){
    var teacherClickListenerListener: OnTeacherClickListener? = null

    class ViewHolder(val binding: LayoutManageAcademyPeopleItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view= LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_manage_academy_people_item,parent,false)
        return ViewHolder(LayoutManageAcademyPeopleItemBinding.bind(view))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.teacher = dataSet[position]
        holder.itemView.setOnClickListener {
            teacherClickListenerListener?.onBackgroundClick(holder.itemView,dataSet[position])
        }
        holder.binding.buttonManagePeopleKick.setOnClickListener {
            teacherClickListenerListener?.onRemoveButtonClick(holder.itemView,dataSet[position])
        }
    }

    override fun getItemCount(): Int= dataSet.size
}