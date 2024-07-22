package com.photo.imagecompressor.tools.presentation.main.fragment.compress_images

import android.view.LayoutInflater
import android.view.ViewGroup
import com.photo.imagecompressor.tools.base.BaseAdapter
import com.photo.imagecompressor.tools.utils.Utils
import com.photo.imagecompressor.tools.databinding.ItemImageSelectionCompressBinding
import com.photo.imagecompressor.tools.domain.model.Image_

class ImageSelectedAdapter : BaseAdapter<ItemImageSelectionCompressBinding, Image_>() {
    override fun getViewBinding(
        inflater: LayoutInflater,
        parent: ViewGroup
    ) = ItemImageSelectionCompressBinding.inflate(inflater, parent, false)

    override fun onBind(binding: ItemImageSelectionCompressBinding, data: Image_, position: Int) {
        Utils.loadImage(binding.img, data.path)
    }
}