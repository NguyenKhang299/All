package com.photo.image.picture.tools.compressor.persentation.main.fragment.result

import android.content.Context
import android.content.Intent
import android.text.format.Formatter
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.photo.image.picture.tools.compressor.base.BaseAdapter
import com.photo.image.picture.tools.compressor.databinding.ItemResult2Binding
import com.photo.image.picture.tools.compressor.databinding.ItemResultBinding
import com.photo.image.picture.tools.compressor.domain.model.Image
import com.jaygoo.widget.RangeSeekBar
import com.photo.image.picture.tools.compressor.utils.Utils.Companion.fileToUri
import java.io.File

class RsAdapter(private val context: Context) :
    BaseAdapter<ItemResultBinding, Pair<Image, Image>>() {
    override fun getViewBinding(inflater: LayoutInflater, parent: ViewGroup) =
        ItemResultBinding.inflate(inflater, parent, false)

    override fun onBind(binding: ItemResultBinding, item: Pair<Image, Image>, position: Int) {
        val mediaBefore = item.first
        val mediaAfter = item.second

        setSeekBarAndSize(
            maxOf(mediaAfter.size, mediaBefore.size),
            binding.seekOriginal,
            binding.sizeBefore,
            mediaBefore.size.toFloat()
        )
        setSeekBarAndSize(
            maxOf(mediaAfter.size, mediaBefore.size),
            binding.seekCompressed,
            binding.sizeAfter,
            mediaAfter.size.toFloat()
        )
        binding.txtName.text = mediaAfter.name

    }

    private fun setSeekBarAndSize(
        maxProgress: Long,
        seekBar: RangeSeekBar,
        sizeTextView: TextView,
        size: Float
    ) {
        seekBar.isEnabled = false
        seekBar.setRange(0f, maxProgress.toFloat())
        seekBar.setProgress(size)
        sizeTextView.text = Formatter.formatFileSize(context, size.toLong())
    }
}