package com.photo.imagecompressor.tools.presentation.main.fragment.select_images

import android.view.LayoutInflater
import android.view.ViewGroup
import com.photo.imagecompressor.tools.base.BaseAdapter
import com.photo.imagecompressor.tools.databinding.ItemFolderImageBinding
import com.photo.imagecompressor.tools.domain.model.Folder_Image
import com.photo.imagecompressor.tools.utils.Utils


class FolderImageAdapter :
    BaseAdapter<ItemFolderImageBinding, Folder_Image>() {
    override fun getViewBinding(
        inflater: LayoutInflater,
        parent: ViewGroup
    ) = ItemFolderImageBinding.inflate(inflater, parent, false)

    override fun onBind(binding: ItemFolderImageBinding, data: Folder_Image, position: Int) {
        binding.apply {
            txtNameFolder.text = data.name
            txtPhotoNumber.text = data.totalImage.toString()
            data.firstImage?.let { Utils.loadImage(imgFirst, it) }
        }
    }
}