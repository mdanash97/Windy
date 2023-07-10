package com.example.windy

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView

class SplashScreenActivity : AppCompatActivity() {

    lateinit var lottieAnimationView: LottieAnimationView
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_splash_screen)

        lottieAnimationView = findViewById(R.id.animation)
        lottieAnimationView.setAnimation(R.raw.windsplash)

        sharedPreferences = this.getSharedPreferences("MyPreferences", MODE_PRIVATE)

        val location = sharedPreferences.getString("Location","Using Map")

        Handler().postDelayed({
            when(location){
                "Using Map" -> {
                    val intent = Intent(this@SplashScreenActivity, MapActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                "Using GPS" ->{
                    val intent = Intent(this@SplashScreenActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }, 4200)
    }
}