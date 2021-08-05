package com.harry.pullgo.data.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.harry.pullgo.R
import com.harry.pullgo.data.api.OnTeacherClick
import com.harry.pullgo.data.objects.Teacher

class TeacherManageAdapter(private val dataSet: List<Teacher>):
    RecyclerView.Adapter<TeacherManageAdapter.ViewHolder>(){
    var teacherClickListener: OnTeacherClick? = null

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val textViewFullName: TextView = view.findViewById(R.id.textViewManagePeopleFullName)
        val textViewUserName: TextView = view.findViewById(R.id.textViewManagePeopleUserName)
        val textViewPhone: TextView = view.findViewById(R.id.textViewManagePeoplePhone)
        val buttonKick: Button = view.findViewById(R.id.buttonManagePeopleKick)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view= LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_manage_academy_people_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textViewFullName.text = dataSet[position].account?.fullName
        holder.textViewUserName.text = "(${dataSet[position].account?.username})"
        holder.textViewPhone.text = dataSet[position].account?.phone
        holder.itemView.setOnClickListener {
            teacherClickListener?.onBackgroundClick(holder.itemView,dataSet[position])
        }
        holder.buttonKick.setOnClickListener {
            teacherClickListener?.onRemoveButtonClick(holder.itemView,dataSet[position])
        }
    }

    override fun getItemCount(): Int= dataSet.size ?: 0
}