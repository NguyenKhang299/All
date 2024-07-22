package com.image.effect.timewarp.scan.filtertiktok.face.filter.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxAdListener
import com.applovin.mediation.MaxError
import com.applovin.mediation.ads.MaxAppOpenAd
import com.bumptech.glide.Glide
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import com.image.effect.timewarp.scan.filtertiktok.face.filter.Const
import com.image.effect.timewarp.scan.filtertiktok.face.filter.R
import com.image.effect.timewarp.scan.filtertiktok.face.filter.ad.BaseAdRevenueListener
import com.image.effect.timewarp.scan.filtertiktok.face.filter.ad.MaxAds
import com.image.effect.timewarp.scan.filtertiktok.face.filter.databinding.ActivitySplashBinding
import io.github.inflationx.viewpump.ViewPumpContextWrapper

class SplashActivity : BaseAppCompatActivity() {
    private val TAG = "SplashActivity"
    private lateinit var binding: ActivitySplashBinding

    private var appOpenAd: AppOpenAd? = null
    private var timer: CountDownTimer? = null
    private var tickCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
        if (MaxAds.canShowAd(this)) {
            loadAd()
        } else {
            Handler(Looper.getMainLooper()).postDelayed({
                openMain()
            }, 1500)
        }
    }

    private fun initViews() {
        Glide.with(this).load(R.mipmap.ic_launcher_round).into(binding.imvIcon)
        Glide.with(this).load(R.drawable.img_appname).into(binding.imvAppname)
    }

    fun loadAd() {
        Log.e(TAG, "loadAd: ")
        timer = object : CountDownTimer(9500, 1500) {
            override fun onTick(p0: Long) {
                if (tickCount > 0) {
                    Log.e(TAG, "onTick: ")
                    if (appOpenAd != null) {
                        timer?.cancel()
                        appOpenAd?.show(this@SplashActivity)
                    }
                }
                tickCount++
            }

            override fun onFinish() {
                openMain()
            }
        }.start()

        AppOpenAd.load(
            this,
            Const.AdmobOpenAdId,
            AdRequest.Builder().build(),
            AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,
            object : AppOpenAd.AppOpenAdLoadCallback() {
                override fun onAdLoaded(ad: AppOpenAd) {
                    Log.d(TAG, "Ad was loaded.")
                    appOpenAd = ad
                    appOpenAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                        override fun onAdDismissedFullScreenContent() {
                            openMain()
                        }

                        override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                            openMain()
                        }
                    }
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    timer?.cancel()
                    openMain()
                }
            })
    }

    private fun openMain() {
        if (isFinishing) return
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }
}