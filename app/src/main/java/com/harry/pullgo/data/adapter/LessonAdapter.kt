package com.harry.pullgo.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.harry.pullgo.R
import com.harry.pullgo.data.api.OnLessonClickListener
import com.harry.pullgo.data.models.Lesson
import com.harry.pullgo.databinding.LayoutBottomSheetItemBinding

class LessonAdapter(private val dataSet: List<Lesson>):
    RecyclerView.Adapter<LessonAdapter.ViewHolder>() {

    var itemClickListener: OnLessonClickListener? = null

    class ViewHolder(val binding: LayoutBottomSheetItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_bottom_sheet_item,parent,false)

        return ViewHolder(LayoutBottomSheetItemBinding.bind(view))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val startHour = dataSet[position].schedule?.beginTime?.split(':')?.get(0)
        val startMin = dataSet[position].schedule?.beginTime?.split(':')?.get(1)
        val endHour = dataSet[position].schedule?.endTime?.split(':')?.get(0)
        val endMin = dataSet[position].schedule?.endTime?.split(':')?.get(1)

        holder.binding.lessonName = dataSet[position].name
        holder.binding.lessonTime = "$startHour:$startMin ~ $endHour:$endMin"
        holder.itemView.setOnClickListener {
            itemClickListener?.onLessonClick(holder.itemView, dataSet[position])
        }
    }

    override fun getItemCount() = dataSet.size
}