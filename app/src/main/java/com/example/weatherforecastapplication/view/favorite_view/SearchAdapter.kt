package com.example.weatherforecastapplication.view.favorite_view

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecastapplication.R
import java.util.Calendar
class StringDiffUtil : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem== newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

}

class SearchAdapter(val context: Context, private val listener: SearchListener) : ListAdapter<String, SearchAdapter.ViewHolder>(StringDiffUtil()) {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val search: TextView =view.findViewById(R.id.tv_name)
        val card:CardView=view.findViewById(R.id.card)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.search_card_layout,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val name=getItem(position)
        holder.search.text=name
        holder.card.setOnClickListener{
            listener.onCitySelected(name)
        }

    }


}