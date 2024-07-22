package com.photo.image.picture.tools.compressor.base

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log
import com.photo.image.picture.tools.compressor.base.BaseActivity.Companion.SHOW_OPEN_FIRST_MAIN
import com.photo.image.picture.tools.compressor.persentation.main.MainActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.appopen.AppOpenAd.AppOpenAdLoadCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.AdError

import com.google.android.gms.ads.FullScreenContentCallback
import com.photo.image.picture.tools.compressor.BuildConfig
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AppOpenAdManager @Inject constructor(
    @ApplicationContext private val application: Context
) :
    Application.ActivityLifecycleCallbacks {

    private var isShowingAd = false
    private var appOpenAd: AppOpenAd? = null
    private var loadCallback: AppOpenAdLoadCallback = object : AppOpenAdLoadCallback() {
        override fun onAdLoaded(ad: AppOpenAd) {
            appOpenAd = ad
        }

        override fun onAdFailedToLoad(loadAdError: LoadAdError) {
            appOpenAd = null
        }
    }

    private var currentActivity: Activity? = null


    private fun fetchAd() {
        if (BaseActivity.IS_SUB_VIP || !BaseActivity.ENABLE_ADS) return
        // Have unused ad, no need to fetch another.
        if (appOpenAd != null) {
            return
        }
        AppOpenAd.load(
            application, BuildConfig.GG_APP_OPEN,
            provideAdRequest(), loadCallback
        )
    }

    fun showAdIfAvailable() {
        if (BaseActivity.IS_SUB_VIP || !BaseActivity.ENABLE_ADS) return
        if (!isShowingAd && currentActivity != null && appOpenAd != null) {
            appOpenAd!!.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    appOpenAd = null
                    isShowingAd = false
                    fetchAd()
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                 }

                override fun onAdShowedFullScreenContent() {
                    isShowingAd = true
                }
            }
            appOpenAd!!.show(currentActivity!!)
        } else {
            fetchAd()
        }
    }

    private fun provideAdRequest(): AdRequest {
        return AdRequest.Builder().build()
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}

    override fun onActivityStarted(activity: Activity) {
        currentActivity = activity
        if (activity is MainActivity && !BaseActivity.SHOW_OPEN_FIRST_MAIN) {
            showAdIfAvailable()
            BaseActivity.SHOW_OPEN_FIRST_MAIN = true
        }
    }

    override fun onActivityResumed(activity: Activity) {

    }

    override fun onActivityPaused(activity: Activity) {}

    override fun onActivityStopped(activity: Activity) {}

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

    override fun onActivityDestroyed(activity: Activity) {
        currentActivity = null
    }
}