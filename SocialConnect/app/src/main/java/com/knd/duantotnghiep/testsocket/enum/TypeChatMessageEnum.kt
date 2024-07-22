package com.knd.duantotnghiep.testsocket.enum

import com.knd.duantotnghiep.testsocket.ui.chat_message.ViewTypeChatMessage
import com.knd.duantotnghiep.testsocket.utils.Utils

enum class TypeChatMessageEnum {
    TEXT,
    VIDEO,
    IMAGE,
    FILE,
    RECORD,
    VIDEO_CALL,
    VOICE_CALL,

    TEXT_REPLY_TEXT_FILE_RECORD,
    FILE_REPLY_TEXT_FILE_RECORD,
    IMAGE_REPLY_TEXT_FILE_RECORD,
    VIDEO_REPLY_TEXT_FILE_RECORD,
    RECORD_REPLY_TEXT_FILE_RECORD,

    TEXT_REPLY_IMAGE_VIDEO,
    FILE_REPLY_IMAGE_VIDEO,
    IMAGE_REPLY_IMAGE_VIDEO,
    VIDEO_REPLY_IMAGE_VIDEO,
    RECORD_REPLY_IMAGE_VIDEO,


}
