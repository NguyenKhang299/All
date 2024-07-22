package com.photo.image.picture.tools.compressor.persentation.view

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.TextView

import androidx.databinding.BindingAdapter
import com.photo.image.picture.tools.compressor.R
import com.photo.image.picture.tools.compressor.action.CompressionQuantity

@BindingAdapter("app:compareOptionSelected", "app:compressionQuantity", requireAll = true)
fun View.compareOptionSelected(
    optionSelected: CompressionQuantity?,
    compressionQuantity: CompressionQuantity
) {
    val backgroundResId = when (optionSelected) {
        compressionQuantity -> R.drawable.bg_gradient
        else -> R.color.light_blue
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

