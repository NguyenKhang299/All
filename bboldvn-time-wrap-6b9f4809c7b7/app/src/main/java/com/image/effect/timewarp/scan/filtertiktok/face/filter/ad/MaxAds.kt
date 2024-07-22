package com.image.effect.timewarp.scan.filtertiktok.face.filter.ad

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.ViewGroup
import android.widget.FrameLayout
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxAdViewAdListener
import com.applovin.mediation.MaxError
import com.applovin.mediation.ads.MaxAdView
import com.image.effect.timewarp.scan.filtertiktok.face.filter.Const
import com.image.effect.timewarp.scan.filtertiktok.face.filter.R
import com.image.effect.timewarp.scan.filtertiktok.face.filter.iap.IapConfig
import com.image.effect.timewarp.scan.filtertiktok.face.filter.iap.lib.MobileBilling

class MaxAds {
    companion object {
        val TAG = "MaxAds"

        fun canShowAd(context: Context): Boolean {
            Log.e(TAG, "canShowAd: ${!MobileBilling.isPurchasedSku(context, IapConfig.AD_FREE)}", )
            return !MobileBilling.isPurchasedSku(context, IapConfig.AD_FREE)
        }

        fun loadBannerAd(activity: Activity, container: ViewGroup, placement: String) {
            val adView = MaxAdView(Const.MaxBannerId, activity)
            adView.placement = placement
            adView.setRevenueListener(BaseAdRevenueListener())
            adView.setListener(object : MaxAdViewAdListener {
                override fun onAdLoaded(p0: MaxAd) {
                    Log.e(TAG, "loadBannerAd. onAdLoaded")
                    val width = ViewGroup.LayoutParams.MATCH_PARENT
                    val heightPx = activity.resources.getDimensionPixelSize(R.dimen.banner_height)
                    adView.layoutParams = FrameLayout.LayoutParams(width, heightPx)
                    adView.setBackgroundResource(R.color.main_background)
                    container.removeAllViews()
                    container.addView(adView)
                }

                override fun onAdLoadFailed(p0: String, p1: MaxError) {
                 }

                override fun onAdDisplayed(p0: MaxAd) {}
                override fun onAdHidden(p0: MaxAd) {}
                override fun onAdClicked(p0: MaxAd) {}
                override fun onAdDisplayFailed(p0: MaxAd, p1: MaxError) {}
                override fun onAdExpanded(p0: MaxAd) {}
                override fun onAdCollapsed(p0: MaxAd) {}
            })

            adView.loadAd()
        }

    }
}