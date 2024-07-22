package com.photo.imagecompressor.tools.presentation.main.fragment.crop_compress

import android.view.LayoutInflater
import android.view.ViewGroup
import com.photo.imagecompressor.tools.base.BaseAdapter
 import com.photo.imagecompressor.tools.databinding.ItemOptionTextCropBinding
import com.photo.imagecompressor.tools.domain.model.Option_Crop

class OptionCropAdapter : BaseAdapter<ItemOptionTextCropBinding, Option_Crop>() {
    override fun getViewBinding(
        inflater: LayoutInflater,
        parent: ViewGroup
    ) = ItemOptionTextCropBinding.inflate(inflater, parent, false)

    override fun onBind(binding: ItemOptionTextCropBinding, data: Option_Crop, position: Int) {
        binding.apply {
            textAspectRatio = data.name
            cropSelected = data.isSelected
        }
    }
}