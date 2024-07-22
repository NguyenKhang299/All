package com.photo.image.picture.tools.compressor.base

import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.access.pro.activity.BaseActivity
import com.access.pro.adcontrol.AdsBannerView
import com.access.pro.callBack.OnShowInterstitialListener
import com.access.pro.callBack.OnShowNativeListener
import com.access.pro.config.ConfigModel
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryPurchasesParams
import com.android.billingclient.api.queryPurchasesAsync
import com.photo.image.picture.tools.compressor.utils.XToast
import com.google.android.gms.ads.nativead.NativeAd
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.checkerframework.checker.units.qual.A
import javax.inject.Inject
abstract class BaseActivity<VB : ViewBinding> : BaseActivity() {
    companion object {
        val DELAY_NEXT_SHOW_AD get() = if (ConfigModel.timeInter == 0) 60000 else ConfigModel.timeInter
        var ENABLE_ADS = true
        var LAST_TIME_SHOW_INTERSTITIAL = -1L
        var IS_SUB_VIP = false
        var SHOW_OPEN_FIRST_MAIN = false
    }

    private var _binding: VB? = null
    protected val binding get() = _binding!!

    var billingClient: BillingClient? = null


    fun setupBilling(onNextActivity: () -> Unit,listener: PurchasesUpdatedListener){
        billingClient = BillingClient.newBuilder(this)
            .setListener(listener)
            .enablePendingPurchases()
            .build()

        billingClient?.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    lifecycleScope.launch {
                        getActivePurchase()
                        onNextActivity()
                    }
                }
            }

            override fun onBillingServiceDisconnected() {
                onNextActivity()
            }
        })
    }


    suspend fun getActivePurchase(): Boolean {
        val subResult = billingClient?.queryPurchasesAsync(
            QueryPurchasesParams.newBuilder()
                .setProductType(BillingClient.ProductType.SUBS).build()
        )
        val inappResult = billingClient?.queryPurchasesAsync(
            QueryPurchasesParams.newBuilder()
                .setProductType(BillingClient.ProductType.INAPP).build()
        )
        proApplication.isSubVip =
            (subResult != null && subResult.purchasesList.isNotEmpty())
                    || (inappResult != null && inappResult.purchasesList.isNotEmpty())
        IS_SUB_VIP = proApplication.isSubVip
        return proApplication.isSubVip
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = provideViewBinding().apply {
            setContentView(root)
        }

        setupObservers()

        setupView(savedInstanceState)
    }


    protected open fun setupObservers() {

    }

    open fun showMess(msg: String) {
        XToast.show(this, msg)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    protected abstract fun provideViewBinding(): VB

    protected abstract fun setupView(savedInstanceState: Bundle?)

    open fun showBannerAds(viewContainer: ViewGroup) {
        Log.d("dsfsfsfsfsd", ENABLE_ADS.toString())

        if (!proApplication.isSubVip && ENABLE_ADS) {
            val banner = AdsBannerView.getView(windowManager, this, viewContainer)
            AdsBannerView.loadAds(AdsBannerView.BANNER_BOTTOM, banner)
        }
    }

    open fun showInterstitial(now: Boolean, call: (Boolean) -> Unit) {
        if (!proApplication.isSubVip && System.currentTimeMillis() - LAST_TIME_SHOW_INTERSTITIAL >= DELAY_NEXT_SHOW_AD && ENABLE_ADS) {
            showAds(true, object : OnShowInterstitialListener {
                override fun onCloseAds(hasAds: Boolean) {
                    if (hasAds) LAST_TIME_SHOW_INTERSTITIAL = System.currentTimeMillis()
                    call(hasAds)
                }
            })
        }
    }

    open fun showNativeAds(viewContainer: ViewGroup, call: (() -> Unit)? = null) {
        if (!proApplication.isSubVip && ENABLE_ADS) {
            nativeRender.prepareNative()
            nativeRender.loadNativeAds(object : OnShowNativeListener {
                override fun onLoadDone(hasAds: Boolean, currentNativeAd: NativeAd?) {
                    // load dc native
                    if (call != null) {
                        call()
                    }
                }

            }, viewContainer)
        }
    }
}