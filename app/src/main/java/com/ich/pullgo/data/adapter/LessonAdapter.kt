package com.ich.pullgo.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ich.pullgo.R
import com.ich.pullgo.data.api.OnLessonClickListener
<<<<<<< HEAD:app/src/main/java/com/ich/pullgo/data/adapter/LessonAdapter.kt
import com.ich.pullgo.data.models.Lesson
import com.ich.pullgo.databinding.LayoutBottomSheetItemBinding
=======
import com.ich.pullgo.databinding.LayoutBottomSheetItemBinding
import com.ich.pullgo.domain.model.Lesson
>>>>>>> ich:app/src/main/java/com/harry/pullgo/data/adapter/LessonAdapter.kt

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