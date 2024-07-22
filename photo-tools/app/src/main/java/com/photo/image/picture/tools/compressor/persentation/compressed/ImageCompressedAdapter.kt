package com.photo.image.picture.tools.compressor.persentation.compressed

import android.net.Uri
import android.text.format.Formatter
import android.view.LayoutInflater
import android.view.ViewGroup
import com.photo.image.picture.tools.compressor.base.BaseAdapter
import com.photo.image.picture.tools.compressor.databinding.ItemCompressedBinding
import com.photo.image.picture.tools.compressor.domain.model.Image
import com.photo.image.picture.tools.compressor.utils.Utils
import com.photo.image.picture.tools.compressor.utils.Utils.Companion.fileToUri
import java.io.File

interface ImageCompressedEvent {
    fun shareImage(uri: Uri)
    fun deleteImage(path: Long, position: Int)
}

class ImageCompressedAdapter(private val imageCompressedEvent: ImageCompressedEvent) :
    BaseAdapter<ItemCompressedBinding, Image>() {
    override fun getViewBinding(
        inflater: LayoutInflater,
        parent: ViewGroup
    ) = ItemCompressedBinding.inflate(inflater, parent, false)

    override fun onBind(binding: ItemCompressedBinding, data: Image, position: Int) {
        binding.apply {
            Utils.loadImage(binding.roundedImageView, data.path)
            txtName.text = data.name
            txtSize.text = Formatter.formatFileSize(root.context, data.size)
            delete.setOnClickListener { imageCompressedEvent.deleteImage(data.id.toLong(), position) }
            share.setOnClickListener {
                val file = File(data.path)
                imageCompressedEvent.shareImage(root.context.fileToUri(file))
            }
        }
    }
}