package com.harry.pullgo

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lakue.lakuepopupactivity.PopupActivity
import com.lakue.lakuepopupactivity.PopupGravity
import com.lakue.lakuepopupactivity.PopupType

class AcademySearchAdapter(private val dataSet: List<Academy>?):
    RecyclerView.Adapter<AcademySearchAdapter.ViewHolder>(){
    var itemClickListener: OnAcademyClick? = null

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val textViewName: TextView = view.findViewById(R.id.textViewSearchAcademyName)
        val textViewAddress: TextView = view.findViewById(R.id.textViewSearchAcademyAddress)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view= LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_search_academy_adapter,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textViewName.text= dataSet?.get(position)?.name.toString()
        holder.textViewAddress.text=dataSet?.get(position)?.address.toString()
        holder.itemView.setOnClickListener {
            itemClickListener?.onAcademyClick(holder.itemView, dataSet?.get(position))
        }
    }

    override fun getItemCount(): Int= dataSet?.size ?: 0
}