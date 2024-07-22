package com.image.effect.timewarp.scan.filtertiktok.face.filter

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.applovin.sdk.AppLovinSdk
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.image.effect.timewarp.scan.filtertiktok.face.filter.ad.MaxAds
import com.image.effect.timewarp.scan.filtertiktok.face.filter.iap.IapConfig
import com.image.effect.timewarp.scan.filtertiktok.face.filter.iap.lib.MobileBilling
import com.image.effect.timewarp.scan.filtertiktok.face.filter.util.DateUtils
import com.orhanobut.hawk.Hawk
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump
import java.util.*

class MainApp : Application(), Application.ActivityLifecycleCallbacks, LifecycleObserver {

    val TAG = "MainApp"

    private lateinit var appOpenAdManager: AppOpenAdManager
    private var currentActivity: Activity? = null

    override fun onCreate() {
        super.onCreate()

        Hawk.init(this).build();
        if (!Hawk.contains(Pref.InstallFd)) {
            Hawk.put(Pref.InstallFd, DateUtils.todayFd())
            Hawk.put(Pref.LastActiveFd, DateUtils.todayFd())
        }

        registerActivityLifecycleCallbacks(this)

        AppLovinSdk.getInstance(this).mediationProvider = "max"
        AppLovinSdk.initializeSdk(this) { }

        appOpenAdManager = AppOpenAdManager()

        ProcessLifecycleOwner.get().lifecycle.addObserver(this)

        MobileBilling.init(IapConfig::class.java)

        ViewPump.init(
            ViewPump.builder()
                .addInterceptor(
                    CalligraphyInterceptor(
                        CalligraphyConfig.Builder()
                            .setDefaultFontPath("fonts/Quicksand-Regular.ttf")
                            .setFontAttrId(R.attr.fontPath)
                            .build()
                    )
                )
                .build()
        )

        checkDateUseApp()
    }

    /** LifecycleObserver method that shows the app open ad when the app moves to foreground. */
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onMoveToForeground() {
        // AD
        currentActivity?.let {
            /** only show on my activities, except Splash */
            Log.e(TAG, "onMoveToForeground: activityClassName: ${it.javaClass.name}")
            if (it.javaClass.name.endsWith("SplashActivity")) return@let
            if (!it.javaClass.name.startsWith(packageName)) return@let

            appOpenAdManager.showAdIfAvailable(
                it,
                object : OnShowAdCompleteListener {
                    override fun onShowAdComplete() {
                        // Empty because the user will go back to the activity that shows the ad.
                    }
                })
        }

        // EVENT LOG
        checkDateUseApp()

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
            Log.e(TAG, "token: ${token}")
        })
    }

    /**
     * log event
     * check 3 ngày liên tiếp
     * từ khi cài đặt
     */
    private fun checkDateUseApp() {
        val lastActiveFd = Hawk.get(Pref.LastActiveFd, -1)
        if (lastActiveFd < 0) return

        val installDate = DateUtils.fdToDate(lastActiveFd)
        val todayFd = DateUtils.todayFd()
        val todayDate = DateUtils.fdToDate(todayFd)
        Log.e(TAG, "ContinuousActiveDay [check]: ${Hawk.get(Pref.ContinuousActiveDay, 0)}")

        if (lastActiveFd == todayFd) return
        if (todayDate.time - installDate.time <= 24 * 60 * 60 * 1000) { // <= 1 day
            Hawk.put(Pref.ContinuousActiveDay, Hawk.get(Pref.ContinuousActiveDay, 0) + 1)
            Hawk.put(Pref.LastActiveFd, todayFd)

            if (Hawk.get(Pref.ContinuousActiveDay, 0) == 2) {
                Events.log(Events.Active3dayContinuous)
            }

            Log.e(TAG, "ContinuousActiveDay [after]: lastFd: ${Hawk.put(Pref.LastActiveFd, todayFd)}, count: ${Hawk.get(Pref.ContinuousActiveDay, 0)}")
        }
    }

    /** Interface definition for a callback to be invoked when an app open ad is complete. */
    interface OnShowAdCompleteListener {
        fun onShowAdComplete()
    }

    /** Inner class that loads and shows app open ads. */
    private inner class AppOpenAdManager {
        val LOG_TAG = "MainApp AppOpenAdManager"
        private var appOpenAd: AppOpenAd? = null
        private var isLoadingAd = false
        var isShowingAd = false
        private var loadTime: Long = 0

        /** Request an ad. */
        fun loadAd(context: Context) {
            if (!MaxAds.canShowAd(this@MainApp)) {
                return
            }
            // Do not load ad if there is an unused ad or one is already loading.
            if (isLoadingAd || isAdAvailable()) {
                return
            }

            Log.e(TAG, "loadAd: ", )

            isLoadingAd = true
            val request = AdRequest.Builder().build()
            AppOpenAd.load(
                context,
                Const.AdmobOpenAdId,
                request,
                AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,
                object : AppOpenAd.AppOpenAdLoadCallback() {
                    override fun onAdLoaded(ad: AppOpenAd) {
                        // Called when an app open ad has loaded.
                        Log.d(LOG_TAG, "Ad was loaded.")
                        appOpenAd = ad
                        isLoadingAd = false
                        loadTime = Date().time
                    }

                    override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                        // Called when an app open ad has failed to load.
                        Log.d(LOG_TAG, loadAdError.message)
                        isLoadingAd = false
                    }
                })
        }

        private fun wasLoadTimeLessThanNHoursAgo(numHours: Long): Boolean {
            val dateDifference: Long = Date().time - loadTime
            val numMilliSecondsPerHour: Long = 3600000
            return dateDifference < numMilliSecondsPerHour * numHours
        }


        /** Check if ad exists and can be shown. */
        private fun isAdAvailable(): Boolean {
            return appOpenAd != null && wasLoadTimeLessThanNHoursAgo(4)
        }

        /** Shows the ad if one isn't already showing. */
        fun showAdIfAvailable(activity: Activity, onShowAdCompleteListener: OnShowAdCompleteListener) {
            Log.e(TAG, "showAdIfAvailable: ", )
            if (!MaxAds.canShowAd(this@MainApp)) {
                return
            }

            // If the app open ad is already showing, do not show the ad again.
            if (isShowingAd) {
                Log.d(LOG_TAG, "The app open ad is already showing.")
                return
            }

            // If the app open ad is not available yet, invoke the callback then load the ad.
            if (!isAdAvailable()) {
                Log.d(LOG_TAG, "The app open ad is not ready yet.")
                onShowAdCompleteListener.onShowAdComplete()
                loadAd(activity)
                return
            }

            appOpenAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    // Called when full screen content is dismissed.
                    // Set the reference to null so isAdAvailable() returns false.
                    Log.d(LOG_TAG, "Ad dismissed fullscreen content.")
                    appOpenAd = null
                    isShowingAd = false

                    onShowAdCompleteListener.onShowAdComplete()
                    loadAd(activity)
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    // Called when fullscreen content failed to show.
                    // Set the reference to null so isAdAvailable() returns false.
                    Log.d(LOG_TAG, adError.message)
                    appOpenAd = null
                    isShowingAd = false

                    onShowAdCompleteListener.onShowAdComplete()
                    loadAd(activity)
                }

                override fun onAdShowedFullScreenContent() {
                    // Called when fullscreen content is shown.
                    Log.d(LOG_TAG, "Ad showed fullscreen content.")
                }
            }
            isShowingAd = true
            appOpenAd?.show(activity)
        }

    }

    /**
     * ActivityLifecycleCallbacks
     */

    override fun onActivityCreated(activity: Activity, bundle: Bundle?) {
    }

    override fun onActivityStarted(activity: Activity) {
        // Updating the currentActivity only when an ad is not showing.
        if (!appOpenAdManager.isShowingAd) {
            currentActivity = activity
        }
        if (!activity.javaClass.name.endsWith("SplashActivity")) {
            appOpenAdManager.loadAd(activity)
        }
    }

    override fun onActivityResumed(activity: Activity) {
        MobileBilling.queryPurchases(activity.applicationContext)
    }

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(activity: Activity, bundle: Bundle) {
    }

    override fun onActivityDestroyed(activity: Activity) {
    }

}