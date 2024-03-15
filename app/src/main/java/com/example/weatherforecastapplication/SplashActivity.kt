package com.example.weatherforecastapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.airbnb.lottie.LottieAnimationView

class SplashActivity : AppCompatActivity() {
    private lateinit var lottieAnimationView: LottieAnimationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        lottieAnimationView = findViewById(R.id.animationView)
        Handler().postDelayed({
            Log.i("TAG", "SplashActivity")
            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)
            finish()
        }, 3000)

    }
}