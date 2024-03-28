package com.example.weatherforecastapplication.view.home_view

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecastapplication.R
import com.example.weatherforecastapplication.model2.Listt
import com.example.weatherforecastapplication.model2.SharedPreferencesManager
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
        val language = SharedPreferencesManager.getInstance(context).getLanguageUnit()
        val locale = Locale("en")
//        Locale.getDefault()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd",locale)
        return dateFormat.format(Date())
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.wether_item_today, parent, false)
        return FavViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavViewHolder, position: Int) {
        val currentItem = listOfWeatherToday[position]
        var currentTemp="°C"
        when (SharedPreferencesManager.getInstance(context).getUnits()) {
            "metric" -> currentTemp = "°C"
            "imperial" -> currentTemp = "F"
            ""->currentTemp = "K"
            else -> "K"}
        holder.name.text = "${currentItem.main.temp.toInt().toString()} $currentTemp"
        holder.thumbnail.setImageResource(getIconUrl(currentItem.weather[0].icon))
        val timeFormatted = formatTime(currentItem.dt_txt.substring(11))
        holder.clock.text = timeFormatted
    }
    private fun getIconUrl(iconCode: String): Int {
        return when (iconCode) {
            "01d" -> R.drawable.day_forecast_sun_sunny_weather_icon
            "01n" -> R.drawable.eclipse_forecast_moon_night_space_icon
            "02d" -> R.drawable.cloud_cloudy_day_forecast_sun_icon
            "02n" -> R.drawable.weather_clouds_cloudy_moon_icon
            "03d", "03n" -> R.drawable.weather_cloud_clouds_cloudy_icon
            "04d", "04n" -> R.drawable.weather_cloud_clouds_cloudyy_icon
            "09d", "09n" -> R.drawable.clouds_cloudy_foggy_weather_icon
            "10d" -> R.drawable.weather_clouds_cloudy_forecast_rain_icon
            "10n" -> R.drawable.weather_clouds_cloudy_rain_sunny_icon
            "11d", "11n" -> R.drawable.cloud_flash_weather_rain_snow_icon
            "13d", "13n" -> R.drawable.weather_storm_icon
            "50d", "50n" -> R.drawable.rain_snowflake_snow_cloud_winter_icon
            else -> R.drawable.cloud_white_24dp // Default icon for unknown weather conditions
        }
    }


    private fun formatTime(time: String): String {
        val language = SharedPreferencesManager.getInstance(context).getLanguageUnit()
        val locale = Locale("en")
        val sdf = SimpleDateFormat("HH:mm",locale)
        val parsedDate = sdf.parse(time)
        val sdf12hr = SimpleDateFormat("hh:mm a",locale)
        return sdf12hr.format(parsedDate!!)
    }
    override fun getItemCount(): Int {
        return listOfWeatherToday.size
    }

//    private fun getIconUrl(iconCode: String): String {
//        return "https://openweathermap.org/img/w/$iconCode.png"
//    }

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