package com.photo.image.picture.tools.compressor.persentation.main.fragment.crop_compress

import android.view.LayoutInflater
import android.view.ViewGroup
import com.photo.image.picture.tools.compressor.base.BaseAdapter
import com.photo.image.picture.tools.compressor.databinding.ItemOptionTextCropBinding
import com.photo.image.picture.tools.compressor.domain.model.OptionCrop

class OptionCropAdapter : BaseAdapter<ItemOptionTextCropBinding, OptionCrop>() {
    override fun getViewBinding(
        inflater: LayoutInflater,
        parent: ViewGroup
    ) = ItemOptionTextCropBinding.inflate(inflater, parent, false)

    override fun onBind(binding: ItemOptionTextCropBinding, data: OptionCrop, position: Int) {
        binding.apply {
            textAspectRatio = data.name
            cropSelected = data.isSelected
        }
    }
}