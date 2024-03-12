package com.example.weatherforecastapplication.view

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.weatherforecastapplication.R
import com.example.weatherforecastapplication.model.ForecastEntry

class HomeAdapter : ListAdapter<ForecastEntry, HomeAdapter.FavViewHolder>(FavDiffUtil()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavViewHolder {
        val inflater : LayoutInflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.wether_item,parent,false)
        return FavViewHolder(view)

    }
//    override fun onBindViewHolder(holder: FavViewHolder, position: Int) {
//
//     val currentObj = getItem(position)
//
//        holder.name.text = currentObj.list[position].main.temp.toString()
//        Glide.with(holder.itemView.context)
//            .load(getIconUrl(currentObj.list[position].weather[0].icon))
//            .apply(RequestOptions().placeholder(R.drawable.ic_launcher_foreground))
//            .transition(DrawableTransitionOptions.withCrossFade())
//            .into(holder.thumbnail)
//
////        holder.name.text = currentObj.list[0].main.temp.toString()
////
////        // Assuming you want to display the icon from the first item in the list
////        Glide.with(holder.itemView.context)
////            .load(getIconUrl(currentObj.list[0].weather[0].icon))
////            .apply(RequestOptions().placeholder(R.drawable.ic_launcher_foreground))
////            .transition(DrawableTransitionOptions.withCrossFade())
////            .into(holder.thumbnail)
////        for (i in 1 until currentObj.list.size) {
////            val additionalForecast = currentObj.list[i]
//////        for (i in currentObj.list.indices) {
////            Log.i("TAG", "onBindViewHolder:  $i")
////            holder.name.text = additionalForecast.main.temp.toString()
////        Glide.with(holder.itemView.context)
////            .load(getIconUrl(additionalForecast.weather[0].icon))
////            .apply(RequestOptions().placeholder(R.drawable.ic_launcher_foreground))
////            .transition(DrawableTransitionOptions.withCrossFade())
////            .into(holder.thumbnail)
////    }
//    }

    override fun onBindViewHolder(holder: FavViewHolder, position: Int) {
        val currentObj = getItem(position)

//        // Assuming you want to display the icon and temperature from the current item in the list
//        Glide.with(holder.itemView.context)
//            .load(getIconUrl(currentObj.list[0].weather[0].icon))
//            .apply(RequestOptions().placeholder(R.drawable.ic_launcher_foreground))
//            .transition(DrawableTransitionOptions.withCrossFade())
//            .into(holder.thumbnail)
//
//        holder.name.text = currentObj.list[0].main.temp.toString()
               for (i in 1 until currentObj.list.size) {
            val additionalForecast = currentObj.list[i]
//        for (i in currentObj.list.indices) {
            Log.i("TAG", "onBindViewHolder:  $position")
            holder.name.text = additionalForecast.main.temp.toString()
        Glide.with(holder.itemView.context)
            .load(getIconUrl(additionalForecast.weather[0].icon))
            .apply(RequestOptions().placeholder(R.drawable.ic_launcher_foreground))
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(holder.thumbnail)
    }
    }

    class FavViewHolder (view : View): RecyclerView.ViewHolder(view){
        var thumbnail : ImageView = view.findViewById(R.id.product_img)
        var name : TextView = view.findViewById(R.id.product_name)
        val card : CardView = view.findViewById(R.id.card_view)

    }
    private fun getIconUrl(iconCode: String): String {
        return "https://openweathermap.org/img/w/$iconCode.png"
    }}

class FavDiffUtil : DiffUtil.ItemCallback<ForecastEntry>() {
    override fun areItemsTheSame(oldItem: ForecastEntry, newItem: ForecastEntry): Boolean {
        return oldItem.list == newItem.list
    }

    override fun areContentsTheSame(oldItem: ForecastEntry, newItem: ForecastEntry): Boolean {
        return oldItem == newItem
    }

}