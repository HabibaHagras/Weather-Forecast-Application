




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
import com.example.weatherforecastapplication.model2.Responce
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HomeAdapter(private val context: Context) : RecyclerView.Adapter<HomeAdapter.FavViewHolder>() {

    private var listOfWeatherToday = listOf<Listt>()

    fun setData(data: List<Listt>) {
        listOfWeatherToday = data
        notifyDataSetChanged()
    }
    fun setDataAndFilterByDate(data: List<Listt>) {
        val currentDate = getCurrentDate()
        Log.i("TAG", "setDataAndFilterByDate: $currentDate ")
        listOfWeatherToday = data.filter { item ->
           item.dt_txt.startsWith(currentDate)
        }
        notifyDataSetChanged()
    }
    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(Date())
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.wether_item_today, parent, false)
        return FavViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavViewHolder, position: Int) {
        val currentItem = listOfWeatherToday[position]

        holder.name.text = currentItem.main.temp.toInt().toString()
        Glide.with(context)
            .load(getIconUrl(currentItem.weather[0].icon))
            .apply(RequestOptions().placeholder(R.drawable.ic_launcher_foreground))
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(holder.thumbnail)
        val timeFormatted = formatTime(currentItem.dt_txt.substring(11))
        holder.clock.text = timeFormatted
    }
    private fun formatTime(time: String): String {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        val parsedDate = sdf.parse(time)
        val sdf12hr = SimpleDateFormat("hh:mm a", Locale.getDefault())
        return sdf12hr.format(parsedDate)
    }
    override fun getItemCount(): Int {
        return listOfWeatherToday.size
    }

    private fun getIconUrl(iconCode: String): String {
        return "https://openweathermap.org/img/w/$iconCode.png"
    }

    class FavViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var thumbnail: ImageView = view.findViewById(R.id.product_img_today)
        var name: TextView = view.findViewById(R.id.product_name_today)
        var clock: TextView = view.findViewById(R.id.clock_Name_today)
    }
}

//package com.example.weatherforecastapplication.view
//
//import android.content.Context
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageView
//import android.widget.TextView
//import androidx.cardview.widget.CardView
//import androidx.recyclerview.widget.DiffUtil
//import androidx.recyclerview.widget.ListAdapter
//import androidx.recyclerview.widget.RecyclerView
//import com.bumptech.glide.Glide
//import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
//import com.bumptech.glide.request.RequestOptions
//import com.example.weatherforecastapplication.R
//import com.example.weatherforecastapplication.model2.Listt
//import com.example.weatherforecastapplication.model2.Responce
//
//class HomeAdapter : ListAdapter<Responce, HomeAdapter.FavViewHolder>(FavDiffUtil()){
//       private var listOfWeatherToday= listOf<Listt>()
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavViewHolder {
//        val inflater : LayoutInflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//        val view = inflater.inflate(R.layout.wether_item,parent,false)
//        return FavViewHolder(view)
//
//    }
////    override fun onBindViewHolder(holder: FavViewHolder, position: Int) {
////
////     val currentObj = getItem(position)
////
////        holder.name.text = currentObj.list[position].main.temp.toString()
////        Glide.with(holder.itemView.context)
////            .load(getIconUrl(currentObj.list[position].weather[0].icon))
////            .apply(RequestOptions().placeholder(R.drawable.ic_launcher_foreground))
////            .transition(DrawableTransitionOptions.withCrossFade())
////            .into(holder.thumbnail)
////
//////        holder.name.text = currentObj.list[0].main.temp.toString()
//////
//////        // Assuming you want to display the icon from the first item in the list
//////        Glide.with(holder.itemView.context)
//////            .load(getIconUrl(currentObj.list[0].weather[0].icon))
//////            .apply(RequestOptions().placeholder(R.drawable.ic_launcher_foreground))
//////            .transition(DrawableTransitionOptions.withCrossFade())
//////            .into(holder.thumbnail)
//////        for (i in 1 until currentObj.list.size) {
//////            val additionalForecast = currentObj.list[i]
////////        for (i in currentObj.list.indices) {
//////            Log.i("TAG", "onBindViewHolder:  $i")
//////            holder.name.text = additionalForecast.main.temp.toString()
//////        Glide.with(holder.itemView.context)
//////            .load(getIconUrl(additionalForecast.weather[0].icon))
//////            .apply(RequestOptions().placeholder(R.drawable.ic_launcher_foreground))
//////            .transition(DrawableTransitionOptions.withCrossFade())
//////            .into(holder.thumbnail)
//////    }
////    }
//
//    override fun onBindViewHolder(holder: FavViewHolder, position: Int) {
//        val currentObj = getItem(position)
//        holder.name.text = currentObj.list[0].main.temp.toString()
//        Log.i("TAG", "onBindViewHolder:  $currentObj")
////        holder.name.text = currentObjmain.temp.toString()
//        // Assuming you want to display the icon and temperature from the current item in the list
//        Glide.with(holder.itemView.context)
//            .load(getIconUrl(currentObj.list[0].weather[0].icon))
//            .apply(RequestOptions().placeholder(R.drawable.ic_launcher_foreground))
//            .transition(DrawableTransitionOptions.withCrossFade())
//            .into(holder.thumbnail)
////
////        holder.name.text = currentObj.list[0].main.temp.toString()
////               for (i in 1 until currentObj.list.size) {
////            val additionalForecast = currentObj.list[i]
//////        for (i in currentObj.list.indices) {
////            Log.i("TAG", "onBindViewHolder:  $position")
////            holder.name.text = additionalForecast.main.temp.toString()
////        Glide.with(holder.itemView.context)
////            .load(getIconUrl(additionalForecast.weather[0].icon))
////            .apply(RequestOptions().placeholder(R.drawable.ic_launcher_foreground))
////            .transition(DrawableTransitionOptions.withCrossFade())
////            .into(holder.thumbnail)
////    }
//    }
//
//    class FavViewHolder (view : View): RecyclerView.ViewHolder(view){
//        var thumbnail : ImageView = view.findViewById(R.id.product_img)
//        var name : TextView = view.findViewById(R.id.product_name)
//        val card : CardView = view.findViewById(R.id.card_view)
//
//    }
//    private fun getIconUrl(iconCode: String): String {
//        return "https://openweathermap.org/img/w/$iconCode.png"
//    }}
//
//class FavDiffUtil : DiffUtil.ItemCallback<Responce>() {
//    override fun areItemsTheSame(oldItem: Responce, newItem: Responce): Boolean {
//        return oldItem.list == newItem.list
//    }
//
//    override fun areContentsTheSame(oldItem: Responce, newItem: Responce): Boolean {
//        return oldItem == newItem
//    }
//
//}