package com.image.effect.timewarp.scan.filtertiktok.face.filter.ad

import android.util.Log
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxAdRevenueListener
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase

class BaseAdRevenueListener : MaxAdRevenueListener {

    override fun onAdRevenuePaid(p0: MaxAd) {
        Firebase.analytics.logEvent(FirebaseAnalytics.Event.AD_IMPRESSION) {
            param(FirebaseAnalytics.Param.AD_PLATFORM, "appLovin")
            param(FirebaseAnalytics.Param.AD_UNIT_NAME, p0.adUnitId)
            param(FirebaseAnalytics.Param.AD_FORMAT, p0.format.displayName)
            param(FirebaseAnalytics.Param.AD_SOURCE, p0.networkName)
            param(FirebaseAnalytics.Param.VALUE, p0.revenue)
            param(FirebaseAnalytics.Param.CURRENCY, "USD")
        }
    }
}