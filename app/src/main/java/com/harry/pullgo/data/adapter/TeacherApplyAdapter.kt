package com.harry.pullgo.data.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.harry.pullgo.R
import com.harry.pullgo.data.api.OnTeacherClickListener
import com.harry.pullgo.data.objects.Teacher
import com.harry.pullgo.databinding.LayoutApplyItemBinding

class TeacherApplyAdapter(private val dataSet: List<Teacher>, private val showRemoveButton: Boolean):
    RecyclerView.Adapter<TeacherApplyAdapter.ViewHolder>(){
    var teacherClickListenerListener: OnTeacherClickListener? = null

    class ViewHolder(val binding: LayoutApplyItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view= LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_apply_item,parent,false)
        return ViewHolder(LayoutApplyItemBinding.bind(view))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.teacher = dataSet[position]
        holder.itemView.setOnClickListener {
            teacherClickListenerListener?.onBackgroundClick(holder.itemView,dataSet[position])
        }
        holder.binding.buttonApplyItemApply.setOnClickListener {
            teacherClickListenerListener?.onApplyButtonClick(holder.itemView,dataSet[position])
        }

        if(showRemoveButton){
            holder.binding.buttonApplyItemRemove.visibility = View.VISIBLE
            holder.binding.buttonApplyItemRemove.setOnClickListener {
                teacherClickListenerListener?.onRemoveButtonClick(holder.itemView,dataSet[position])
            }
        }
    }

    override fun getItemCount(): Int= dataSet.size ?: 0
}