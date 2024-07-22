package com.knd.duantotnghiep.testsocket.utils

import android.content.Context
import android.content.res.Resources.NotFoundException
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewParent
import androidx.viewbinding.ViewBinding
import com.knd.duantotnghiep.testsocket.R
import com.knd.duantotnghiep.testsocket.databinding.ItemCallVoiceMessageBinding
import com.knd.duantotnghiep.testsocket.databinding.ItemTextSentBinding
import com.knd.duantotnghiep.testsocket.databinding.ItemTextReceivedBinding
import com.knd.duantotnghiep.testsocket.databinding.ItemVideoSentBinding
import com.knd.duantotnghiep.testsocket.databinding.ItemVideoReceivedBinding
import com.knd.duantotnghiep.testsocket.databinding.ItemImageSentBinding
import com.knd.duantotnghiep.testsocket.databinding.ItemImageReceivedBinding
import com.knd.duantotnghiep.testsocket.databinding.ItemFileSentBinding
import com.knd.duantotnghiep.testsocket.databinding.ItemFileReceivedBinding
import com.knd.duantotnghiep.testsocket.databinding.ItemRecordSentBinding
import com.knd.duantotnghiep.testsocket.databinding.ItemRecordReceivedBinding

import com.knd.duantotnghiep.testsocket.databinding.ItemTextReplyTextFileRecordSentBinding
import com.knd.duantotnghiep.testsocket.databinding.ItemTextReplyTextFileRecordReceivedBinding
import com.knd.duantotnghiep.testsocket.databinding.ItemFileReplyTextFileRecordSentBinding
import com.knd.duantotnghiep.testsocket.databinding.ItemFileReplyTextFileRecordReceivedBinding
import com.knd.duantotnghiep.testsocket.databinding.ItemImageReplyTextFileRecordSentBinding
import com.knd.duantotnghiep.testsocket.databinding.ItemImageReplyTextFileRecordReceivedBinding
import com.knd.duantotnghiep.testsocket.databinding.ItemVideoReplyTextFileRecordSentBinding
import com.knd.duantotnghiep.testsocket.databinding.ItemVideoReplyTextFileRecordReceivedBinding
import com.knd.duantotnghiep.testsocket.databinding.ItemRecordReplyTextFileRecordSentBinding
import com.knd.duantotnghiep.testsocket.databinding.ItemRecordReplyTextFileRecordReceivedBinding
import com.knd.duantotnghiep.testsocket.databinding.ItemTextReplyImageVideoSentBinding
import com.knd.duantotnghiep.testsocket.databinding.ItemTextReplyImageVideoReceivedBinding
import com.knd.duantotnghiep.testsocket.databinding.ItemFileReplyImageVideoSentBinding
import com.knd.duantotnghiep.testsocket.databinding.ItemFileReplyImageVideoReceivedBinding
import com.knd.duantotnghiep.testsocket.databinding.ItemImageReplyImageVideoSentBinding
import com.knd.duantotnghiep.testsocket.databinding.ItemImageReplyImageVideoReceivedBinding
import com.knd.duantotnghiep.testsocket.databinding.ItemVideoReplyImageVideoSentBinding
import com.knd.duantotnghiep.testsocket.databinding.ItemVideoReplyImageVideoReceivedBinding
import com.knd.duantotnghiep.testsocket.databinding.ItemRecordReplyImageVideoSentBinding
import com.knd.duantotnghiep.testsocket.databinding.ItemRecordReplyImageVideoReceivedBinding
import com.knd.duantotnghiep.testsocket.enum.StatusMessageEnum
import com.knd.duantotnghiep.testsocket.enum.StatusWithdraw
import com.knd.duantotnghiep.testsocket.enum.TypeChatMessageEnum
import com.knd.duantotnghiep.testsocket.response.ChatResponse
import com.knd.duantotnghiep.testsocket.ui.chat_message.ViewTypeChatMessage

object Utils {
    var idUserCurrent: Int = 0

    fun convertMillisecondsToTimers(milliseconds: Long): String {
        val seconds = milliseconds / 1000
        val minutes = seconds / 60
        val hours = minutes / 60

        val formattedHours = String.format("%02d", hours)
        val formattedMinutes = String.format("%02d", minutes % 60)
        val formattedSeconds = String.format("%02d", seconds % 60)

        return "$formattedHours:$formattedMinutes:$formattedSeconds"
    }


}