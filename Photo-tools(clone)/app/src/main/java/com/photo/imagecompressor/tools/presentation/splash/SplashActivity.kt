package com.photo.imagecompressor.tools.presentation.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.photo.imagecompressor.tools.R
import com.photo.imagecompressor.tools.base.RemoteConfigHelper
import com.photo.imagecompressor.tools.data.RemoteHelper
import com.photo.imagecompressor.tools.presentation.main.MainActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)
        RemoteConfigHelper.getConfigData()
        Handler(Looper.myLooper()!!).postDelayed({
            if (!RemoteHelper.IS_FAILED){
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }, 3000)
    }
}