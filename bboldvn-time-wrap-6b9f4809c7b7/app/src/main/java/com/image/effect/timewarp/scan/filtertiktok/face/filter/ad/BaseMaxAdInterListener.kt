package com.image.effect.timewarp.scan.filtertiktok.face.filter.ad

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxAdListener
import com.applovin.mediation.MaxError
import com.applovin.mediation.ads.MaxInterstitialAd
import java.util.concurrent.TimeUnit

open class BaseMaxAdInterListener(val interstitialAd: MaxInterstitialAd?) : MaxAdListener {
    val TAG = "BaseMaxAdListener"
    private var retryAttempt = 0.0

    // MAX Ad Listener
    override fun onAdLoaded(maxAd: MaxAd) {
        Log.e(TAG, "onAdLoaded:")
        // Interstitial ad is ready to be shown. interstitialAd.isReady() will now return 'true'

        // Reset retry attempt
        retryAttempt = 0.0
    }

    override fun onAdLoadFailed(p0: String, p1: MaxError) {
         // Interstitial ad failed to load
        // AppLovin recommends that you retry with exponentially higher delays up to a maximum delay (in this case 64 seconds)

        retryAttempt++
        val delayMillis = TimeUnit.SECONDS.toMillis(Math.pow(2.0, Math.min(6.0, retryAttempt)).toLong())

        Handler(Looper.getMainLooper()).postDelayed({
            interstitialAd?.let {
                if (!interstitialAd.isReady) interstitialAd.loadAd()
            }
        }, delayMillis)
    }

    override fun onAdDisplayFailed(p0: MaxAd, p1: MaxError) {
         // Interstitial ad failed to display. AppLovin recommends that you load the next ad.
        interstitialAd?.loadAd()
    }

    override fun onAdDisplayed(maxAd: MaxAd) {
        Log.e(TAG, "onAdDisplayed:")
    }

    override fun onAdClicked(maxAd: MaxAd) {
        Log.e(TAG, "onAdClicked:")
    }



    override fun onAdHidden(maxAd: MaxAd) {
        Log.e(TAG, "onAdHidden:")
        // Interstitial ad is hidden. Pre-load the next ad
//        interstitialAd.loadAd()
    }
}