package com.image.effect.timewarp.scan.filtertiktok.face.filter.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxError
import com.applovin.mediation.ads.MaxInterstitialAd
import com.bumptech.glide.Glide
import com.image.effect.timewarp.scan.filtertiktok.face.filter.Const
import com.image.effect.timewarp.scan.filtertiktok.face.filter.R
import com.image.effect.timewarp.scan.filtertiktok.face.filter.ad.BaseAdRevenueListener
import com.image.effect.timewarp.scan.filtertiktok.face.filter.ad.BaseMaxAdInterListener
import com.image.effect.timewarp.scan.filtertiktok.face.filter.ad.MaxAds
import com.image.effect.timewarp.scan.filtertiktok.face.filter.databinding.ActivityMainBinding
import com.image.effect.timewarp.scan.filtertiktok.face.filter.fragment.GalleryFragment
import com.image.effect.timewarp.scan.filtertiktok.face.filter.fragment.TrendingFragment
import com.image.effect.timewarp.scan.filtertiktok.face.filter.iap.IapConfig
import com.image.effect.timewarp.scan.filtertiktok.face.filter.iap.lib.MobileBilling
import io.github.inflationx.viewpump.ViewPumpContextWrapper

class MainActivity : BaseAppCompatActivity() {

    public lateinit var binding: ActivityMainBinding
//    var doubleBackToExitPressedOnce = false

    public var interAd: MaxInterstitialAd? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
        loadAd()
    }

//    override fun onResume() {
//        super.onResume()
//        Log.e("MAINNNNN", "onResume: isPurchaseAd: ${MobileBilling.isPurchasedSku(this, IapConfig.AD_FREE)}")
//    }

    private fun initViews() {
        Glide.with(this).load(R.mipmap.ic_launcher_round).into(binding.imvIcon)

        val pagerAdapter = SimplePagerAdapter(this)
        binding.viewpager.adapter = pagerAdapter
        binding.viewpager.isUserInputEnabled = false
        binding.viewpager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position == 0) {
                    binding.txtTitle.text = getString(R.string.hot_trending_video)
                    binding.layoutBottomBar.btnTrending.setImageResource(R.drawable.ic_home_active)
                    binding.layoutBottomBar.btnTrending.alpha = 1f
                    binding.layoutBottomBar.tvTabTrending.setTextColor(getColor(R.color.primary))
                    binding.layoutBottomBar.tvTabTrending.alpha = 1f

                    binding.layoutBottomBar.btnGallery.setImageResource(R.drawable.ic_gallery)
                    binding.layoutBottomBar.btnGallery.alpha = 0.5f
                    binding.layoutBottomBar.tvTabGallery.setTextColor(getColor(R.color.white))
                    binding.layoutBottomBar.tvTabGallery.alpha = 0.5f
                } else {
                    binding.txtTitle.text = getString(R.string.my_gallery)
                    binding.layoutBottomBar.btnTrending.setImageResource(R.drawable.ic_home)
                    binding.layoutBottomBar.btnTrending.alpha = 0.5f
                    binding.layoutBottomBar.tvTabTrending.setTextColor(getColor(R.color.white))
                    binding.layoutBottomBar.tvTabTrending.alpha = 0.5f

                    binding.layoutBottomBar.btnGallery.setImageResource(R.drawable.ic_gallery_active)
                    binding.layoutBottomBar.btnGallery.alpha = 1f
                    binding.layoutBottomBar.tvTabGallery.setTextColor(getColor(R.color.primary))
                    binding.layoutBottomBar.tvTabGallery.alpha = 1f
                }
            }
        })

        binding.btnSetting.setOnClickListener {
            startActivity(Intent(this, SettingActivity::class.java))
        }

        binding.layoutBottomBar.btnRecord.setOnClickListener {
            startActivity(Intent(this, CameraActivity::class.java))
        }

        binding.layoutBottomBar.btnTrending.setOnClickListener {
            binding.viewpager.currentItem = 0
        }
        binding.layoutBottomBar.tvTabTrending.setOnClickListener {
            binding.viewpager.currentItem = 0
        }

        binding.layoutBottomBar.btnGallery.setOnClickListener {
            binding.viewpager.currentItem = 1
        }
        binding.layoutBottomBar.tvTabGallery.setOnClickListener {
            binding.viewpager.currentItem = 1
        }
    }

    private fun loadAd() {
        MaxAds.loadBannerAd(this, binding.bannerAdContainer, "banner_main")

        if (MaxAds.canShowAd(this)) {
            interAd = MaxInterstitialAd(Const.MaxFullId, this)
            interAd?.setRevenueListener(BaseAdRevenueListener())
            interAd?.setListener(object : BaseMaxAdInterListener(interAd) {
                override fun onAdLoadFailed(p0: String, p1: MaxError) {
                 }

                override fun onAdDisplayFailed(p0: MaxAd, p1: MaxError) {
                    super.onAdDisplayFailed(p0, p1)
                    openExitActivity()
                }


                override fun onAdHidden(maxAd: com.applovin.mediation.MaxAd) {
                    super.onAdHidden(maxAd)
                    openExitActivity()
                }
            })
            interAd?.loadAd()
        }
    }

    private fun showAdIfReady() {
        if (MaxAds.canShowAd(this) && interAd?.isReady == true) {
            interAd?.showAd("app_exit")
        } else {
            openExitActivity()
        }
    }

    private fun openExitActivity() {
        val intent = Intent(this, ExitActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        if (binding.viewpager.currentItem != 0) {
            binding.viewpager.currentItem = 0
            return
        }

//        if (doubleBackToExitPressedOnce) {
        showAdIfReady()
//            return
//        }
//        this.doubleBackToExitPressedOnce = true
//        Toast.makeText(this, "Press Back again to Exit", Toast.LENGTH_SHORT).show()
//        Handler(Looper.getMainLooper()).postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }

    private inner class SimplePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = 2

        override fun createFragment(position: Int): Fragment {
            return if (position == 0) TrendingFragment() else GalleryFragment()
        }
    }

}