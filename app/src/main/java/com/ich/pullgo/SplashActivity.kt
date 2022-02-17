package com.ich.pullgo

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.ich.pullgo.presentation.login.LoginActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(R.layout.activity_splash)
        val intent = Intent(this, LoginActivity::class.java)
        val handler = Handler(mainLooper)
        handler.postDelayed({
            startActivity(intent)
            finish()
        }, 2000)
    }
}