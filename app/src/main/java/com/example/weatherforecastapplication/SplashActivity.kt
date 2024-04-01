package com.example.weatherforecastapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.airbnb.lottie.LottieAnimationView
import com.example.weatherforecastapplication.model2.SharedPreferencesManager

class SplashActivity : AppCompatActivity() {
    companion object {
        lateinit var instance: SplashActivity
    }
    private lateinit var lottieAnimationView: LottieAnimationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        lottieAnimationView = findViewById(R.id.animationView)
        instance = this
        Handler().postDelayed({
            Log.i("TAG", "SplashActivity")
            val intent = Intent(this, MainActivity2::class.java)
            intent.putExtra("fragment_to_load", "YourFragmentTag")
            startActivity(intent)
            finish()
        }, 3000)

    }
}