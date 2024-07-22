package com.photo.imagecompressor.tools.presentation.main.fragment.result

import android.content.Context
import android.text.format.Formatter
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.photo.imagecompressor.tools.base.BaseAdapter
import com.jaygoo.widget.RangeSeekBar
import com.photo.imagecompressor.tools.databinding.ItemResultBinding
import com.photo.imagecompressor.tools.domain.model.Image_

class RsAdapter(private val context: Context) :
    BaseAdapter<ItemResultBinding, Pair<Image_, Image_>>() {
    override fun getViewBinding(inflater: LayoutInflater, parent: ViewGroup) =
        ItemResultBinding.inflate(inflater, parent, false)

    override fun onBind(binding: ItemResultBinding, item: Pair<Image_, Image_>, position: Int) {
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