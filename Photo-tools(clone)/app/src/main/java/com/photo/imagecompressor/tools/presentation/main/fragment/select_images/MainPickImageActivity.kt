package com.photo.imagecompressor.tools.presentation.main.fragment.select_images

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.navigation.findNavController
import com.photo.imagecompressor.tools.presentation.DialogBackFragment
import com.photo.imagecompressor.tools.utils.Utils.Companion.startToMain
import com.photo.imagecompressor.tools.utils.XAnimationUtils
import com.photo.imagecompressor.tools.utils.clickIconNavigation
import com.photo.imagecompressor.tools.utils.getActionImage
import com.photo.imagecompressor.tools.utils.setMenuToolbar
import com.photo.imagecompressor.tools.R
import com.photo.imagecompressor.tools.action.ActionImage
import com.photo.imagecompressor.tools.base.BaseActivity
import com.photo.imagecompressor.tools.databinding.AcitvityPickImageBinding
import com.photo.imagecompressor.tools.presentation.main.BackDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainPickImageActivity : BaseActivity<AcitvityPickImageBinding>() {
    private val navController by lazy { findNavController(R.id.fragmentContainerView) }
    private val mainImageViewModel by viewModels<MainImageViewModel>()

    override fun provideViewBinding() = AcitvityPickImageBinding.inflate(layoutInflater)

    override fun setupView(savedInstanceState: Bundle?) {
        mainImageViewModel.postActionImage(intent.getActionImage()!!)
        setUpSelected()
    }


    private fun setUpSelected() {
        binding.mToolbar.apply {
            visibility = View.GONE
            setMenuToolbar(R.menu.menu_next) {
                XAnimationUtils.transitionHideView(this) {
                    handleClickNext()
                }
                true
            }
            //  setColorItemMenu(R.id.next, Color.WHITE)
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
            binding.mToolbar.title =
                "${mainImageViewModel.getImagesSelected().size} ${getString(R.string.selected)}"
        }

        mainImageViewModel.imageSelectedLiveData.observe(this) {
            binding.mToolbar.title =
                "${mainImageViewModel.getImagesSelected().size} ${getString(R.string.selected)}"
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        if (navController.currentDestination?.id == R.id.resultFragment) {
            DialogBackFragment().show(supportFragmentManager, DialogBackFragment::class.simpleName)
            return
        }

        if (navController.currentDestination?.id == R.id.imageFolderFragment) {
            navController.popBackStack()
            return
        }
        BackDialogFragment.show(this)

    }
}
