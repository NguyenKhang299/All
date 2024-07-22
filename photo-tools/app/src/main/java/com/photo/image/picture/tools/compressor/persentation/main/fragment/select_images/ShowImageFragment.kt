package com.photo.image.picture.tools.compressor.persentation.main.fragment.select_images

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.activityViewModels
import com.photo.image.picture.tools.compressor.R
import com.photo.image.picture.tools.compressor.base.BaseFragment
import com.photo.image.picture.tools.compressor.databinding.FragmentShowImageBinding
import com.photo.image.picture.tools.compressor.utils.XToast
import dagger.hilt.android.AndroidEntryPoint
import java.util.ArrayList

@AndroidEntryPoint
class ShowImageFragment : BaseFragment<FragmentShowImageBinding>() {
    override fun provideViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentShowImageBinding.inflate(inflater, container, false)

    private val mainImageViewModel by activityViewModels<MainImageViewModel>()
    private val imageAdapter by lazy { ImageAdapter(mainImageViewModel, requireActivity()) }

    override fun setupView(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            mRcy.adapter = imageAdapter
        }
    }

    override fun setupObservers() {
        mainImageViewModel.apply {

            imagesLiveData.observe(requireActivity()) {
                if (it.isEmpty()) {
                    binding.mLayoutNoFiles.visibility=if (it.isEmpty()) View.VISIBLE else View.GONE
                }
                imageAdapter.submitData = ArrayList(updateImageDataSelected(it))
            }

            mainImageViewModel.closeSelectedLiveData.observe(this@ShowImageFragment) { it ->
                if (it) {
                    mainImageViewModel.getImagesSelected().forEach { image ->
                        val index = imageAdapter.submitData.indexOfLast { it.id == image.id }
                         index.takeIf { it != -1 }?.let { position ->
                            imageAdapter.submitData[position].isSelected = false
                            imageAdapter.notifyItemChanged(position)
                            binding.mRcy.findViewHolderForAdapterPosition(position)?.itemView?.startAnimation(
                                AnimationUtils.loadAnimation(context, R.anim.scale_up)
                            )
                        }
                    }
                    mainImageViewModel.getImagesSelected().clear()
                    mainImageViewModel.showSelect()
                }
            }
        }
    }
}