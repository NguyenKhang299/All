package com.image.effect.timewarp.scan.filtertiktok.face.filter.activity

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.android.billingclient.api.Purchase
import com.codemybrainsout.ratingdialog.RatingDialog
import com.google.firebase.ktx.Firebase
import com.image.effect.timewarp.scan.filtertiktok.face.filter.*
import com.image.effect.timewarp.scan.filtertiktok.face.filter.ad.MaxAds
import com.image.effect.timewarp.scan.filtertiktok.face.filter.databinding.ActivitySettingBinding
import com.image.effect.timewarp.scan.filtertiktok.face.filter.databinding.DialogLanguageBinding
import com.image.effect.timewarp.scan.filtertiktok.face.filter.databinding.ItemLanguageBinding
import com.image.effect.timewarp.scan.filtertiktok.face.filter.iap.IapConfig
import com.image.effect.timewarp.scan.filtertiktok.face.filter.iap.lib.MobileBilling
import com.image.effect.timewarp.scan.filtertiktok.face.filter.iap.lib.OnPurchaseListener
import com.image.effect.timewarp.scan.filtertiktok.face.filter.util.UIUtils
import com.orhanobut.hawk.Hawk
import io.github.inflationx.viewpump.ViewPumpContextWrapper

class SettingActivity : BaseAppCompatActivity() {

    private lateinit var binding: ActivitySettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
        loadAd()
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }

    private fun initViews() {
        binding.toolbar.txtTitle.text = getString(R.string.settings)
        binding.toolbar.imvShare.visibility = View.GONE
        binding.toolbar.imvDelete.visibility = View.GONE
        binding.toolbar.imvBack.setOnClickListener { onBackPressed() }

        binding.settRemoveAds.textview.text = getString(R.string.remove_ads)
        binding.settRemoveAds.imvIcon.setImageResource(R.drawable.ic_sett_remove_ads)
        binding.settLanguage.textview.text = getString(R.string.language)
        binding.settLanguage.imvIcon.setImageResource(R.drawable.ic_sett_language)
        binding.settShareApp.textview.text = getString(R.string.share_app)
        binding.settShareApp.imvIcon.setImageResource(R.drawable.ic_sett_share)
        binding.settRateApp.textview.text = getString(R.string.rate_app)
        binding.settRateApp.imvIcon.setImageResource(R.drawable.ic_sett_heart)
        binding.settPolicy.textview.text = getString(R.string.privacy_policy)
        binding.settPolicy.imvIcon.setImageResource(R.drawable.ic_sett_policy)
        binding.settMoreApp.textview.text = getString(R.string.more_apps)
        binding.settMoreApp.imvIcon.setImageResource(R.drawable.ic_sett_more_app)
        binding.settAbout.textview.text = getString(R.string.about)
        binding.settAbout.imvIcon.setImageResource(R.drawable.ic_sett_about)

        updateItemRemoveAds()

        binding.settRemoveAds.container.setOnClickListener {
            settingRemoveAdClicked()
        }

        binding.settLanguage.container.setOnClickListener {
            settingLanguageClicked()
        }

        binding.settShareApp.container.setOnClickListener {
            Events.log(Events.ShareApp)
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "Try TimeWarp Filter Now!\nhttps://play.google.com/store/apps/details?id=${packageName}")
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }

        binding.settRateApp.container.setOnClickListener {
            UIUtils.showRatingDialog(this)
        }

        binding.settPolicy.container.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(Const.PolicyLink)))
        }

        binding.settMoreApp.container.setOnClickListener {
            try {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName")))
            } catch (e: ActivityNotFoundException) {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$packageName")))
            }
            /*try {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://dev?id=${Const.DevId}")))
            } catch (e: ActivityNotFoundException) {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/dev?id=${Const.DevId}")))
            }*/
        }

        binding.settAbout.container.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle(R.string.about)
                .setMessage("TimeWarp v${BuildConfig.VERSION_NAME}" + "\n© 2022 All Right Reserved")
                .setPositiveButton("OK") { _, _ -> }
                .show()
        }
    }

    private fun loadAd() {
        MaxAds.loadBannerAd(this, binding.bannerAdContainer, "banner_setting")
    }

    private fun settingLanguageClicked() {
        var dialog: AlertDialog? = null
        val customView = DialogLanguageBinding.inflate(layoutInflater)
        Const.getSupportLocales().forEach { locale ->
            val item = ItemLanguageBinding.inflate(layoutInflater, null, false)
            item.textview.text = locale.displayLanguage
            item.textview.setOnClickListener {
                Hawk.put(Pref.SelectedLanguage, locale.language)

                startActivity(Intent(this@SettingActivity, SplashActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                })
                dialog?.dismiss()
                finish()
            }
            customView.container.addView(item.root)
        }

        dialog = AlertDialog.Builder(this, R.style.Theme_TimewarpDialog)
            .setView(customView.root)
            .setPositiveButton("OK") { _, _ -> }
            .setNegativeButton("Cancel") { _, _ -> }
            .show()
    }

    private fun settingRemoveAdClicked() {
        MobileBilling.purchaseSkuInApp(this, IapConfig.AD_FREE, object : OnPurchaseListener {
            override fun onPurchaseSucceeded(purchase: Purchase?) {
                Toast.makeText(this@SettingActivity, "Purchase Succeeded", Toast.LENGTH_SHORT).show()
                updateItemRemoveAds()
            }

            override fun onPurchaseFailed(error: String?, code: Int) {
                Toast.makeText(this@SettingActivity, "Purchase failed. Please try again", Toast.LENGTH_SHORT).show()
                Log.e("RemoveAdClicked", "onPurchaseFailed: ${error}")
            }
        })
    }

    private fun updateItemRemoveAds() {
        binding.settRemoveAds.container.visibility = if (MobileBilling.isPurchasedSku(this, IapConfig.AD_FREE)) View.GONE else View.VISIBLE
    }

}