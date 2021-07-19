package com.harry.pullgo.ui.calendar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.harry.pullgo.R
import com.harry.pullgo.data.api.OnLessonClick
import com.harry.pullgo.data.objects.Lesson

class BottomSheetLessonsAdapter(private val dataSet: List<Lesson>?):
    RecyclerView.Adapter<BottomSheetLessonsAdapter.ViewHolder>() {

    var itemClickListener: OnLessonClick? = null

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val lessonName: TextView = view.findViewById(R.id.textViewBottomSheetLessonName)
        val lessonTime: TextView = view.findViewById(R.id.textViewBottomSheetLessonTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_bottom_sheet_item,parent,false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val startHour = dataSet?.get(position)?.schedule?.beginTime?.split(':')?.get(0)
        val startMin = dataSet?.get(position)?.schedule?.beginTime?.split(':')?.get(1)
        val endHour = dataSet?.get(position)?.schedule?.endTime?.split(':')?.get(0)
        val endMin = dataSet?.get(position)?.schedule?.endTime?.split(':')?.get(1)

        holder.lessonName.text = dataSet?.get(position)?.name
        holder.lessonTime.text = "$startHour:$startMin ~ $endHour:$endMin"
        holder.itemView.setOnClickListener {
            itemClickListener?.onLessonClick(holder.itemView, dataSet?.get(position))
        }
    }

    override fun getItemCount() = dataSet?.size ?: 0
}