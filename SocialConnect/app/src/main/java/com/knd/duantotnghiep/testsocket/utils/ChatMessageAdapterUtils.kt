package com.knd.duantotnghiep.testsocket.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.knd.duantotnghiep.testsocket.R
import com.knd.duantotnghiep.testsocket.databinding.ItemCallVoiceMessageBinding
import com.knd.duantotnghiep.testsocket.databinding.ItemFileReceivedBinding
import com.knd.duantotnghiep.testsocket.databinding.ItemFileReplyImageVideoReceivedBinding
import com.knd.duantotnghiep.testsocket.databinding.ItemFileReplyImageVideoSentBinding
import com.knd.duantotnghiep.testsocket.databinding.ItemFileReplyTextFileRecordReceivedBinding
import com.knd.duantotnghiep.testsocket.databinding.ItemFileReplyTextFileRecordSentBinding
import com.knd.duantotnghiep.testsocket.databinding.ItemFileSentBinding
import com.knd.duantotnghiep.testsocket.databinding.ItemImageReceivedBinding
import com.knd.duantotnghiep.testsocket.databinding.ItemImageReplyImageVideoReceivedBinding
import com.knd.duantotnghiep.testsocket.databinding.ItemImageReplyImageVideoSentBinding
import com.knd.duantotnghiep.testsocket.databinding.ItemImageReplyTextFileRecordReceivedBinding
import com.knd.duantotnghiep.testsocket.databinding.ItemImageReplyTextFileRecordSentBinding
import com.knd.duantotnghiep.testsocket.databinding.ItemImageSentBinding
import com.knd.duantotnghiep.testsocket.databinding.ItemRecordReceivedBinding
import com.knd.duantotnghiep.testsocket.databinding.ItemRecordReplyImageVideoReceivedBinding
import com.knd.duantotnghiep.testsocket.databinding.ItemRecordReplyImageVideoSentBinding
import com.knd.duantotnghiep.testsocket.databinding.ItemRecordReplyTextFileRecordReceivedBinding
import com.knd.duantotnghiep.testsocket.databinding.ItemRecordReplyTextFileRecordSentBinding
import com.knd.duantotnghiep.testsocket.databinding.ItemRecordSentBinding
import com.knd.duantotnghiep.testsocket.databinding.ItemTextReceivedBinding
import com.knd.duantotnghiep.testsocket.databinding.ItemTextReplyImageVideoReceivedBinding
import com.knd.duantotnghiep.testsocket.databinding.ItemTextReplyImageVideoSentBinding
import com.knd.duantotnghiep.testsocket.databinding.ItemTextReplyTextFileRecordReceivedBinding
import com.knd.duantotnghiep.testsocket.databinding.ItemTextReplyTextFileRecordSentBinding
import com.knd.duantotnghiep.testsocket.databinding.ItemTextSentBinding
import com.knd.duantotnghiep.testsocket.databinding.ItemVideoReceivedBinding
import com.knd.duantotnghiep.testsocket.databinding.ItemVideoReplyImageVideoReceivedBinding
import com.knd.duantotnghiep.testsocket.databinding.ItemVideoReplyImageVideoSentBinding
import com.knd.duantotnghiep.testsocket.databinding.ItemVideoReplyTextFileRecordReceivedBinding
import com.knd.duantotnghiep.testsocket.databinding.ItemVideoReplyTextFileRecordSentBinding
import com.knd.duantotnghiep.testsocket.databinding.ItemVideoSentBinding
import com.knd.duantotnghiep.testsocket.enum.StatusWithdraw
import com.knd.duantotnghiep.testsocket.enum.TypeChatMessageEnum
import com.knd.duantotnghiep.testsocket.response.ChatResponse
import com.knd.duantotnghiep.testsocket.ui.chat_message.ViewTypeChatMessage

object ChatMessageAdapterUtils {
    fun getViewBinding(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): ViewBinding {
        return when (viewType) {
            ViewTypeChatMessage.TEXT_SENT.ordinal -> ItemTextSentBinding.inflate(
                inflater, parent, false
            )

            ViewTypeChatMessage.TEXT_RECEIVED.ordinal -> ItemTextReceivedBinding.inflate(
                inflater, parent, false
            )

            ViewTypeChatMessage.VIDEO_SENT.ordinal -> ItemVideoSentBinding.inflate(
                inflater, parent, false
            )

            ViewTypeChatMessage.VIDEO_RECEIVED.ordinal -> ItemVideoReceivedBinding.inflate(
                inflater, parent, false
            )

            ViewTypeChatMessage.IMAGE_SENT.ordinal -> ItemImageSentBinding.inflate(
                inflater, parent, false
            )

            ViewTypeChatMessage.IMAGE_RECEIVED.ordinal -> ItemImageReceivedBinding.inflate(
                inflater, parent, false
            )

            ViewTypeChatMessage.FILE_SENT.ordinal -> ItemFileSentBinding.inflate(
                inflater, parent, false
            )

            ViewTypeChatMessage.FILE_RECEIVED.ordinal -> ItemFileReceivedBinding.inflate(
                inflater, parent, false
            )

            ViewTypeChatMessage.RECORD_SENT.ordinal -> ItemRecordSentBinding.inflate(
                inflater, parent, false
            )

            ViewTypeChatMessage.RECORD_RECEIVED.ordinal -> ItemRecordReceivedBinding.inflate(
                inflater, parent, false
            )

            ViewTypeChatMessage.VIDEO_CALL_SENT.ordinal -> ItemCallVoiceMessageBinding.inflate(
                inflater, parent, false
            )

            ViewTypeChatMessage.VIDEO_CALL_RECEIVED.ordinal -> ItemCallVoiceMessageBinding.inflate(
                inflater, parent, false
            )

            ViewTypeChatMessage.VOICE_CALL_SENT.ordinal -> ItemCallVoiceMessageBinding.inflate(
                inflater, parent, false
            )

            ViewTypeChatMessage.VOICE_CALL_RECEIVED.ordinal -> ItemCallVoiceMessageBinding.inflate(
                inflater, parent, false
            )

            ViewTypeChatMessage.TEXT_REPLY_TEXT_FILE_RECORD_SENT.ordinal -> ItemTextReplyTextFileRecordSentBinding.inflate(
                inflater, parent, false
            )

            ViewTypeChatMessage.TEXT_REPLY_TEXT_FILE_RECORD_RECEIVED.ordinal -> ItemTextReplyTextFileRecordReceivedBinding.inflate(
                inflater, parent, false
            )

            ViewTypeChatMessage.FILE_REPLY_TEXT_FILE_RECORD_SENT.ordinal -> ItemFileReplyTextFileRecordSentBinding.inflate(
                inflater, parent, false
            )

            ViewTypeChatMessage.FILE_REPLY_TEXT_FILE_RECORD_RECEIVED.ordinal -> ItemFileReplyTextFileRecordReceivedBinding.inflate(
                inflater, parent, false
            )

            ViewTypeChatMessage.IMAGE_REPLY_TEXT_FILE_RECORD_SENT.ordinal -> ItemImageReplyTextFileRecordSentBinding.inflate(
                inflater, parent, false
            )

            ViewTypeChatMessage.IMAGE_REPLY_TEXT_FILE_RECORD_RECEIVED.ordinal -> ItemImageReplyTextFileRecordReceivedBinding.inflate(
                inflater, parent, false
            )

            ViewTypeChatMessage.VIDEO_REPLY_TEXT_FILE_RECORD_SENT.ordinal -> ItemVideoReplyTextFileRecordSentBinding.inflate(
                inflater, parent, false
            )

            ViewTypeChatMessage.VIDEO_REPLY_TEXT_FILE_RECORD_RECEIVED.ordinal -> ItemVideoReplyTextFileRecordReceivedBinding.inflate(
                inflater, parent, false
            )

            ViewTypeChatMessage.RECORD_REPLY_TEXT_FILE_RECORD_SENT.ordinal -> ItemRecordReplyTextFileRecordSentBinding.inflate(
                inflater, parent, false
            )

            ViewTypeChatMessage.RECORD_REPLY_TEXT_FILE_RECORD_RECEIVED.ordinal -> ItemRecordReplyTextFileRecordReceivedBinding.inflate(
                inflater, parent, false
            )

            ViewTypeChatMessage.TEXT_REPLY_IMAGE_VIDEO_SENT.ordinal -> ItemTextReplyImageVideoSentBinding.inflate(
                inflater, parent, false
            )

            ViewTypeChatMessage.TEXT_REPLY_IMAGE_VIDEO_RECEIVED.ordinal -> ItemTextReplyImageVideoReceivedBinding.inflate(
                inflater, parent, false
            )

            ViewTypeChatMessage.FILE_REPLY_IMAGE_VIDEO_SENT.ordinal -> ItemFileReplyImageVideoSentBinding.inflate(
                inflater, parent, false
            )

            ViewTypeChatMessage.FILE_REPLY_IMAGE_VIDEO_RECEIVED.ordinal -> ItemFileReplyImageVideoReceivedBinding.inflate(
                inflater, parent, false
            )

            ViewTypeChatMessage.IMAGE_REPLY_IMAGE_VIDEO_SENT.ordinal -> ItemImageReplyImageVideoSentBinding.inflate(
                inflater, parent, false
            )

            ViewTypeChatMessage.IMAGE_REPLY_IMAGE_VIDEO_RECEIVED.ordinal -> ItemImageReplyImageVideoReceivedBinding.inflate(
                inflater, parent, false
            )

            ViewTypeChatMessage.VIDEO_REPLY_IMAGE_VIDEO_SENT.ordinal -> ItemVideoReplyImageVideoSentBinding.inflate(
                inflater, parent, false
            )

            ViewTypeChatMessage.VIDEO_REPLY_IMAGE_VIDEO_RECEIVED.ordinal -> ItemVideoReplyImageVideoReceivedBinding.inflate(
                inflater, parent, false
            )

            ViewTypeChatMessage.RECORD_REPLY_IMAGE_VIDEO_SENT.ordinal -> ItemRecordReplyImageVideoSentBinding.inflate(
                inflater, parent, false
            )

            ViewTypeChatMessage.RECORD_REPLY_IMAGE_VIDEO_RECEIVED.ordinal -> ItemRecordReplyImageVideoReceivedBinding.inflate(
                inflater, parent, false
            )

            else -> throw IllegalArgumentException("Unknown viewType: $viewType")
        }
    }


    fun processViewTypeChatMessage(typeMessage: String, id: Int) =
        when (TypeChatMessageEnum.valueOf(typeMessage)) {
            TypeChatMessageEnum.TEXT -> {
                if (id == Utils.idUserCurrent) ViewTypeChatMessage.TEXT_SENT.ordinal else ViewTypeChatMessage.TEXT_RECEIVED.ordinal
            }

            TypeChatMessageEnum.VIDEO -> {
                if (id == Utils.idUserCurrent) ViewTypeChatMessage.VIDEO_SENT.ordinal else ViewTypeChatMessage.VIDEO_RECEIVED.ordinal
            }

            TypeChatMessageEnum.IMAGE -> {
                if (id == Utils.idUserCurrent) ViewTypeChatMessage.IMAGE_SENT.ordinal else ViewTypeChatMessage.IMAGE_RECEIVED.ordinal
            }

            TypeChatMessageEnum.FILE -> {
                if (id == Utils.idUserCurrent) ViewTypeChatMessage.FILE_SENT.ordinal else ViewTypeChatMessage.FILE_RECEIVED.ordinal
            }

            TypeChatMessageEnum.RECORD -> {
                if (id == Utils.idUserCurrent) ViewTypeChatMessage.RECORD_SENT.ordinal else ViewTypeChatMessage.RECORD_RECEIVED.ordinal
            }

            TypeChatMessageEnum.VIDEO_CALL -> {
                if (id == Utils.idUserCurrent) ViewTypeChatMessage.VIDEO_CALL_RECEIVED.ordinal else ViewTypeChatMessage.VIDEO_CALL_RECEIVED.ordinal
            }

            TypeChatMessageEnum.VOICE_CALL -> {
                if (id == Utils.idUserCurrent) ViewTypeChatMessage.VOICE_CALL_SENT.ordinal else ViewTypeChatMessage.VOICE_CALL_RECEIVED.ordinal
            }

            TypeChatMessageEnum.TEXT_REPLY_TEXT_FILE_RECORD -> {
                if (id == Utils.idUserCurrent) ViewTypeChatMessage.TEXT_REPLY_TEXT_FILE_RECORD_SENT.ordinal else ViewTypeChatMessage.TEXT_REPLY_TEXT_FILE_RECORD_RECEIVED.ordinal
            }

            TypeChatMessageEnum.FILE_REPLY_TEXT_FILE_RECORD -> {
                if (id == Utils.idUserCurrent) ViewTypeChatMessage.FILE_REPLY_TEXT_FILE_RECORD_SENT.ordinal else ViewTypeChatMessage.FILE_REPLY_TEXT_FILE_RECORD_RECEIVED.ordinal
            }

            TypeChatMessageEnum.IMAGE_REPLY_TEXT_FILE_RECORD -> {
                if (id == Utils.idUserCurrent) ViewTypeChatMessage.IMAGE_REPLY_TEXT_FILE_RECORD_SENT.ordinal else ViewTypeChatMessage.IMAGE_REPLY_TEXT_FILE_RECORD_RECEIVED.ordinal
            }

            TypeChatMessageEnum.VIDEO_REPLY_TEXT_FILE_RECORD -> {
                if (id == Utils.idUserCurrent) ViewTypeChatMessage.VIDEO_REPLY_TEXT_FILE_RECORD_SENT.ordinal else ViewTypeChatMessage.VIDEO_REPLY_TEXT_FILE_RECORD_RECEIVED.ordinal
            }

            TypeChatMessageEnum.RECORD_REPLY_TEXT_FILE_RECORD -> {
                if (id == Utils.idUserCurrent) ViewTypeChatMessage.RECORD_REPLY_TEXT_FILE_RECORD_SENT.ordinal else ViewTypeChatMessage.RECORD_REPLY_TEXT_FILE_RECORD_RECEIVED.ordinal
            }

            TypeChatMessageEnum.TEXT_REPLY_IMAGE_VIDEO -> {
                if (id == Utils.idUserCurrent) ViewTypeChatMessage.TEXT_REPLY_IMAGE_VIDEO_SENT.ordinal else ViewTypeChatMessage.TEXT_REPLY_IMAGE_VIDEO_RECEIVED.ordinal
            }

            TypeChatMessageEnum.FILE_REPLY_IMAGE_VIDEO -> {
                if (id == Utils.idUserCurrent) ViewTypeChatMessage.FILE_REPLY_IMAGE_VIDEO_SENT.ordinal else ViewTypeChatMessage.FILE_REPLY_IMAGE_VIDEO_RECEIVED.ordinal
            }

            TypeChatMessageEnum.IMAGE_REPLY_IMAGE_VIDEO -> {
                if (id == Utils.idUserCurrent) ViewTypeChatMessage.IMAGE_REPLY_IMAGE_VIDEO_SENT.ordinal else ViewTypeChatMessage.IMAGE_REPLY_IMAGE_VIDEO_RECEIVED.ordinal
            }

            TypeChatMessageEnum.VIDEO_REPLY_IMAGE_VIDEO -> {
                if (id == Utils.idUserCurrent) ViewTypeChatMessage.VIDEO_REPLY_IMAGE_VIDEO_SENT.ordinal else ViewTypeChatMessage.VIDEO_REPLY_IMAGE_VIDEO_RECEIVED.ordinal
            }

            TypeChatMessageEnum.RECORD_REPLY_IMAGE_VIDEO -> {
                if (id == Utils.idUserCurrent) ViewTypeChatMessage.RECORD_REPLY_IMAGE_VIDEO_SENT.ordinal else ViewTypeChatMessage.RECORD_REPLY_IMAGE_VIDEO_RECEIVED.ordinal
            }

            else -> {
                -1
            }
        }


    fun Context.handleWithdrawnReplyMessageTextFileRecord(//Xử lí khi tin nhắn trả lời text,file,record  bị gỡ
        item: ChatResponse
    ): String? {
        val messageReply = item.messageReply
        val modifiedMessageReply = messageReply?.let {
            if (it.typeMessage == TypeChatMessageEnum.TEXT.name) {
                it.message
            } else {
                getString(R.string.attached_files)
            }
        }
        return modifiedMessageReply
    }



    fun Context.handleNameUserReplyMessage(//Xử lí Tên người dùng trả lời
        item: ChatResponse,
        isSeen: Boolean = false,
        callBack: (String) -> Unit
    ) {
        val messageReply = item.messageReply
        val modifiedNameUserReply = messageReply?.run {
            if (Utils.idUserCurrent != senderID && senderID != item.senderID) {
                "${item.nickName} ${getString(R.string.are_replying)} $nickName"
            } else if (Utils.idUserCurrent == senderID) {
                "${item.nickName} ${getString(R.string.are_replying)} ${getString(R.string.you)}"
            } else {
                "${item.nickName} ${getString(R.string.answered_himself)}"
            }
        }
            ?: if (isSeen) getString(R.string.you_replied_to_a_message_that_was_removed) else "${item.nickName} ${
                getString(
                    R.string.replied_to_a_message_that_was_removed
                )
            }"
        callBack(modifiedNameUserReply)
    }
}