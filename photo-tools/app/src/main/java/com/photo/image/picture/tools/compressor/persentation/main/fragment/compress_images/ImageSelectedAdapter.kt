package com.photo.image.picture.tools.compressor.persentation.main.fragment.compress_images

import android.view.LayoutInflater
import android.view.ViewGroup
import com.photo.image.picture.tools.compressor.base.BaseAdapter
import com.photo.image.picture.tools.compressor.databinding.ItemImageSelectionCompressBinding
import com.photo.image.picture.tools.compressor.domain.model.Image
import com.photo.image.picture.tools.compressor.utils.Utils

class ImageSelectedAdapter : BaseAdapter<ItemImageSelectionCompressBinding, Image>() {
    override fun getViewBinding(
        inflater: LayoutInflater,
        parent: ViewGroup
    ) = ItemImageSelectionCompressBinding.inflate(inflater, parent, false)

    override fun onBind(binding: ItemImageSelectionCompressBinding, data: Image, position: Int) {
        Utils.loadImage(binding.img, data.path)
    }
}