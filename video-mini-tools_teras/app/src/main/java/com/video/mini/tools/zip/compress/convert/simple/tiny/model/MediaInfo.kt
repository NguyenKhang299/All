package com.video.mini.tools.zip.compress.convert.simple.tiny.model

import java.io.Serializable
fun Resolution.isLandscapeVideo( ): Boolean {
    return width >height
}

fun Resolution.isPortraitVideo( ): Boolean {
    return width < height
}
data class Resolution(var width: Int = 0, var height: Int = 0) : Serializable {
    override fun toString(): String {
        return if (height == 0) "$width" else "$width x $height"
    }



    fun getRatio() = width * 1.0f / height
}

data class MediaInfo(
    val id: Long,
    var name: String,
    val path: String,
    var size: Long,
    var resolution: Resolution? = null,
    var time: String,
    var mime: String,
    var bitrate: Long,
    val date: String,
    var isVideo: Boolean = true,
    var isSelected: Boolean = false,
    var stateCompression: StateCompression = StateCompression.Waiting
) : Serializable {

}

sealed class StateCompression : Serializable {
    data object Success : StateCompression()
    data object Failure : StateCompression()
    data object Waiting : StateCompression()
    data object Processing : StateCompression()
}

class MediaInfos : ArrayList<MediaInfo>() {}