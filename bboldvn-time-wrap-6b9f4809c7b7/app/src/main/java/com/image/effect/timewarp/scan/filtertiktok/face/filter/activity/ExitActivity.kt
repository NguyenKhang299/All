package com.image.effect.timewarp.scan.filtertiktok.face.filter.activity

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.image.effect.timewarp.scan.filtertiktok.face.filter.databinding.ActivityExitBinding
import io.github.inflationx.viewpump.ViewPumpContextWrapper

class ExitActivity : BaseAppCompatActivity() {
    private lateinit var binding: ActivityExitBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityExitBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
        startTimer()
    }

    private fun initViews() {

    }

    private fun startTimer() {
        Handler(Looper.getMainLooper()).postDelayed({
            finish()
        }, 3000)
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }
}