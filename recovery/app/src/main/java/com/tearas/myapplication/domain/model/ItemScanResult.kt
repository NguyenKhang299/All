package com.tearas.myapplication.domain.model

import androidx.annotation.DrawableRes
import com.tearas.myapplication.R

data class ItemScanResult(
    @DrawableRes val icon: Int,
    val title: Int,
    val mediaFound: Int,
    val description: Int,
)

val ListItemScanResults = mapOf(
    MediaType.IMAGE to ItemScanResult(
        R.drawable.image,
        R.string.restore_images,
        R.string.images_found,
        R.string.click_to_scan_new_images
    ),
    MediaType.AUDIO to ItemScanResult(
        R.drawable.audio,
        R.string.restore_audio,
        R.string.audio_found,
        R.string.click_to_scan_new_audios
    ),
    MediaType.VIDEO to ItemScanResult(
        R.drawable.video,
        R.string.restore_video,
        R.string.videos_found,
        R.string.click_to_scan_new_videos
    ),
    MediaType.FILES to ItemScanResult(
        R.drawable.document,
        R.string.restore_document,
        R.string.documents_found,
        R.string.click_to_scan_new_documents
    )
)