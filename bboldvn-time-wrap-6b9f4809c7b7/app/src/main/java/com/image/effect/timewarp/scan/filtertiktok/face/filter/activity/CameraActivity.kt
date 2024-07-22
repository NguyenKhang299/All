package com.image.effect.timewarp.scan.filtertiktok.face.filter.activity

import android.Manifest.permission.*
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Camera
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxError
import com.applovin.mediation.ads.MaxInterstitialAd
import com.daasuu.gpuv.camerarecorder.LensFacing
import com.daasuu.gpuv.egl.filter.GlFilterGroup
import com.image.effect.timewarp.scan.filtertiktok.face.filter.Const
import com.image.effect.timewarp.scan.filtertiktok.face.filter.Events
import com.image.effect.timewarp.scan.filtertiktok.face.filter.Pref
import com.image.effect.timewarp.scan.filtertiktok.face.filter.R
import com.image.effect.timewarp.scan.filtertiktok.face.filter.ad.BaseAdRevenueListener
import com.image.effect.timewarp.scan.filtertiktok.face.filter.ad.BaseMaxAdInterListener
import com.image.effect.timewarp.scan.filtertiktok.face.filter.ad.MaxAds
import com.image.effect.timewarp.scan.filtertiktok.face.filter.databinding.ActivityCameraBinding
import com.image.effect.timewarp.scan.filtertiktok.face.filter.filter.GlLineFilter
import com.image.effect.timewarp.scan.filtertiktok.face.filter.filter.GlTimeWarpFilter
import com.image.effect.timewarp.scan.filtertiktok.face.filter.util.PermUtils
import com.orhanobut.hawk.Hawk
import com.tbruyelle.rxpermissions3.RxPermissions
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import java.util.concurrent.Executors

open class CameraActivity : BaseCameraActivity() {

    private val TAG = "CameraActivity"

    private lateinit var binding: ActivityCameraBinding

    private var isRecording = false
    private var isVideoMode = true
    private var isVertical = true

    val lineFilter = GlLineFilter()
    val timeWarpFilter = GlTimeWarpFilter()

    val SCAN_MILLIS = 10 * 1000L
    val SCAN_STEP = 200

    var interAd: MaxInterstitialAd? = null

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
        loadAd()

        RxPermissions(this)
            .requestEachCombined(CAMERA, RECORD_AUDIO, WRITE_EXTERNAL_STORAGE)
            .subscribe { permission ->
                if (permission.granted) {
                    setUpCamera()
                } else if (permission.shouldShowRequestPermissionRationale) {
                    Toast.makeText(this, "Permissions denied", Toast.LENGTH_LONG).show()
                    finish()
                } else {
                    PermUtils.showDialogPermissionDenied(this) { finish() }
                }
            }

    }

    override fun onResume() {
        super.onResume()

        if (PermUtils.hasPermissions(this, CAMERA, RECORD_AUDIO, WRITE_EXTERNAL_STORAGE)) {
            setUpCamera()
        }
    }

    override fun onStop() {
        super.onStop()
        releaseCamera()
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }

    private fun initViews() {
        cameraWidth = 1024
        cameraHeight = 576
        videoWidth = 576
        videoHeight = 1024

        binding.btnRecord.setOnClickListener {
            onButtonRecordClicked()
        }

        binding.btnSwitchCamera.setOnClickListener {
            onButtonSwitchCameraClicked()
        }

        binding.btnClose.setOnClickListener {
            onBackPressed()
        }

        binding.btnSwitchMode.setOnClickListener {
            onButtonSwitchModeClicked()
        }

        binding.btnVertical.setOnClickListener {
            if (isRecording) return@setOnClickListener
            isVertical = true
            updateDirectionViews()
        }

        binding.btnHorizontal.setOnClickListener {
            if (isRecording) return@setOnClickListener
            isVertical = false
            updateDirectionViews()
        }
    }

    private fun loadAd() {
        MaxAds.loadBannerAd(this, binding.bannerAdContainer, "banner_camera")

        if (MaxAds.canShowAd(this)) {
            interAd = MaxInterstitialAd(Const.MaxFullId, this)
            interAd?.setRevenueListener(BaseAdRevenueListener())
            interAd?.setListener(object : BaseMaxAdInterListener(interAd) {
                override fun onAdLoadFailed(p0: String, p1: MaxError) {
                 }

                override fun onAdDisplayFailed(p0: MaxAd, p1: MaxError) {
                    super.onAdDisplayFailed(p0, p1)
                    openPreview()
                    interAd?.loadAd()
                }



                override fun onAdHidden(maxAd: com.applovin.mediation.MaxAd) {
                    super.onAdHidden(maxAd)
                    openPreview()
                    interAd?.loadAd()
                }
            })
            interAd?.loadAd()
        }
    }

    private fun showAdIfReady() {
        if (MaxAds.canShowAd(this) && interAd?.isReady == true) {
            interAd?.showAd("recording_finished")
        } else {
            openPreview()
        }
    }

    override fun setUpCamera() {
        super.setUpCamera()
        lineFilter.setProgress(0f)
        timeWarpFilter.setProgress(0f)
    }

    private fun onButtonSwitchCameraClicked() {
        if (isRecording) return
        releaseCamera()
        lensFacing = if (lensFacing == LensFacing.BACK) {
            LensFacing.FRONT
        } else {
            LensFacing.BACK
        }
        toggleClick = true
    }

    private fun onButtonSwitchModeClicked() {
        if (isRecording) return
        isVideoMode = !isVideoMode
        updateButtonsIcon()
        Toast.makeText(this, if (isVideoMode) "Video mode" else "Photo mode", Toast.LENGTH_SHORT).show()
    }

    private fun updateButtonsIcon() {
        binding.btnRecord.setImageResource(
            if (isRecording) R.drawable.ic_stop_shadow
            else (if (isVideoMode) R.drawable.ic_record_shadow else R.drawable.ic_photo_shadow)
        )
        binding.btnSwitchMode.setImageResource(if (!isVideoMode) R.drawable.ic_record_shadow else R.drawable.ic_photo_shadow)

        if (!isRecording) binding.progressBar.progress = 0f

        hideViewWhileRecording(binding.btnSwitchCamera)
        hideViewWhileRecording(binding.btnSwitchMode)
        hideViewWhileRecording(binding.btnClose)
        updateDirectionViews()
    }

    private fun updateDirectionViews() {
        hideViewWhileRecording(binding.layoutDirections)
        binding.btnVertical.setBackgroundResource(if (isVertical) R.drawable.bg_direction else android.R.color.transparent)
        binding.btnHorizontal.setBackgroundResource(if (!isVertical) R.drawable.bg_direction else android.R.color.transparent)
    }

    private fun hideViewWhileRecording(view: View) {
        view.visibility = if (isRecording) View.GONE else View.VISIBLE
    }

    private fun onButtonRecordClicked() {
        if (isRecording) {
            stopRecording()
            return
        }

        if (isVideoMode) {
            // Video mod
            try {
                isRecording = true
                filepath = getVideoFilePath()
                applyFilters()
                gpuCameraRecorder.start(filepath)
            } catch (e: Exception) {
                val mIntent = intent
                finish()
                startActivity(mIntent)
            }
        } else {
            // Photo mode
            isRecording = true
            applyFilters()
            startTimer()
        }

        updateButtonsIcon()
    }

    private fun stopRecording() {
        isRecording = false
        if (isVideoMode) {
            gpuCameraRecorder?.stop()
        } else {
            if (sampleGLView == null) {
                Toast.makeText(this, "Error. Please try again", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, CameraActivity::class.java))
                finish()
            }
            captureBitmap { bitmap ->
                run {
                    Handler(Looper.getMainLooper()).post {
                        filepath = getImageFilePath()
                        saveAsPngImage(bitmap, filepath)
                        exportPngToGallery(getApplicationContext(), filepath)
                        showAdIfReady()
                        logFilterUseCount()
                    }
                }
            }
        }
        updateButtonsIcon()
        binding.progressBar.progress = 0f
        lineFilter.setProgress(0f)
        timeWarpFilter.setProgress(0f)
    }

    override fun onBackPressed() {
        if (isRecording) {
            return
        }
        super.onBackPressed()
    }

    //

    override fun onGPUCameraRecordStarted() {
        startTimer()
    }

    override fun onGPUCameraRecordCompleted() {
        showAdIfReady()
        logFilterUseCount()
    }

    private fun logFilterUseCount() {
        // count filter use time & log to firebase
        Hawk.put(Pref.FilterUseCount, Hawk.get(Pref.FilterUseCount, 0) + 1)
        if (Hawk.get(Pref.FilterUseCount, 0) == 10) {
            Events.log(Events.UseFilter10times)
        }
    }

    private fun openPreview() {
        val intent = Intent(this, PreviewActivity::class.java).apply {
            putExtra("source_type", if (isVideoMode) "video" else "photo")
            putExtra("source_file", filepath)
        }
        startActivity(intent)
    }

    private fun applyFilters() {
        if (gpuCameraRecorder == null) return

        val scanType = if (isVertical) 1 else 0
        lineFilter.setTypeScan(scanType)
        timeWarpFilter.setTypeScan(scanType)

        val group = GlFilterGroup(lineFilter, timeWarpFilter)
        gpuCameraRecorder.setFilter(group)
    }

    private fun startTimer() {
        Executors.newSingleThreadExecutor().execute {
            val step = 1f / SCAN_STEP.toFloat()
            var progress = 0.0f

            while (progress < 1f && isRecording) {
                progress += step

                if (isVertical) {
                    lineFilter.setProgress(1 - progress - 0.02f)
                    timeWarpFilter.setProgress(1 - progress)
                } else {
                    lineFilter.setProgress(progress + 0.02f)
                    timeWarpFilter.setProgress(progress)
                }
                Handler(mainLooper).post {
                    binding.progressBar.progress = progress * 100
                }
                Thread.sleep(SCAN_MILLIS / SCAN_STEP)
            }

            // finish scan
            if (progress >= 1f && isRecording) {
                Handler(mainLooper).post {
                    stopRecording()
                }
            }
        }
    }

}