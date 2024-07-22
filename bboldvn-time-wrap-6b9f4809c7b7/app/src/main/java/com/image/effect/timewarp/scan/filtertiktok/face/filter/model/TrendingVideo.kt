package com.image.effect.timewarp.scan.filtertiktok.face.filter.model

data class TrendingVideo(
    val upload_time: Long? = 0,
    val credit: String? = "",
    val thumbnail_url: String? = "",
    val video_url: String? = ""
) : java.io.Serializable