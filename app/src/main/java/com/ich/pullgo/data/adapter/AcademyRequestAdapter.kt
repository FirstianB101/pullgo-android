package com.ich.pullgo.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ich.pullgo.R
import com.ich.pullgo.data.api.OnAcademyRequestListener
import com.ich.pullgo.data.models.Academy
import com.ich.pullgo.databinding.LayoutAcademyRequestItemBinding

class AcademyRequestAdapter(private val dataSet: List<Academy>):
    RecyclerView.Adapter<AcademyRequestAdapter.ViewHolder>(){
    var itemClickListener: OnAcademyRequestListener? = null

    class ViewHolder(val binding: LayoutAcademyRequestItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_academy_request_item,parent,false)
        return ViewHolder(LayoutAcademyRequestItemBinding.bind(view))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.academy = dataSet[position]
        holder.itemView.setOnClickListener {
            itemClickListener?.onAcademyClick(holder.itemView, dataSet[position])
        }
        holder.binding.buttonRemoveAcademyRequest.setOnClickListener {
            itemClickListener?.onRemoveRequest(it, dataSet[position])
        }
    }

    override fun getItemCount(): Int = dataSet.size
}