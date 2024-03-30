package com.example.weatherforecastapplication.view.home_view
import android.content.Context
import android.graphics.drawable.Drawable
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.weatherforecastapplication.R

class IconUrl {
     fun getIconDrawable(iconCode: String, context: Context): Drawable {
        return when (iconCode) {
            "01d" -> ContextCompat.getDrawable(context, R.drawable.animated_weather)!!
            "01n" -> ContextCompat.getDrawable(context, R.drawable.icon01n)!!
            "02d" -> ContextCompat.getDrawable(context, R.drawable.icon02d)!!
            "02n" -> ContextCompat.getDrawable(context, R.drawable.icon02n)!!
            "03d", "03n" -> ContextCompat.getDrawable(context, R.drawable.icon03d)!!
            "04d", "04n" -> ContextCompat.getDrawable(context, R.drawable.icon03d)!!
            "09d", "09n" -> ContextCompat.getDrawable(context, R.drawable.icon_09d)!!
            "10d" -> ContextCompat.getDrawable(context, R.drawable.icon10d)!!
            "10n" -> ContextCompat.getDrawable(context, R.drawable.icon10n)!!
            "11d", "11n" -> ContextCompat.getDrawable(context, R.drawable.icon11d)!!
            "13d", "13n" -> ContextCompat.getDrawable(context, R.drawable.icon13d)!!
            "50d", "50n" -> ContextCompat.getDrawable(context, R.drawable.icon50d)!!
            else -> ContextCompat.getDrawable(context, R.drawable.animated_weather)!! // Default icon for unknown weather conditions
        }

    }


}
