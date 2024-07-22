package com.knd.duantotnghiep.testsocket.ui.chat_message

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.VideoView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.knd.duantotnghiep.testsocket.core.BaseAdapterMultiView
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
import com.knd.duantotnghiep.testsocket.databinding.ItemTextRevokedBinding
import com.knd.duantotnghiep.testsocket.databinding.ItemTextSentBinding
import com.knd.duantotnghiep.testsocket.databinding.ItemVideoReceivedBinding
import com.knd.duantotnghiep.testsocket.databinding.ItemVideoReplyImageVideoReceivedBinding
import com.knd.duantotnghiep.testsocket.databinding.ItemVideoReplyImageVideoSentBinding
import com.knd.duantotnghiep.testsocket.databinding.ItemVideoReplyTextFileRecordReceivedBinding
import com.knd.duantotnghiep.testsocket.databinding.ItemVideoReplyTextFileRecordSentBinding
import com.knd.duantotnghiep.testsocket.databinding.ItemVideoSentBinding
import com.knd.duantotnghiep.testsocket.enum.StatusWithdraw
import com.knd.duantotnghiep.testsocket.enum.TypeChatMessageEnum
import com.knd.duantotnghiep.testsocket.my_interface.IChatAdapter
import com.knd.duantotnghiep.testsocket.response.ChatResponse
import com.knd.duantotnghiep.testsocket.response.UploadResponse
import com.knd.duantotnghiep.testsocket.utils.ChatMessageAdapterUtils
import com.knd.duantotnghiep.testsocket.utils.ChatMessageAdapterUtils.handleNameUserReplyMessage
import com.knd.duantotnghiep.testsocket.utils.ChatMessageAdapterUtils.handleWithdrawnReplyMessageTextFileRecord

import com.knd.duantotnghiep.testsocket.utils.Utils

class ChatMessageAdapter(private val iChatAdapter: IChatAdapter.ClickReplyListener) :
    BaseAdapterMultiView<ChatResponse>() {
    companion object {
        const val SPACE = 15
    }


    override fun getViewBinding(
        inflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): ViewBinding {
        if (viewType == TypeChatMessageEnum.values().size) return ItemTextRevokedBinding.inflate(
            inflater, parent, false
        )

        return ChatMessageAdapterUtils.getViewBinding(inflater, parent, viewType)
    }

    private lateinit var chatResponse: ChatResponse
    override fun onBind(binding: ViewBinding, item: ChatResponse, viewType: Int) {
        val context = binding.root.context
        val isUserCurrent = item.id == Utils.idUserCurrent
        chatResponse = item
        when (binding) {
            is ItemTextRevokedBinding -> binding.isCurrentUserSent = isUserCurrent

            is ItemTextSentBinding -> {
                binding.message = item.message
            }

            is ItemTextReceivedBinding -> {
                binding.message = item.message
            }

            is ItemVideoSentBinding -> {
                handleVideoView(binding.video)
            }

            is ItemVideoReceivedBinding -> {
                handleVideoView(binding.video)
            }

            is ItemImageSentBinding -> {
                binding.urls = item.files
                handleImageAdapter(binding.recyclerView, item.files)

            }

            is ItemImageReceivedBinding -> {
                binding.urls = item.files
                handleImageAdapter(binding.recyclerView, item.files)

            }

            is ItemFileSentBinding -> {
                binding.file = item.files[0]
            }

            is ItemFileReceivedBinding -> {
                binding.file = item.files[0]
            }

            is ItemRecordSentBinding -> {

            }

            is ItemRecordReceivedBinding -> {

            }

            is ItemCallVoiceMessageBinding -> {

            }


            is ItemTextReplyTextFileRecordSentBinding -> {
                binding.apply {
                    message = item.message
                    messageReply = context.handleWithdrawnReplyMessageTextFileRecord(item)
                    context.handleNameUserReplyMessage(item, isUserCurrent, this::setNameUserReply)
                    onClickReply(binding.cardReply)
                }
            }

            is ItemTextReplyTextFileRecordReceivedBinding -> {
                binding.apply {
                    message = item.message
                    messageReply = context.handleWithdrawnReplyMessageTextFileRecord(item)
                    context.handleNameUserReplyMessage(item, isUserCurrent, this::setNameUserReply)
                    onClickReply(binding.cardReply)
                }
            }

            is ItemFileReplyTextFileRecordSentBinding -> {
                binding.apply {
                    file = item.files[0]
                    messageReply = context.handleWithdrawnReplyMessageTextFileRecord(item)
                    context.handleNameUserReplyMessage(item, isUserCurrent, this::setNameUserReply)
                    onClickReply(binding.cardReply)
                }
            }

            is ItemFileReplyTextFileRecordReceivedBinding -> {
                binding.apply {
                    file = item.files[0]
                    messageReply = context.handleWithdrawnReplyMessageTextFileRecord(item)
                    context.handleNameUserReplyMessage(item, isUserCurrent, this::setNameUserReply)
                    onClickReply(binding.cardReply)
                }
            }

            is ItemImageReplyTextFileRecordSentBinding -> {
                binding.apply {
                    handleImageAdapter(binding.itemImageSent.recyclerView, item.files)
                    urls = item.files
                    messageReply = context.handleWithdrawnReplyMessageTextFileRecord(item)
                    context.handleNameUserReplyMessage(item, isUserCurrent, this::setNameUserReply)
                    onClickReply(binding.cardReply)
                }
            }

            is ItemImageReplyTextFileRecordReceivedBinding -> {
                binding.apply {
                    handleImageAdapter(binding.itemImageSent.recyclerView, item.files)
                    urls = item.files
                    messageReply = context.handleWithdrawnReplyMessageTextFileRecord(item)
                    context.handleNameUserReplyMessage(item, isUserCurrent, this::setNameUserReply)
                    onClickReply(binding.cardReply)
                }
            }

            is ItemVideoReplyTextFileRecordSentBinding -> {
                binding.apply {
                    handleVideoView(binding.includeVideoSent.video)
                    messageReply = context.handleWithdrawnReplyMessageTextFileRecord(item)
                    context.handleNameUserReplyMessage(item, isUserCurrent, this::setNameUserReply)
                    onClickReply(binding.cardReply)
                }
            }

            is ItemVideoReplyTextFileRecordReceivedBinding -> {
                binding.apply {
                    handleVideoView(binding.includeVideoReceiver.video)
                    messageReply = context.handleWithdrawnReplyMessageTextFileRecord(item)
                    context.handleNameUserReplyMessage(item, isUserCurrent, this::setNameUserReply)
                    onClickReply(binding.cardReply)
                }
            }

            is ItemRecordReplyTextFileRecordSentBinding -> {

            }

            is ItemRecordReplyTextFileRecordReceivedBinding -> {


            }

            is ItemTextReplyImageVideoSentBinding -> {
                binding.apply {
                    message = item.message
                    isVideo = isVideo()
                    urlReply = getFirstFileChatReply()
                    isCurrentUserSent = isUserCurrent
                    context.handleNameUserReplyMessage(item, isUserCurrent, this::setNameUserReply)
                    onClickReply(binding.cardReply)
                }
            }

            is ItemTextReplyImageVideoReceivedBinding -> {
                binding.apply {
                    message = item.message
                    isVideo = isVideo()
                    urlReply = getFirstFileChatReply()
                    isCurrentUserSent = isUserCurrent
                    context.handleNameUserReplyMessage(item, isUserCurrent, this::setNameUserReply)
                    onClickReply(binding.cardReply)
                }
            }

            is ItemFileReplyImageVideoSentBinding -> {
                binding.apply {
                    file = item.files[0]
                    isVideo = isVideo()
                    urlReply = getFirstFileChatReply()
                    isCurrentUserSent = isUserCurrent
                    context.handleNameUserReplyMessage(item, isUserCurrent, this::setNameUserReply)
                    onClickReply(binding.cardReply)
                }
            }

            is ItemFileReplyImageVideoReceivedBinding -> {
                binding.apply {
                    file = item.files[0]
                    isVideo = isVideo()
                    urlReply = getFirstFileChatReply()
                    isCurrentUserSent = isUserCurrent
                    context.handleNameUserReplyMessage(item, isUserCurrent, this::setNameUserReply)
                    onClickReply(binding.cardReply)
                }
            }

            is ItemImageReplyImageVideoSentBinding -> {
                binding.apply {
                    urls = item.files
                    isVideo = isVideo()
                    urlReply = getFirstFileChatReply()
                    isCurrentUserSent = isUserCurrent
                    context.handleNameUserReplyMessage(item, isUserCurrent, this::setNameUserReply)
                    onClickReply(binding.cardReply)
                }
            }

            is ItemImageReplyImageVideoReceivedBinding -> {
                binding.apply {
                    urls = item.files
                    isVideo = isVideo()
                    isCurrentUserSent = isUserCurrent
                    urlReply = getFirstFileChatReply()
                    context.handleNameUserReplyMessage(item, isUserCurrent, this::setNameUserReply)
                    onClickReply(binding.cardReply)
                }
            }

            is ItemVideoReplyImageVideoSentBinding -> {
                binding.apply {
                    handleVideoView(binding.includeVideoSent.video)
                    isVideo = isVideo()
                    isCurrentUserSent = isUserCurrent
                    urlReply = getFirstFileChatReply()
                    context.handleNameUserReplyMessage(item, isUserCurrent, this::setNameUserReply)
                    onClickReply(binding.cardReply)
                }
            }

            is ItemVideoReplyImageVideoReceivedBinding -> {
                binding.apply {
                    handleVideoView(binding.includeVideoReceived.video)
                    isVideo = isVideo()
                    urlReply = getFirstFileChatReply()
                    isCurrentUserSent = isUserCurrent
                    context.handleNameUserReplyMessage(item, isUserCurrent, this::setNameUserReply)
                    onClickReply(binding.cardReply)
                }
            }

            is ItemRecordReplyImageVideoSentBinding -> {
                binding.apply {

                }

            }

            is ItemRecordReplyImageVideoReceivedBinding -> {
                binding.apply {

                }

            }

            else -> throw IllegalArgumentException("Unknown binding type: $binding")
        }
    }

    private fun handleImageAdapter(recyclerView: RecyclerView, files: ArrayList<UploadResponse>) {
        if (files.size > 1) {
            recyclerView.adapter = setImageAdapter(files)
        }
    }

    private fun getFirstFileChatReply(): String =
        chatResponse.messageReply!!.files!![0].toString()


    private fun isVideo(): Boolean =
        chatResponse.typeMessage == TypeChatMessageEnum.VIDEO.name

    private fun handleVideoView(video: VideoView) {
        video.apply {
            setVideoPath(chatResponse.files[0].filePath)
            setMediaController(MediaController(context))
        }
    }

    private fun onClickReply(cardReply: CardView) {
        cardReply.setOnClickListener {
            iChatAdapter.onClickReply(chatResponse.messageReply?.id ?: -1)
        }
    }

    private fun setImageAdapter(files: ArrayList<UploadResponse>): ImagesAdapter {
        return ImagesAdapter().apply {
            val imagesAdapter = ImagesAdapter()
            imagesAdapter.submitData = files
            imagesAdapter.setOnLongClickListener {
                iChatAdapter.onClickReply(chatResponse.id)
            }
        }
    }


    override fun getItemViewTypeCustom(position: Int): Int {
        val chatMessage = submitData[position]
        val typeMessage = chatMessage.typeMessage
        if (chatMessage.statusWithdraw == StatusWithdraw.WITHDRAW_ALL.name) return TypeChatMessageEnum.entries.size
        return ChatMessageAdapterUtils.processViewTypeChatMessage(typeMessage, chatMessage.senderID)
    }
}