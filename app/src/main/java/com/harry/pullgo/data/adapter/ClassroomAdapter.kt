package com.harry.pullgo.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.harry.pullgo.R
import com.harry.pullgo.data.api.OnClassroomClickListener
import com.harry.pullgo.data.models.Classroom
import com.harry.pullgo.databinding.LayoutClassroomItemBinding

class ClassroomAdapter(private val dataSet: List<Classroom>)
    : RecyclerView.Adapter<ClassroomAdapter.ViewHolder>(){

    var itemClickListener: OnClassroomClickListener? = null

    class ViewHolder(val binding: LayoutClassroomItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_classroom_item,parent,false)
        return ViewHolder(LayoutClassroomItemBinding.bind(view))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.classroomInfo = dataSet[position].name.toString().split(';')

        holder.itemView.setOnClickListener {
            itemClickListener?.onClassroomClick(holder.itemView, dataSet[position])
        }
    }

    override fun getItemCount(): Int = dataSet.size
}