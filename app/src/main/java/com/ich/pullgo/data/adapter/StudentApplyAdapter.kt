package com.ich.pullgo.data.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ich.pullgo.R
import com.ich.pullgo.data.api.OnStudentClickListener
<<<<<<< HEAD:app/src/main/java/com/ich/pullgo/data/adapter/StudentApplyAdapter.kt
import com.ich.pullgo.data.models.Student
import com.ich.pullgo.databinding.LayoutApplyItemBinding
=======
import com.ich.pullgo.databinding.LayoutApplyItemBinding
import com.ich.pullgo.domain.model.Student
>>>>>>> ich:app/src/main/java/com/harry/pullgo/data/adapter/StudentApplyAdapter.kt

class StudentApplyAdapter  (private val dataSet: List<Student>, private val showRemoveButton: Boolean):
    RecyclerView.Adapter<StudentApplyAdapter.ViewHolder>(){
    var studentClickListener: OnStudentClickListener? = null

    class ViewHolder(val binding: LayoutApplyItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view= LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_apply_item,parent,false)
        return ViewHolder(LayoutApplyItemBinding.bind(view))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.student = dataSet[position]
        holder.itemView.setOnClickListener {
            studentClickListener?.onBackgroundClick(holder.itemView,dataSet[position])
        }
        holder.binding.buttonApplyItemApply.setOnClickListener {
            studentClickListener?.onApplyButtonClick(holder.itemView,dataSet[position])
        }

        if(showRemoveButton){
            holder.binding.buttonApplyItemRemove.visibility = View.VISIBLE
            holder.binding.buttonApplyItemRemove.setOnClickListener {
                studentClickListener?.onRemoveButtonClick(holder.itemView,dataSet[position])
            }
        }
    }

    override fun getItemCount(): Int= dataSet.size ?: 0
}