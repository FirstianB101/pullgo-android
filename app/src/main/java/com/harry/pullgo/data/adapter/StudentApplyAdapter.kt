package com.harry.pullgo.data.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.harry.pullgo.R
import com.harry.pullgo.data.api.OnStudentClick
import com.harry.pullgo.data.objects.Student

class StudentApplyAdapter  (private val dataSet: List<Student>, private val showRemoveButton: Boolean):
    RecyclerView.Adapter<StudentApplyAdapter.ViewHolder>(){
    var studentClickListener: OnStudentClick? = null

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val textViewName: TextView = view.findViewById(R.id.textViewApplyItemName)
        val textViewSchool: TextView = view.findViewById(R.id.textViewApplyItemSchoolName)
        val textViewYear: TextView = view.findViewById(R.id.textViewApplyItemSchoolYear)
        val buttonApply: Button = view.findViewById(R.id.buttonApplyItemApply)
        val buttonRemove: ImageButton = view.findViewById(R.id.buttonApplyItemRemove)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view= LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_apply_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textViewName.text = dataSet[position].account?.fullName
        holder.textViewSchool.text = dataSet[position].schoolName
        holder.textViewYear.text = "${dataSet[position].schoolYear.toString()}학년"
        holder.itemView.setOnClickListener {
            studentClickListener?.onBackgroundClick(holder.itemView,dataSet[position])
        }
        holder.buttonApply.setOnClickListener {
            studentClickListener?.onApplyButtonClick(holder.itemView,dataSet[position])
        }

        if(showRemoveButton){
            holder.buttonRemove.visibility = View.VISIBLE
            holder.buttonRemove.setOnClickListener {
                studentClickListener?.onRemoveButtonClick(holder.itemView,dataSet[position])
            }
        }
    }

    override fun getItemCount(): Int= dataSet.size ?: 0
}