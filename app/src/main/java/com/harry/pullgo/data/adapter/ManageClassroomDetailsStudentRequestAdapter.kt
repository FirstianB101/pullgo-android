package com.harry.pullgo.data.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.harry.pullgo.R
import com.harry.pullgo.data.api.OnStudentClick
import com.harry.pullgo.data.objects.Student

class ManageClassroomDetailsStudentRequestAdapter  (private val dataSet: List<Student>):
    RecyclerView.Adapter<ManageClassroomDetailsStudentRequestAdapter.ViewHolder>(){
    var studentClickListener: OnStudentClick? = null

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val textViewName: TextView = view.findViewById(R.id.textViewStudentItemName)
        val textViewSchool: TextView = view.findViewById(R.id.textViewStudentItemSchoolName)
        val textViewYear: TextView = view.findViewById(R.id.textViewStudentItemYear)
        val buttonApply: Button = view.findViewById(R.id.buttonStudentItemApply)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view= LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_student_apply_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textViewName.text = dataSet[position].account?.fullName
        holder.textViewSchool.text = dataSet[position].schoolName
        holder.textViewYear.text = "${dataSet[position].schoolYear.toString()}학년"
        holder.buttonApply.setOnClickListener {
            studentClickListener?.onStudentClick(holder.itemView,dataSet[position])
        }
    }

    override fun getItemCount(): Int= dataSet.size ?: 0
}