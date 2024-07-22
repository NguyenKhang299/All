package com.knd.duantotnghiep.phonetrackerlocation.utils

import android.graphics.Bitmap
import android.graphics.Typeface
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout
import com.squareup.picasso.Picasso
import java.lang.Error

@BindingAdapter("setImageBitmap")
fun setImageBitmap(imageView: ImageView, bitmap: Bitmap) {
    imageView.setImageBitmap(bitmap)
}

@BindingAdapter("url" )
fun setImageUrl(imageView: ImageView, url: String?) {
    if (url != null) Picasso.get().load(url).fit().into(imageView)
}

@BindingAdapter("setBold" )
fun setStyleText(txt: TextView, b:Boolean) {
    txt.typeface=if (b) Typeface.DEFAULT else Typeface.DEFAULT_BOLD
}

