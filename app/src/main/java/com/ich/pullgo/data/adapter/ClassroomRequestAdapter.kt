package com.ich.pullgo.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ich.pullgo.R
import com.ich.pullgo.data.api.OnClassroomRequestListener
import com.ich.pullgo.data.models.Classroom
import com.ich.pullgo.databinding.LayoutClassroomRequestItemBinding

class ClassroomRequestAdapter(private val dataSet: List<Classroom>)
    : RecyclerView.Adapter<ClassroomRequestAdapter.ViewHolder>(){

    var itemClickListener: OnClassroomRequestListener? = null

    class ViewHolder(val binding: LayoutClassroomRequestItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_classroom_request_item,parent,false)
        return ViewHolder(LayoutClassroomRequestItemBinding.bind(view))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.classroomInfo = dataSet[position].name.toString().split(';')
        holder.binding.creator = dataSet[position].creator

        holder.itemView.setOnClickListener {
            itemClickListener?.onClassroomClick(holder.itemView, dataSet[position])
        }

        holder.binding.buttonRemoveClassroomRequest.setOnClickListener {
            itemClickListener?.onRemoveRequest(it,dataSet[position])
        }
    }

    override fun getItemCount(): Int = dataSet.size
}