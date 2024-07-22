package com.photo.imagecompressor.tools.presentation.compressed_

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.media.Image
import android.net.Uri
import android.provider.MediaStore
import android.text.format.Formatter
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import com.photo.imagecompressor.tools.base.BaseAdapter
import com.photo.imagecompressor.tools.databinding.ItemCompressedBinding
import com.photo.imagecompressor.tools.domain.model.Image_
import com.photo.imagecompressor.tools.utils.Utils
import com.photo.imagecompressor.tools.utils.Utils.Companion.fileToUri
import java.io.File
import java.net.URI


interface ImageCompressedEvent {
    fun shareImage(uri: Uri)
    fun deleteImage(path: Image_, position: Int)
}

class ImageCompressedAdapter(private val imageCompressedEvent: ImageCompressedEvent) :
    BaseAdapter<ItemCompressedBinding, Image_>() {
    override fun getViewBinding(
        inflater: LayoutInflater,
        parent: ViewGroup
    ) = ItemCompressedBinding.inflate(inflater, parent, false)

    override fun onBind(binding: ItemCompressedBinding, data: Image_, position: Int) {
        binding.apply {
            Utils.loadImage(binding.roundedImageView, data.path)
            txtName.text = data.name
            txtSize.text = Formatter.formatFileSize(root.context, data.size)
            delete.setOnClickListener { imageCompressedEvent.deleteImage(data, position) }
            share.setOnClickListener {
                 val path = it.context.getPath(data.id.toLong())
                imageCompressedEvent.shareImage(it.context.fileToUri(File(path)))
            }
        }
    }

    @SuppressLint("Range")
    fun Context.getPath(id: Long): String {
        val cursor = contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            null, MediaStore.Images.Media._ID + " = ? ", arrayOf(id.toString()), null
        )!!
        cursor.moveToFirst()
        val path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))!!
        cursor.close()
        return path
    }
}