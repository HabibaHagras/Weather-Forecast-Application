package com.example.weatherforecastapplication.view

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.weatherforecastapplication.R
import com.example.weatherforecastapplication.model2.Listt
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HomeWeekAdapter (private val context: Context) : RecyclerView.Adapter<HomeWeekAdapter.HomeWeekAdapterViewHolder>() {

    private var listOfWeatherToday = listOf<Listt>()

    fun setData(data: List<Listt>) {
        val currentDate = getCurrentDate()
        listOfWeatherToday = data.filterNot { item ->
            item.dt_txt.startsWith(currentDate)
        }
        notifyDataSetChanged()
    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(Date())
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeWeekAdapterViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.wether_item, parent, false)
        return HomeWeekAdapterViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeWeekAdapterViewHolder, position: Int) {
        val currentItem = listOfWeatherToday[position]

        holder.name.text = currentItem.main.temp.toString()
        Glide.with(context)
            .load(getIconUrl(currentItem.weather[0].icon))
            .apply(RequestOptions().placeholder(R.drawable.ic_launcher_foreground))
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(holder.thumbnail)
    }

    override fun getItemCount(): Int {
        return listOfWeatherToday.size
    }

    private fun getIconUrl(iconCode: String): String {
        return "https://openweathermap.org/img/w/$iconCode.png"
    }

    class HomeWeekAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var thumbnail: ImageView = view.findViewById(R.id.product_img)
        var name: TextView = view.findViewById(R.id.product_name)
    }
}