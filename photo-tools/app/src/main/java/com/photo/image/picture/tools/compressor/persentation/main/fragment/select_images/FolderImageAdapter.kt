package com.photo.image.picture.tools.compressor.persentation.main.fragment.select_images

import android.view.LayoutInflater
import android.view.ViewGroup
import com.photo.image.picture.tools.compressor.base.BaseAdapter
import com.photo.image.picture.tools.compressor.databinding.ItemFolderImageBinding
import com.photo.image.picture.tools.compressor.domain.model.FolderImage
import com.photo.image.picture.tools.compressor.utils.Utils
import javax.inject.Inject


class FolderImageAdapter :
    BaseAdapter<ItemFolderImageBinding, FolderImage>() {
    override fun getViewBinding(
        inflater: LayoutInflater,
        parent: ViewGroup
    ) = ItemFolderImageBinding.inflate(inflater, parent, false)

    override fun onBind(binding: ItemFolderImageBinding, data: FolderImage, position: Int) {
        binding.apply {
            txtNameFolder.text = data.name
            txtPhotoNumber.text = data.totalImage.toString()
            data.firstImage?.let { Utils.loadImage(imgFirst, it) }
        }
    }
}