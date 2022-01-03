package com.ich.pullgo.data.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ich.pullgo.R
import com.ich.pullgo.data.api.OnClassroomClickListener
<<<<<<< HEAD:app/src/main/java/com/ich/pullgo/data/adapter/ManageClassroomAdapter.kt
import com.ich.pullgo.data.models.Classroom
=======
import com.ich.pullgo.domain.model.Classroom
>>>>>>> ich:app/src/main/java/com/harry/pullgo/data/adapter/ManageClassroomAdapter.kt

class ManageClassroomAdapter(private val dataSet: List<Classroom>)
    :RecyclerView.Adapter<ManageClassroomAdapter.ViewHolder>(){

    var itemClickListener: OnClassroomClickListener? = null

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val textViewName: TextView = view.findViewById(R.id.textViewTeacherClassroomName)
        val textViewTeacherName: TextView = view.findViewById(R.id.textViewTeacherName)
        val textViewDate: TextView = view.findViewById(R.id.textViewTeacherClassroomDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view= LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_classroom_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val name = dataSet[position].name.toString()
        val creator = dataSet[position].creator
        val information = name.split(';')

        holder.textViewName.text = information[0]
        holder.textViewTeacherName.text = "${creator?.account?.fullName} 선생님"
        holder.textViewDate.text = information[1]
        holder.itemView.setOnClickListener {
            itemClickListener?.onClassroomClick(holder.itemView, dataSet[position])
        }
    }

    override fun getItemCount(): Int = dataSet.size
}