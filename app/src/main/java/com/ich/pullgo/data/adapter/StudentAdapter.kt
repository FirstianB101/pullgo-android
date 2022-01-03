package com.ich.pullgo.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ich.pullgo.R
import com.ich.pullgo.data.api.OnStudentClickListener
import com.ich.pullgo.databinding.LayoutStudentItemNoButtonBinding
import com.ich.pullgo.domain.model.Student

class StudentAdapter (private val dataSet: List<Student>):
    RecyclerView.Adapter<StudentAdapter.ViewHolder>(){
    var studentClickListener: OnStudentClickListener? = null

    class ViewHolder(val binding: LayoutStudentItemNoButtonBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view= LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_student_item_no_button,parent,false)
        return ViewHolder(LayoutStudentItemNoButtonBinding.bind(view))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.student = dataSet[position]
        holder.itemView.setOnClickListener {
            studentClickListener?.onBackgroundClick(holder.itemView,dataSet[position])
        }
    }

    override fun getItemCount(): Int= dataSet.size
}