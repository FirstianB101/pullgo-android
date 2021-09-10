package com.harry.pullgo.data.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.harry.pullgo.R
import com.harry.pullgo.data.api.OnStudentClickListener
import com.harry.pullgo.data.objects.Student
import com.harry.pullgo.databinding.LayoutApplyItemBinding

class StudentApplyAdapter  (private val dataSet: List<Student>, private val showRemoveButton: Boolean):
    RecyclerView.Adapter<StudentApplyAdapter.ViewHolder>(){
    var studentClickListenerListener: OnStudentClickListener? = null

    class ViewHolder(val binding: LayoutApplyItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view= LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_apply_item,parent,false)
        return ViewHolder(LayoutApplyItemBinding.bind(view))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.student = dataSet[position]
        holder.itemView.setOnClickListener {
            studentClickListenerListener?.onBackgroundClick(holder.itemView,dataSet[position])
        }
        holder.binding.buttonApplyItemApply.setOnClickListener {
            studentClickListenerListener?.onApplyButtonClick(holder.itemView,dataSet[position])
        }

        if(showRemoveButton){
            holder.binding.buttonApplyItemRemove.visibility = View.VISIBLE
            holder.binding.buttonApplyItemRemove.setOnClickListener {
                studentClickListenerListener?.onRemoveButtonClick(holder.itemView,dataSet[position])
            }
        }
    }

    override fun getItemCount(): Int= dataSet.size ?: 0
}