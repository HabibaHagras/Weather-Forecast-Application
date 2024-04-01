package com.example.weatherforecastapplication.view.home_view

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
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




        val dayName = getDayNameFromDateString(currentItem.dt_txt)
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

        holder.day.text = dayName
        val tempNumbermin = currentItem.main.temp_min.toInt()
        val formattedtempNumbermin= if (SharedPreferencesManager.getInstance(context).getLanguageUnit()=="ar") {
            val arabicNumber = convertNumber().convertNumberToArabic(tempNumbermin)
            "$arabicNumber"
        } else {
            "$tempNumbermin"
        }
        val tempNumbermax =currentItem.main.temp_max.toInt()
        val formattedtempNumbermax= if (SharedPreferencesManager.getInstance(context).getLanguageUnit()=="ar") {
            val arabicNumber = convertNumber().convertNumberToArabic(tempNumbermax)
            "$arabicNumber"
        } else {
            "$tempNumbermax"
        }
        holder.temp.text="$formattedtempNumbermax"+ "/" +"$formattedtempNumbermin $currentTemp"
    }

    override fun getItemCount(): Int {
        return listOfWeatherToday.size
    }

    class HomeWeekAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var thumbnail: ImageView = view.findViewById(R.id.product_img)
        var name: TextView = view.findViewById(R.id.product_name)
        var day: TextView = view.findViewById(R.id.Day_Name)
        var temp: TextView = view.findViewById(R.id.temp_week)
    }
}
