package com.photo.image.picture.tools.compressor.persentation.main

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.NotificationCompat
import androidx.core.view.GravityCompat
import androidx.core.view.get
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.photo.image.picture.tools.compressor.R
import com.photo.image.picture.tools.compressor.action.ActionImage
import com.photo.image.picture.tools.compressor.base.AppOpenAdManager
import com.photo.image.picture.tools.compressor.base.BaseActivity
import com.photo.image.picture.tools.compressor.databinding.ActivityMainBinding
import com.photo.image.picture.tools.compressor.di.FileDirectory
import com.photo.image.picture.tools.compressor.persentation.DialogBackFragment
import com.photo.image.picture.tools.compressor.persentation.compressed.CompressedActivity
import com.photo.image.picture.tools.compressor.persentation.main.fragment.select_images.MainPickImageActivity
import com.photo.image.picture.tools.compressor.utils.Const
import com.photo.image.picture.tools.compressor.utils.PermUtils
import com.photo.image.picture.tools.compressor.utils.Utils
import com.photo.image.picture.tools.compressor.utils.passActionImage
import com.tbruyelle.rxpermissions3.RxPermissions
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.lang.reflect.Array
import java.util.Arrays
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(), MainViewModel.MainFragmentEvent,
    OnCLickItemListener {
    override fun provideViewBinding() = ActivityMainBinding.inflate(layoutInflater)
    private var navigationDrawerBody: NavigationDrawerBody? = null
    private val mainViewModel: MainViewModel by viewModels()

    @Inject
    lateinit var appOpenManager: AppOpenAdManager

    @Inject
    @FileDirectory
    lateinit var rootFilePath: File

    @SuppressLint("RtlHardcoded", "SetTextI18n")
    override fun setupView(savedInstanceState: Bundle?) {
         navigationDrawerBody =
            supportFragmentManager.findFragmentById(R.id.navBody) as NavigationDrawerBody
        navigationDrawerBody?.setOnClickItemListener(this)
        binding.event = this
        binding.toolbar.setNavigationOnClickListener {
            mainViewModel.openDrawer()
        }
        showBannerAds(binding.bannerAds)
        binding.header.version.text = "Version: " + Utils.getAppVersion(this)

        if (SHOW_OPEN_FIRST_MAIN) {
            showInterstitial(true) {}
        }
    }

    override fun onResume() {
        super.onResume()
        onClickItem(R.id.home)

    }

    @SuppressLint("CheckResult")
    override fun setupObservers() {
        mainViewModel.apply {
            val owner = this@MainActivity
            openDrawerLiveData.observe(owner) {
                if (it) {
                    binding.drawerLayout.openDrawer(GravityCompat.START, true)
                } else {
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                }
            }
        }
    }

    override fun onClickItem(id: Int) {
        binding.drawerLayout.close()
        when (id) {
            R.id.home -> {}
            R.id.compressPhoto -> {
                startActivity(Intent(this, CompressedActivity::class.java))
            }

            R.id.changeLanguage -> {}
            R.id.support -> {
                val email = "khangnguyen.teras@gmail.com"
                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:$email")
                }
                startActivity(intent)
            }

            R.id.rateUp -> {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(Const.linkApp))
                startActivity(intent)
            }

            R.id.shareApp -> {
                val textToShare = "Please download and experience our app ${Const.linkApp}"

                val shareIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, textToShare)
                    type = "text/plain"
                }
                startActivity(Intent.createChooser(shareIntent, "Share:"))
            }

            R.id.policy -> {}
            R.id.termsConditions -> {}
        }
        navigationDrawerBody?.setOnChecked(id)
    }

    private lateinit var actionImage: ActionImage
    private val reqPerm =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            if (it.isNotEmpty() && it.values.all { it }) {
                startPicker(actionImage)
            } else {
                showMess(getString(R.string.perm_denied))
            }
        }

    @SuppressLint("CheckResult")
    override fun clickActionCompress(actionImage: ActionImage) {
        this.actionImage = actionImage
        if (!PermUtils.checkImageAccessPermissions(this)) {
            val permissions = PermUtils.getImageAccessPermissions()
            reqPerm.launch(permissions)
        } else {
            startPicker(actionImage)
        }
    }

    private fun startPicker(actionImage: ActionImage) {
        startActivity(Intent(this, MainPickImageActivity::class.java).apply {
            passActionImage(actionImage)
        })
    }
}