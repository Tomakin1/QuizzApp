package com.example.quizzproject.Activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.example.quizzproject.R
import com.example.quizzproject.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        val anim = AnimationUtils.loadAnimation(this, R.anim.myanim)
        binding.appName.startAnimation(anim)


        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this@SplashActivity, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000)

    }
}
