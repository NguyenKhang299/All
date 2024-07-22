package com.photo.image.picture.tools.compressor.persentation.main.fragment.select_images

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import androidx.activity.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.photo.image.picture.tools.compressor.R
import com.photo.image.picture.tools.compressor.action.ActionImage
import com.photo.image.picture.tools.compressor.base.BaseActivity
import com.photo.image.picture.tools.compressor.databinding.AcitvityPickImageBinding
import com.photo.image.picture.tools.compressor.persentation.DialogBackFragment
import com.photo.image.picture.tools.compressor.utils.Utils.Companion.startToMain
import com.photo.image.picture.tools.compressor.utils.XAnimationUtils
import com.photo.image.picture.tools.compressor.utils.clickIconNavigation
import com.photo.image.picture.tools.compressor.utils.getActionImage
import com.photo.image.picture.tools.compressor.utils.setColorItemMenu
import com.photo.image.picture.tools.compressor.utils.setMenuToolbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainPickImageActivity : BaseActivity<AcitvityPickImageBinding>() {
    private val navController by lazy { findNavController(R.id.fragmentContainerView) }
    private val mainImageViewModel by viewModels<MainImageViewModel>()

    override fun provideViewBinding() = AcitvityPickImageBinding.inflate(layoutInflater)

    override fun setupView(savedInstanceState: Bundle?) {
        mainImageViewModel.postActionImage(intent.getActionImage()!!)
        setUpSelected()
        showBannerAds(binding.bannerAds)
    }

    fun hideBanner(boolean: Boolean) {
        binding.bannerAds.visibility = if (boolean) View.GONE else View.VISIBLE
    }

    private fun setUpSelected() {
        binding.mToolbar.apply {
            visibility = View.GONE
            setMenuToolbar(R.menu.menu_next) {
                XAnimationUtils.transitionShowSelectedView(this) {
                    handleClickNext()
                }
                true
            }
            setColorItemMenu(R.id.next, Color.WHITE)
            clickIconNavigation {
                mainImageViewModel.postClose(true)
                XAnimationUtils.transitionHideView(this) {
                    visibility = View.GONE
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return true
    }

    private fun handleClickNext() {
        XAnimationUtils.transitionHideView(binding.mToolbar) {
            binding.mToolbar.visibility = View.GONE
            when (mainImageViewModel.getActionImage()) {
                ActionImage.ADVANCE_COMPRESS -> {
                    val actionId =
                        if (navController.currentDestination?.id == R.id.mainPickFragment) {
                            R.id.action_mainPickFragment_to_advanceCompress
                        } else {
                            R.id.action_imageFolderFragment_to_advanceCompress
                        }
                    navController.navigate(actionId)
                }

                ActionImage.COMPRESS_PHOTO -> {
                    val actionId =
                        if (navController.currentDestination?.id == R.id.mainPickFragment) {
                            R.id.action_mainPickFragment_to_selectCompression
                        } else {
                            R.id.action_imageFolderFragment_to_selectCompression
                        }
                    navController.navigate(actionId)
                }

                ActionImage.CROP_COMPRESS -> {
                    val actionId =
                        if (navController.currentDestination?.id == R.id.mainPickFragment) {
                            R.id.action_mainPickFragment_to_cropImageFragment
                        } else {
                            R.id.action_imageFolderFragment_to_cropImageFragment
                        }
                    navController.navigate(actionId)
                }

                ActionImage.CONVERT_PHOTO -> {
                    val actionId =
                        if (navController.currentDestination?.id == R.id.mainPickFragment) {
                            R.id.action_mainPickFragment_to_covertPhotoFragment
                        } else {
                            R.id.action_imageFolderFragment_to_covertPhotoFragment
                        }
                    navController.navigate(actionId)
                }

                ActionImage.RESIZE_COMPRESS -> {
                    val actionId =
                        if (navController.currentDestination?.id == R.id.mainPickFragment) {
                            R.id.action_mainPickFragment_to_resizeFragment
                        } else {
                            R.id.action_imageFolderFragment_to_resizeFragment
                        }
                    navController.navigate(actionId)
                }
            }
        }
    }

    override fun setupObservers() {

        mainImageViewModel.imageSelectedLiveData.observe(this) {
            if (mainImageViewModel.getImagesSelected().size == 1) {
                binding.mToolbar.visibility = View.VISIBLE
                XAnimationUtils.transitionShowSelectedView(binding.mToolbar) { }
            }
        }

        mainImageViewModel.imageUnSelectedLiveData.observe(this) {
            if (mainImageViewModel.getImagesSelected().size == 0) {
                binding.mToolbar.visibility = View.VISIBLE
                XAnimationUtils.transitionHideView(binding.mToolbar) {
                    binding.mToolbar.visibility = View.GONE
                }
            }
            binding.mToolbar.title = "${mainImageViewModel.getImagesSelected().size} Selected"
        }

        mainImageViewModel.imageSelectedLiveData.observe(this) {
            binding.mToolbar.title = "${mainImageViewModel.getImagesSelected().size} Selected"
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        if (navController.currentDestination?.id == R.id.resultFragment) {
            DialogBackFragment().show(supportFragmentManager, DialogBackFragment::class.simpleName)
            return
        }
        startToMain()
    }

}
