package com.harry.pullgo.ui.calendar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.harry.pullgo.R
import com.harry.pullgo.data.api.OnLessonClick
import com.harry.pullgo.data.objects.Lesson

class BottomSheetLessonsAdapter(private val dataSet: Array<Lesson>):
    RecyclerView.Adapter<BottomSheetLessonsAdapter.ViewHolder>() {

    var itemClickListener: OnLessonClick? = null

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val lessonName: TextView = view.findViewById(R.id.textViewBottomSheetLessonName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_bottom_sheet_item,parent,false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.lessonName.text = dataSet[position].name
        holder.itemView.setOnClickListener {
            itemClickListener?.onLessonClick(holder.itemView,dataSet[position])
        }
    }

    override fun getItemCount() = dataSet.size
}