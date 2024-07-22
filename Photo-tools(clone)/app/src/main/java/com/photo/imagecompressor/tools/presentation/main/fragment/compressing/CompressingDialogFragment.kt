package com.photo.imagecompressor.tools.presentation.main.fragment.compressing

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels


import com.photo.imagecompressor.tools.base.BaseDialogFragment
import com.photo.imagecompressor.tools.databinding.FragmentCompressing1Binding
import com.photo.imagecompressor.tools.persentation.view.OnProgressChangedListener
import com.photo.imagecompressor.tools.utils.startToResults
import com.photo.imagecompressor.tools.databinding.FragmentCompressingBinding
import com.photo.imagecompressor.tools.presentation.main.fragment.select_images.MainImageViewModel


class CompressingDialogFragment() : BaseDialogFragment<FragmentCompressingBinding>(),
    OnProgressChangedListener {
    override fun provideViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentCompressingBinding.inflate(inflater, container, false)

    private val activityViewModel by activityViewModels<MainImageViewModel>()
    private val compressingViewModel by viewModels<CompressingViewModel>()
    override fun setupView(view: View, savedInstanceState: Bundle?) {
        try {
            binding.progressCircle.totalProgress = 100F
            binding.progressCircle.format("%.0f%%")
            binding.progressCircle.setOnProgressChanged(this)
            compressingViewModel.setCountTotal(activityViewModel.getImagesSelected().size)
            activityViewModel.compressImage(requireContext())
        } catch (e: Exception) {

        }
    }

    override fun setupObservers() {
        activityViewModel.saveLiveData.observe(this) {
            compressingViewModel.updateProgress()
        }

        compressingViewModel.progressLiveData.observe(this) {
            binding.progressCircle.currentProgress = it
        }
        compressingViewModel.progressLiveData.observe(this) {
            if (it == 100f) {
                binding.progress = 100
                binding.lottieAnimationView.playAnimation()
                binding.lottieAnimationView.addAnimatorListener(object : AnimatorListener {
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

    override fun onProgress(progress: Float) {
        if (progress == 100f) {


        }
    }
}

class CompressingDialogLoadingFragment() : BaseDialogFragment<FragmentCompressing1Binding>(),
    OnProgressChangedListener {
    override fun provideViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentCompressing1Binding.inflate(inflater, container, false)

    override fun setupView(view: View, savedInstanceState: Bundle?) {

    }

    override fun setupObservers() {
        binding.lottieAnimationView.playAnimation()
    }

    override fun onProgress(progress: Float) {

    }
}