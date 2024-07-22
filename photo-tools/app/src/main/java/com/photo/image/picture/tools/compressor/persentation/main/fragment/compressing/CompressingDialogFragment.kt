package com.photo.image.picture.tools.compressor.persentation.main.fragment.compressing

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels

 import com.photo.image.picture.tools.compressor.databinding.FragmentCompressingBinding

import com.photo.image.picture.tools.compressor.base.BaseDialogFragment
import com.photo.image.picture.tools.compressor.persentation.main.fragment.select_images.MainImageViewModel
import com.photo.image.picture.tools.compressor.persentation.view.OnProgressChangedListener
import com.photo.image.picture.tools.compressor.utils.startToResults


class CompressingDialogFragment() : BaseDialogFragment<FragmentCompressingBinding>(),
    OnProgressChangedListener {
    override fun provideViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentCompressingBinding.inflate(inflater, container, false)

    private val activityViewModel by activityViewModels<MainImageViewModel>()
    private val compressingViewModel by viewModels<CompressingViewModel>()

    override fun setupView(view: View, savedInstanceState: Bundle?) {
        binding.progressCircle.totalProgress = 100F
        binding.progressCircle.format("%.0f%%")
        binding.progressCircle.setOnProgressChanged(this)
        compressingViewModel.setCountTotal(activityViewModel.getImagesSelected().size)
        activityViewModel.compressImage()
    }

    override fun setupObservers() {
        activityViewModel.saveLiveData.observe(this) {
            compressingViewModel.updateProgress()
        }

        compressingViewModel.progressLiveData.observe(this) {
            binding.progressCircle.currentProgress = it
        }
    }

    override fun onProgress(progress: Float) {
        if (progress == 100f) {
            binding.progress = 100
            binding.lottieAnimationView.playAnimation()
            binding.lottieAnimationView.addAnimatorListener(object :AnimatorListener{
                override fun onAnimationStart(animation: Animator) {

                }

                override fun onAnimationEnd(animation: Animator) {
                    startToResults()
                }

                override fun onAnimationCancel(animation: Animator) {

                }

                override fun onAnimationRepeat(animation: Animator) {

                }

            })

        }
    }
}