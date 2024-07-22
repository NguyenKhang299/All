package com.photo.imagecompressor.tools.presentation.view

import android.graphics.Color
import android.view.View
import android.widget.TextView

import androidx.databinding.BindingAdapter
import com.photo.imagecompressor.tools.R
import com.photo.imagecompressor.tools.action.CompressionQuantity

@BindingAdapter("app:compareOptionSelected", "app:compressionQuantity", requireAll = true)
fun View.compareOptionSelected(
    optionSelected: CompressionQuantity?,
    compressionQuantity: CompressionQuantity
) {
    val backgroundResId = when (optionSelected) {
        compressionQuantity -> R.drawable.bg_item_selected
        else -> R.drawable.bg_view_child
    }
    setBackgroundResource(backgroundResId)
}


@BindingAdapter("app:cropSelected", requireAll = true)
fun TextView.cropSelected(
    boolean: Boolean
) {
    if (!boolean) {
        setTextColor(Color.GRAY)
        setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,null,null)
        return
    }
    setTextColor(Color.BLACK)
    setCompoundDrawablesWithIntrinsicBounds(null,null,null, context.getDrawable(R.drawable.ic_dot) )
}

