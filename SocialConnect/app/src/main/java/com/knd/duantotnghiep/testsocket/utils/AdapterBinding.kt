package com.knd.duantotnghiep.testsocket.utils

import android.annotation.SuppressLint
import android.media.MediaMetadataRetriever
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter

@BindingAdapter("setVisibility")
fun View.setVisibility(isVisibility: Boolean) {
    val visibility = if (isVisibility) {
        View.VISIBLE
    } else {
        View.GONE
    }
    this.visibility = visibility
}

@SuppressLint("CheckResult")
@BindingAdapter("setImageView", "isLoad", requireAll = true)
fun ImageView.setImage(url: String, isLoad: Boolean) {

    if (isLoad && url.isNotEmpty()) {
        val mmr = MediaMetadataRetriever()
        mmr.setDataSource(url)
        val thumbnailBitmap = mmr.frameAtTime
        setImageBitmap(thumbnailBitmap)
    }
}
