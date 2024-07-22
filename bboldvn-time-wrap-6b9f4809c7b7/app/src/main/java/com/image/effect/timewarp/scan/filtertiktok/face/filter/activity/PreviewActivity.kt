package com.image.effect.timewarp.scan.filtertiktok.face.filter.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxError
import com.applovin.mediation.ads.MaxInterstitialAd
import com.bumptech.glide.Glide
import com.image.effect.timewarp.scan.filtertiktok.face.filter.Const
import com.image.effect.timewarp.scan.filtertiktok.face.filter.R
import com.image.effect.timewarp.scan.filtertiktok.face.filter.ad.BaseAdRevenueListener
import com.image.effect.timewarp.scan.filtertiktok.face.filter.ad.BaseMaxAdInterListener
import com.image.effect.timewarp.scan.filtertiktok.face.filter.ad.MaxAds
import com.image.effect.timewarp.scan.filtertiktok.face.filter.databinding.ActivityPreviewBinding
import com.potyvideo.library.globalEnums.EnumRepeatMode
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import java.io.File

class PreviewActivity : BaseAppCompatActivity() {
    val TAG = "PreviewActivity"

    private lateinit var binding: ActivityPreviewBinding
    private var filePath: String? = null
    private var fileType: String? = null

    var interAd: MaxInterstitialAd? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityPreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
        loadAd()
    }

    private fun initViews() {
        binding.toolbar.txtTitle.text = getString(R.string.preview)
        binding.toolbar.imvBack.setOnClickListener {
            onBackPressed()
        }
        binding.toolbar.imvShare.visibility = View.GONE
        binding.toolbar.imvDelete.visibility = View.GONE

        binding.btnDiscard.setOnClickListener {
            onButtonDiscardClicked()
        }
        binding.btnSave.setOnClickListener {
            onButtonSaveClicked()
        }

        // play video
        val exo = binding.andExoPlayerView
        exo.setShowControllers(false)
        exo.setShowControllers()
        exo.setRepeatMode(EnumRepeatMode.REPEAT_ALWAYS)
        exo.setPlayWhenReady(true)

        filePath = intent.getStringExtra("source_file")
        fileType = intent.getStringExtra("source_type")
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
        MaxAds.loadBannerAd(this, binding.bannerAdContainer, "banner_preview")

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
                    super.onAdDisplayFailed(p0, p1)
                    openShareActivity()
                }


                override fun onAdHidden(maxAd: com.applovin.mediation.MaxAd) {
                    super.onAdHidden(maxAd)
                    openShareActivity()
                }
            })
            interAd?.loadAd()
        }
    }

    private fun showAdIfReady() {
        if (MaxAds.canShowAd(this) && interAd?.isReady == true) {
            interAd?.showAd("preview_saved")
        } else {
            openShareActivity()
        }
    }

    private fun openShareActivity() {
        val intent = Intent(this, ShareActivity::class.java).apply {
            putExtra("source_type", fileType)
            putExtra("source_file", filePath)
            putExtra("opened_from", "camera")
        }
        startActivity(intent)
        finish()
    }

    private fun onButtonSaveClicked() {
        showAdIfReady()
    }

    private fun onButtonDiscardClicked() {
        AlertDialog.Builder(this)
            .setTitle(R.string.confirm)
            .setMessage(R.string.discard_confirm_msg)
            .setPositiveButton(R.string.discard) { dialog, _ ->
                filePath?.let { File(it).delete() }
                dialog.dismiss()
                finish()
            }
            .setNegativeButton(R.string.cancel) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        onButtonDiscardClicked()
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }
}