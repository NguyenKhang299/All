package com.photo.imagecompressor.tools.presentation.main.fragment.select_images

import android.text.format.Formatter
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import com.photo.imagecompressor.tools.base.BaseAdapter
import com.photo.imagecompressor.tools.R
import com.photo.imagecompressor.tools.action.ActionImage
import com.photo.imagecompressor.tools.databinding.ItemImageBinding
import com.photo.imagecompressor.tools.domain.model.Image_
import com.photo.imagecompressor.tools.utils.Utils

class ImageAdapter(
    private val mainImageViewModel: MainImageViewModel,
    private val lifecycleOwner: LifecycleOwner
) : BaseAdapter<ItemImageBinding, Image_>() {
    override fun getViewBinding(
        inflater: LayoutInflater,
        parent: ViewGroup
    ) = ItemImageBinding.inflate(inflater, parent, false)

    private fun checkCropImage() =
        mainImageViewModel.getActionImage() == ActionImage.CROP_COMPRESS && mainImageViewModel.getImagesSelected().size == 1

    override
    fun onBind(binding: ItemImageBinding, image: Image_, position: Int) {
        if (checkCropImage()) return
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
                if (checkCropImage() && !image.isSelected) {
                    Toast.makeText(
                        it.context,
                        it.context.getString(R.string.noti),
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
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