package com.example.weatherforecastapplication.view.home_view

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
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
        if (SharedPreferencesManager.getInstance(context).getLanguageUnit()=="en"){
            when (SharedPreferencesManager.getInstance(context).getUnits()) {

                "metric" -> currentTemp = "°C"
                "imperial" -> currentTemp = "F"
                ""->currentTemp = "K"
                else -> "K"}}else{
            when (SharedPreferencesManager.getInstance(context).getUnits()) {

                "metric" -> currentTemp = "سيليزي"
                "imperial" -> currentTemp = " فهرنهاي"
                ""->currentTemp ="كلفن"
                else -> " كلفن"}
        }
        val tempNumber = currentItem.main.temp.toInt()
        val formattedtempNumber= if (SharedPreferencesManager.getInstance(context).getLanguageUnit()=="ar") {
            val arabicNumber = convertNumber().convertNumberToArabic(tempNumber)
            "$arabicNumber $currentTemp"
        } else {
            "$tempNumber $currentTemp"
        }
        holder.name.text =  formattedtempNumber
//        holder.thumbnail.setImageResource(getIconUrl(currentItem.weather[0].icon))
        val iconUrl = IconUrl().getIconDrawable(currentItem.weather[0].icon, context)
        Glide.with(holder.itemView)
            .load(iconUrl)
            .listener(object : RequestListener<Drawable> {
                override fun onResourceReady(
                    resource: Drawable,
                    model: Any,
                    target: com.bumptech.glide.request.target.Target<Drawable>?,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    val rotateAnimation = RotateAnimation(-10f, 10f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
                    rotateAnimation.duration = 1000 // 1 second
                    rotateAnimation.interpolator = LinearInterpolator()
                    rotateAnimation.repeatCount = Animation.INFINITE // Repeat indefinitely
                    holder.thumbnail.startAnimation(rotateAnimation)
                    return false                }

                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: com.bumptech.glide.request.target.Target<Drawable>,
                    isFirstResource: Boolean
                ): Boolean {
                    TODO("Not yet implemented")
                }
            })
            .into(holder.thumbnail)

        val timeFormatted = formatTime(currentItem.dt_txt.substring(11))
        holder.clock.text = timeFormatted
    }
    private fun getIconUrl(iconCode: String): Int {
        return when (iconCode) {
            "01d" -> R.drawable.day_forecast_sun_sunny_weather_icon
            "01n" -> R.drawable.eclipse_forecast_moon_night_space_iconn
            "02d" -> R.drawable.cloud_cloudy_day_forecast_sun_iconn
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

    class FavViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var thumbnail: ImageView = view.findViewById(R.id.product_img_today)
        var name: TextView = view.findViewById(R.id.product_name_today)
        var clock: TextView = view.findViewById(R.id.clock_Name_today)
    }
}
