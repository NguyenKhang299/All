package com.image.effect.timewarp.scan.filtertiktok.face.filter.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ShareCompat
import androidx.core.content.FileProvider
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxError
import com.applovin.mediation.ads.MaxInterstitialAd
import com.bumptech.glide.Glide
import com.image.effect.timewarp.scan.filtertiktok.face.filter.BuildConfig
import com.image.effect.timewarp.scan.filtertiktok.face.filter.Const
import com.image.effect.timewarp.scan.filtertiktok.face.filter.Pref
import com.image.effect.timewarp.scan.filtertiktok.face.filter.R
import com.image.effect.timewarp.scan.filtertiktok.face.filter.ad.BaseAdRevenueListener
import com.image.effect.timewarp.scan.filtertiktok.face.filter.ad.BaseMaxAdInterListener
import com.image.effect.timewarp.scan.filtertiktok.face.filter.ad.MaxAds
import com.image.effect.timewarp.scan.filtertiktok.face.filter.databinding.ActivityShareBinding
import com.image.effect.timewarp.scan.filtertiktok.face.filter.util.UIUtils
import com.image.effect.timewarp.scan.filtertiktok.face.filter.util.Utils
import com.orhanobut.hawk.Hawk
import com.potyvideo.library.globalEnums.EnumRepeatMode
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import java.io.File

class ShareActivity : BaseAppCompatActivity() {
    val TAG = "ShareActivity"

    private lateinit var binding: ActivityShareBinding
    private var filePath: String? = null
    private var fileType: String? = null
    private var openedFrom: String? = null

    var interAd: MaxInterstitialAd? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityShareBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
        loadAd()

        showRatingIfNeeded()
    }

    private fun showRatingIfNeeded() {
        if ("camera" == openedFrom) {
            if (!Hawk.get(Pref.AppRated, false)) {
                UIUtils.showRatingDialog(this)
            }
        }
    }

    private fun initViews() {
        binding.toolbar.txtTitle.text = getString(R.string.share)
        binding.toolbar.imvBack.setOnClickListener {
            onBackPressed()
        }
        binding.toolbar.imvShare.visibility = View.GONE
        binding.toolbar.imvDelete.visibility = View.GONE

        binding.btnScan.container.setOnClickListener {
            showAdIdReady()
        }

        binding.btnShareTt.setOnClickListener {
            shareToApp(Const.PackageTiktok)
        }

        binding.btnShareFb.setOnClickListener {
            shareToApp(Const.PackageFacebook)
        }

        binding.btnShareIg.setOnClickListener {
            shareToApp(Const.PackageInstagram)
        }

        binding.btnShareMore.setOnClickListener {
            onShareMore()
        }

        // play video
        val exo = binding.andExoPlayerView
        exo.setShowControllers(false)
        exo.setShowControllers()
        exo.setRepeatMode(EnumRepeatMode.REPEAT_ALWAYS)
        exo.setPlayWhenReady(true)

        fileType = intent.getStringExtra("source_type")
        filePath = intent.getStringExtra("source_file")
        openedFrom = intent.getStringExtra("opened_from")
        Log.e(TAG, "filePath: $filePath")
        if (filePath != null) {
            if (fileType == "video") {
                binding.andExoPlayerView.visibility = View.VISIBLE
                binding.imageView.visibility = View.GONE
                binding.andExoPlayerView.setSource(filePath!!)
            } else {
                binding.andExoPlayerView.visibility = View.GONE
                binding.imageView.visibility = View.VISIBLE
                Glide.with(this).load(filePath).centerInside().into(binding.imageView)
            }
        } else {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun loadAd() {
        MaxAds.loadBannerAd(this, binding.bannerAdContainer, "banner_share")

        if (MaxAds.canShowAd(this)) {
            interAd = MaxInterstitialAd(Const.MaxFullId, this)
            interAd?.setRevenueListener(BaseAdRevenueListener())
            interAd?.setListener(object : BaseMaxAdInterListener(interAd) {
                override fun onAdLoadFailed(p0: String, p1: MaxError) {
                 }

                override fun onAdDisplayFailed(p0: MaxAd, p1: MaxError) {
                    super.onAdDisplayFailed(p0, p1)
                    openCameraActivity()
                }


                override fun onAdHidden(maxAd: com.applovin.mediation.MaxAd) {
                    super.onAdHidden(maxAd)
                    openCameraActivity()
                }
            })
            interAd?.loadAd()
        }
    }

    private fun showAdIdReady() {
        if (MaxAds.canShowAd(this) && interAd?.isReady == true) {
            interAd?.showAd("share_scan_next")
        } else {
            openCameraActivity()
        }
    }

    fun openCameraActivity() {
        if ("camera" == openedFrom) {
            finish()
        } else {
            startActivity(Intent(this, CameraActivity::class.java))
            finish()
        }
    }

    private fun shareToApp(packageName: String) {
        if (!Utils.isPackageInstalled(this, packageName)) {
            Toast.makeText(this, "App not found", Toast.LENGTH_SHORT).show()
            return
        }

        val share = Intent(Intent.ACTION_SEND)
        share.type = if(fileType == "video") "video/*" else "image/*"
        share.setPackage(packageName)

        val uri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", File(filePath!!))
        share.putExtra(Intent.EXTRA_STREAM, uri)
        startActivity(Intent.createChooser(share, "Share to"))
    }

    private fun onShareMore() {
        val videoFile = File(filePath!!)
        val videoURI: Uri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", videoFile)
        ShareCompat.IntentBuilder.from(this)
            .setStream(videoURI)
            .setType(if(fileType == "video") "video/mp4" else "image/*")
            .setChooserTitle("Share to...")
            .startChooser()
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }
}