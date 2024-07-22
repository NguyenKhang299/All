package com.photo.imagecompressor.tools.action

import java.io.Serializable

enum class ActionImage : Serializable {
    COMPRESS_PHOTO, ADVANCE_COMPRESS, RESIZE_COMPRESS, CROP_COMPRESS, CONVERT_PHOTO
}