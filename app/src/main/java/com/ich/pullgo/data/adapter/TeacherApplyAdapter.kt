package com.ich.pullgo.data.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ich.pullgo.R
import com.ich.pullgo.data.api.OnTeacherClickListener
import com.ich.pullgo.databinding.LayoutApplyItemBinding
import com.ich.pullgo.domain.model.Teacher

class TeacherApplyAdapter(private val dataSet: List<Teacher>, private val showRemoveButton: Boolean):
    RecyclerView.Adapter<TeacherApplyAdapter.ViewHolder>(){
    var teacherClickListener: OnTeacherClickListener? = null

    class ViewHolder(val binding: LayoutApplyItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view= LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_apply_item,parent,false)
        return ViewHolder(LayoutApplyItemBinding.bind(view))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.teacher = dataSet[position]
        holder.itemView.setOnClickListener {
            teacherClickListener?.onBackgroundClick(holder.itemView,dataSet[position])
        }
        holder.binding.buttonApplyItemApply.setOnClickListener {
            teacherClickListener?.onApplyButtonClick(holder.itemView,dataSet[position])
        }

        if(showRemoveButton){
            holder.binding.buttonApplyItemRemove.visibility = View.VISIBLE
            holder.binding.buttonApplyItemRemove.setOnClickListener {
                teacherClickListener?.onRemoveButtonClick(holder.itemView,dataSet[position])
            }
        }
    }

    override fun getItemCount(): Int= dataSet.size ?: 0
}