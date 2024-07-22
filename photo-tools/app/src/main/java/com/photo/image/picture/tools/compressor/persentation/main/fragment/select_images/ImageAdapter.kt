package com.photo.image.picture.tools.compressor.persentation.main.fragment.select_images

import android.text.format.Formatter
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.lifecycle.LifecycleOwner
import com.photo.image.picture.tools.compressor.R
import com.photo.image.picture.tools.compressor.action.ActionImage
import com.photo.image.picture.tools.compressor.base.BaseAdapter
import com.photo.image.picture.tools.compressor.databinding.ItemImageBinding
import com.photo.image.picture.tools.compressor.domain.model.Image
import com.photo.image.picture.tools.compressor.utils.Utils

class ImageAdapter(
    private val mainImageViewModel: MainImageViewModel,
    private val lifecycleOwner: LifecycleOwner
) : BaseAdapter<ItemImageBinding, Image>() {
    override fun getViewBinding(
        inflater: LayoutInflater,
        parent: ViewGroup
    ) = ItemImageBinding.inflate(inflater, parent, false)

    override fun onBind(binding: ItemImageBinding, image: Image, position: Int) {
        if (mainImageViewModel.getActionImage() == ActionImage.CROP_COMPRESS && mainImageViewModel.getImagesSelected().size > 0) return
        mainImageViewModel.showCheckedLiveData.observe(lifecycleOwner) {
            binding.isShowSelect = it
        }
        binding.apply {
            isSelected = image.isSelected
            isShowSelect = mainImageViewModel.getImagesSelected().isNotEmpty()
            resolution = image.resolution.toString()
            size = Formatter.formatFileSize(root.context, image.size)

            if (image.isSelected) {
                mConstraint.startAnimation(
                    AnimationUtils.loadAnimation(root.context, R.anim.scale_down)
                        .apply { duration = 0 }
                )
            }
            mConstraint.setOnClickListener {
                image.isSelected = !image.isSelected
                isSelected = image.isSelected
                mainImageViewModel.setSelected(image)
                mainImageViewModel.showSelect()
                val animationRes = if (!image.isSelected) R.anim.scale_up else R.anim.scale_down
                mConstraint.startAnimation(AnimationUtils.loadAnimation(it.context, animationRes))
            }

            Utils.loadImage(mImage, image.path)
        }

    }
}