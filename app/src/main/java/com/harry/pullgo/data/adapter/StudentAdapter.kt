package com.harry.pullgo.data.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.harry.pullgo.R
import com.harry.pullgo.data.api.OnStudentClickListener
import com.harry.pullgo.data.objects.Student
import com.harry.pullgo.databinding.LayoutStudentItemNoButtonBinding

class StudentAdapter (private val dataSet: List<Student>):
    RecyclerView.Adapter<StudentAdapter.ViewHolder>(){
    var studentClickListenerListener: OnStudentClickListener? = null

    class ViewHolder(val binding: LayoutStudentItemNoButtonBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view= LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_student_item_no_button,parent,false)
        return ViewHolder(LayoutStudentItemNoButtonBinding.bind(view))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.student = dataSet[position]
        holder.itemView.setOnClickListener {
            studentClickListenerListener?.onBackgroundClick(holder.itemView,dataSet[position])
        }
    }

    override fun getItemCount(): Int= dataSet.size
}