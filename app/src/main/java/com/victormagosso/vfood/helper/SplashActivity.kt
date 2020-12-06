package com.victormagosso.vfood.helper

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowInsets
import android.view.WindowManager
import com.victormagosso.vfood.MainActivity
import com.victormagosso.vfood.R
import com.victormagosso.vfood.login.AuthActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController!!.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        Handler(Looper.getMainLooper()).postDelayed({
            run() {
                openAuthScreen()
            }
        }, 3000)
    }
    private fun openAuthScreen() {
        startActivity(Intent(this, AuthActivity::class.java))
        finish()
    }
}