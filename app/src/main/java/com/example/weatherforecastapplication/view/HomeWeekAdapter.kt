package com.example.weatherforecastapplication.view

import android.content.Context
import android.content.res.Resources
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
import com.example.weatherforecastapplication.model2.SharedPreferencesManager
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
class HomeWeekAdapter(private val context: Context) : RecyclerView.Adapter<HomeWeekAdapter.HomeWeekAdapterViewHolder>() {

    private var listOfWeatherToday = listOf<Listt>()
    private val resources: Resources = context.resources

    fun setData(data: List<Listt>) {
        val currentDate = getCurrentDate()
        listOfWeatherToday = data.filterNot { item ->
            item.dt_txt.substring(0, 10) == currentDate
        }.groupBy { it.dt_txt.substring(0, 10) }.values.map { it.first() }
        notifyDataSetChanged()
    }
    fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(Date())
    }
    private fun getDayNameFromDateString(dateString: String): String {
        val language = SharedPreferencesManager.getInstance(context).getLanguageUnit()
        val locale = Locale(language)
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", locale)
        val date = sdf.parse(dateString)
        val calendar = Calendar.getInstance().apply {
            time = date
        }
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        val dayName = when (dayOfWeek) {
            Calendar.SUNDAY -> R.string.sunday
            Calendar.MONDAY -> R.string.monday
            Calendar.TUESDAY -> R.string.tuesday
            Calendar.WEDNESDAY -> R.string.wednesday
            Calendar.THURSDAY -> R.string.thursday
            Calendar.FRIDAY -> R.string.friday
            Calendar.SATURDAY -> R.string.saturday
            else -> R.string.saturday
        }
        return resources.getString(dayName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeWeekAdapterViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.wether_item, parent, false)
        return HomeWeekAdapterViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeWeekAdapterViewHolder, position: Int) {
        val currentItem = listOfWeatherToday[position]

        holder.name.text = currentItem.weather[0].description.toString()
//        Glide.with(context)
//            .load(getIconUrl(currentItem.weather[0].icon))
//            .apply(RequestOptions().placeholder(R.drawable.ic_launcher_foreground))
//            .transition(DrawableTransitionOptions.withCrossFade())
//            .into(holder.thumbnail)
        holder.thumbnail.setImageResource(getIconUrl(currentItem.weather[0].icon))

        // Convert date to day name and set it to the TextView
        val dayName = getDayNameFromDateString(currentItem.dt_txt)
        holder.day.text = dayName
        holder.temp.text="${currentItem.main.temp_max.toInt().toString()}"+ "/" +"${currentItem.main.temp_min.toInt().toString()}"
    }

    override fun getItemCount(): Int {
        return listOfWeatherToday.size
    }

//    private fun getIconUrl(iconCode: String): String {
//        return "https://openweathermap.org/img/w/$iconCode.png"
//    }
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
    class HomeWeekAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var thumbnail: ImageView = view.findViewById(R.id.product_img)
        var name: TextView = view.findViewById(R.id.product_name)
        var day: TextView = view.findViewById(R.id.Day_Name)
        var temp: TextView = view.findViewById(R.id.temp_week)
    }
}
