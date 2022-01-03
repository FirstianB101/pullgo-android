package com.ich.pullgo.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ich.pullgo.R
import com.ich.pullgo.data.api.OnAcademyClickListener
<<<<<<< HEAD:app/src/main/java/com/ich/pullgo/data/adapter/AcademyAdapter.kt
import com.ich.pullgo.data.models.Academy
import com.ich.pullgo.databinding.LayoutSearchAcademyItemBinding
=======
import com.ich.pullgo.databinding.LayoutSearchAcademyItemBinding
import com.ich.pullgo.domain.model.Academy
>>>>>>> ich:app/src/main/java/com/harry/pullgo/data/adapter/AcademyAdapter.kt

class AcademyAdapter(private val dataSet: List<Academy>):
    RecyclerView.Adapter<AcademyAdapter.ViewHolder>(){
    var itemClickListener: OnAcademyClickListener? = null

    class ViewHolder(val binding: LayoutSearchAcademyItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view= LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_search_academy_item,parent,false)
        return ViewHolder(LayoutSearchAcademyItemBinding.bind(view))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.academy = dataSet[position]
        holder.itemView.setOnClickListener {
            itemClickListener?.onAcademyClick(holder.itemView, dataSet[position])
        }
    }

    override fun getItemCount(): Int= dataSet.size ?: 0
}