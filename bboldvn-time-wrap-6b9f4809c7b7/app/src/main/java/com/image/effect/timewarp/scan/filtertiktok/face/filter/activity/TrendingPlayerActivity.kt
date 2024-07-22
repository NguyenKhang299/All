package com.image.effect.timewarp.scan.filtertiktok.face.filter.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxError
import com.applovin.mediation.ads.MaxInterstitialAd
import com.bumptech.glide.Glide
import com.image.effect.timewarp.scan.filtertiktok.face.filter.Const
import com.image.effect.timewarp.scan.filtertiktok.face.filter.R
import com.image.effect.timewarp.scan.filtertiktok.face.filter.ad.BaseAdRevenueListener
import com.image.effect.timewarp.scan.filtertiktok.face.filter.ad.BaseMaxAdInterListener
import com.image.effect.timewarp.scan.filtertiktok.face.filter.ad.MaxAds
import com.image.effect.timewarp.scan.filtertiktok.face.filter.databinding.ActivityPlayerBinding
import com.image.effect.timewarp.scan.filtertiktok.face.filter.model.TrendingVideo
import com.potyvideo.library.globalEnums.EnumRepeatMode
import io.github.inflationx.viewpump.ViewPumpContextWrapper

class TrendingPlayerActivity : BaseAppCompatActivity() {
    val TAG = "PlayerActivity"

    companion object {
        const val ExtraTrendingVideo = "extra_trending_video"
    }

    private lateinit var binding: ActivityPlayerBinding
    private var trendingVideo: TrendingVideo? = null

    var interAd: MaxInterstitialAd? = null
    var willGoToCameraActivity: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
        loadAd()
    }

    private fun initViews() {
        Glide.with(this).load(R.drawable.bg_try_effect).into(binding.bgLayoutRecord)
        binding.imvBack.setOnClickListener {
            onBackPressed()
        }
        binding.layoutRecord.setOnClickListener {
            willGoToCameraActivity = true
            showAdIfReady()
        }

        // video
        val exo = binding.andExoPlayerView
        exo.setShowControllers(false)
        exo.setShowControllers()
        exo.setRepeatMode(EnumRepeatMode.REPEAT_ALWAYS)
        exo.setPlayWhenReady(true)

        trendingVideo = intent.getSerializableExtra(ExtraTrendingVideo) as TrendingVideo?
        Log.e(TAG, "trendingVideo: $trendingVideo")
        if (trendingVideo != null) {
            binding.txtCredit.text = trendingVideo!!.credit
            binding.andExoPlayerView.setSource(trendingVideo!!.video_url!!)
        } else {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun loadAd() {
        MaxAds.loadBannerAd(this, binding.bannerAdContainer, "banner_player")

        if (MaxAds.canShowAd(this)) {
            interAd = MaxInterstitialAd(Const.MaxFullId, this)
            interAd?.setRevenueListener(BaseAdRevenueListener())
            interAd?.setListener(object : BaseMaxAdInterListener(interAd) {
                override fun onAdDisplayed(maxAd: com.applovin.mediation.MaxAd) {
                    super.onAdDisplayed(maxAd)
                    finish()
                }

                override fun onAdLoadFailed(p0: String, p1: MaxError) {
                 }



                override fun onAdDisplayFailed(p0: MaxAd, p1: MaxError) {
                    if (willGoToCameraActivity) {
                        goToCameraActivity()
                    }
                    finish()
                }

                override fun onAdHidden(maxAd: com.applovin.mediation.MaxAd) {
                    super.onAdHidden(maxAd)
                    if (willGoToCameraActivity) {
                        goToCameraActivity()
                    }
                    finish()
                }
            })
            interAd?.loadAd()
        }
    }

    private fun showAdIfReady() {
        if (MaxAds.canShowAd(this) && interAd?.isReady == true) {
            interAd?.showAd("trending_player_try_effect_clicked")
        } else {
            goToCameraActivity()
        }
    }

    private fun goToCameraActivity() {
        startActivity(Intent(this@TrendingPlayerActivity, CameraActivity::class.java))
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }
}