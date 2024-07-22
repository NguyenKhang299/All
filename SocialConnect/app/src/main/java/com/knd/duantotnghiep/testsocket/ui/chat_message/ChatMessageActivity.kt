package com.knd.duantotnghiep.testsocket.ui.chat_message

import android.Manifest
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.ItemTouchHelper
import com.knd.duantotnghiep.testsocket.R
import com.knd.duantotnghiep.testsocket.core.BaseActivity
import com.knd.duantotnghiep.testsocket.databinding.ActivityChatMessageBinding
import com.knd.duantotnghiep.testsocket.enum.StatusMessageEnum
import com.knd.duantotnghiep.testsocket.enum.StatusWithdraw
import com.knd.duantotnghiep.testsocket.enum.TypeChatMessageEnum
import com.knd.duantotnghiep.testsocket.my_interface.IChatAdapter
import com.knd.duantotnghiep.testsocket.response.ChatReplyResponse
import com.knd.duantotnghiep.testsocket.response.ChatResponse
import com.knd.duantotnghiep.testsocket.response.NotificationResponse
import com.knd.duantotnghiep.testsocket.response.Room
import com.knd.duantotnghiep.testsocket.response.UploadResponse

import com.knd.duantotnghiep.testsocket.ui.chat_message.record.RecordAction
import com.knd.duantotnghiep.testsocket.ui.chat_message.record.RecordManager
import com.knd.duantotnghiep.testsocket.utils.FeedbackEnum
import com.knd.duantotnghiep.testsocket.utils.MyNotificationManager
import com.knd.duantotnghiep.testsocket.utils.POST_NOTIFICATION
import com.knd.duantotnghiep.testsocket.utils.PermissionStatus
import com.knd.duantotnghiep.testsocket.utils.RECORD_AUDIO
import com.knd.duantotnghiep.testsocket.utils.RequestPermission
import com.knd.duantotnghiep.testsocket.utils.TypeNotificationEnum
import com.knd.duantotnghiep.testsocket.utils.Utils
import com.knd.duantotnghiep.testsocket.utils.checkSelfPermission
import com.knd.duantotnghiep.testsocket.utils.hasPostNoti
import com.knd.duantotnghiep.testsocket.utils.hasRecord
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.DelicateCoroutinesApi


@AndroidEntryPoint
class ChatMessageActivity : BaseActivity<ActivityChatMessageBinding>(),            SwiperListener,
    IChatAdapter.ClickReplyListener {

    private val viewModel by lazy { ChatMessageViewModel() }
    private val listData = ArrayList<ChatResponse>()
    override fun initData() {
        val chatResponse = ChatResponse()
        chatResponse.apply {
            messageReply = ChatReplyResponse()
                .apply {
                    message = "Day la tin nhan phan hoi"
                    nickName = "Nguyễn Duy Khiêm"
                    senderID = 2
                }
            sendTo = ""
            isSeen = false
            callDuration = 10
            senderID = 1
            nickName = "Nguyễn Duy Khang"
            avatar =
                "https://img.thuthuatphanmem.vn/uploads/2018/10/06/anh-3d-anime-girl-ca-tinh_033448756.jpg"
            message = "Hello babe"
            files = arrayListOf(
                UploadResponse(
                    1,
                    "image",
                    "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
                    "video", emptyList(),
                    "15MB", System.currentTimeMillis()
                )
            )
            typeMessage = TypeChatMessageEnum.VIDEO.name
            status = arrayListOf(StatusMessageEnum.SENT.name)
        }
        listData.add(chatResponse)
        val chatResponse2 = ChatResponse()
        chatResponse2.apply {
            messageReply = ChatReplyResponse()
                .apply {
                    message = "Day la tin nhan phan hoi"
                    nickName = "Nguyễn Duy Khiêm"
                    senderID = 2

                }
            sendTo = ""
            isSeen = false
            callDuration = 10
            senderID = 0
            nickName = "Nguyễn Duy Khang"
            avatar =
                "https://img.thuthuatphanmem.vn/uploads/2018/10/06/anh-3d-anime-girl-ca-tinh_033448756.jpg"
            message = "Hello babe2"
            files = arrayListOf(
                UploadResponse(
                    1,
                    "image",
                    "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
                    "image/png", emptyList(), "15MB",
                    System.currentTimeMillis()
                )
            )
            typeMessage = TypeChatMessageEnum.IMAGE_REPLY_TEXT_FILE_RECORD.name
            status = arrayListOf(StatusMessageEnum.SENT.name)
        }
        listData.add(chatResponse2)
        listData.add(ChatResponse().apply {
            statusWithdraw = StatusWithdraw.WITHDRAW_ALL.name
        })
    }

    private val recordManager = RecordManager(this, object : RecordManager.OnRecordListener {
        override fun onStatus(action: RecordAction) {
            when (action) {
                is RecordAction.Start -> {
                    showMessage("Start")

                }

                is RecordAction.Stop -> {
                    showMessage("Stop")

                }

                is RecordAction.Resume -> {
                    showMessage("Resume")

                }

                is RecordAction.Pause -> {
                    showMessage("Pause")
                }

                else -> {}
            }
        }

        override fun onMaxAmplitude(maxAmplitude: Int) {
            binding.includeRecord.waveformView.addAmplitude(maxAmplitude)
        }

        override fun onTimerTick(duration: String) {
            binding.includeRecord.txtTime.text = duration
        }
    })

    private val rqPermissions = RequestPermission(this)


    override fun initView() {
        rqPermissions.status.observe(this) {

            when (it) {
                is PermissionStatus.Granted -> {

                 }

                else -> {}
            }
        }

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        setAdapterChatMessage()
        if (!hasPostNoti()) {
            rqPermissions.launch(POST_NOTIFICATION)
        }
        rqPermissions.launch(Manifest.permission.USE_FULL_SCREEN_INTENT)

        binding.btn.setOnClickListener {
            val chatResponse = ChatResponse()
            chatResponse.apply {
                messageReply = ChatReplyResponse()
                    .apply {
                        message = "Day la tin nhan phan hoi"
                        nickName = "Nguyễn Duy Khiêm"
                        senderID = 2
                    }
                sendTo = ""
                isSeen = false
                callDuration = 10
                senderID = 1
                nickName = "Nguyễn Duy Khang"
                avatar =
                    "https://img.thuthuatphanmem.vn/uploads/2018/10/06/anh-3d-anime-girl-ca-tinh_033448756.jpg"
                message = "Hello babe"
                files = arrayListOf(
                    UploadResponse(
                        1,
                        "image",
                        "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
                        "video", emptyList(),
                        "15MB", System.currentTimeMillis()
                    )
                )
                typeMessage = TypeChatMessageEnum.VIDEO.name
                status = arrayListOf(StatusMessageEnum.SENT.name)
            }
            MyNotificationManager.handleShowNotification(this, NotificationResponse(
                1,chatResponse,Room(
                    15,"Test Group","https://haycafe.vn/wp-content/uploads/2022/02/Tai-anh-girl-gai-dep-de-thuong-ve-may.jpg"
                ),false, TypeNotificationEnum.MESSAGE.name, FeedbackEnum.NONE.name
            ))
           // if (hasRecord()) recordManager.startRecord() else rqPermissions.launch(RECORD_AUDIO)
        }
        recordManager.apply {
            dirPath = cacheDir.path
            filename = "khang"
        }
        binding.includeRecord.action.setOnClickListener {
            recordManager.pauseRecord()
        }

    }

    private fun setAdapterChatMessage() {
        val chatMessageAdapter = ChatMessageAdapter(this)
        binding.recyclerView.apply {
            binding.includeRecord.waveformView.apply {
                color = Color.WHITE
            }
            chatMessageAdapter.submitData = listData
            setHasFixedSize(true)
            addItemDecoration(ItemDecoration(ChatMessageAdapter.SPACE))

            val swiperChat = SwiperChat(this@ChatMessageActivity, this@ChatMessageActivity)

            val itemTouchHelper = ItemTouchHelper(swiperChat)
            itemTouchHelper.attachToRecyclerView(this)
            adapter = chatMessageAdapter
            chatMessageAdapter.setOnTouchListener {
                handlerTouchItemChatMessage(swiperChat, it)
            }
        }
    }

    private fun handlerTouchItemChatMessage(swiperChat: SwiperChat, chatResponse: ChatResponse) {
        swiperChat.typeSwipeDirs = if (chatResponse.statusWithdraw == StatusWithdraw.NONE.name ||
            chatResponse.typeMessage == TypeChatMessageEnum.VIDEO_CALL.name ||
            chatResponse.typeMessage == TypeChatMessageEnum.VOICE_CALL.name
        ) {
            if (chatResponse.senderID == Utils.idUserCurrent) ItemTouchHelper.RIGHT else ItemTouchHelper.LEFT
        } else -1
    }

    override fun getMenu(): Int {
        return R.menu.chat_mess_menu
    }

    override fun getViewBinding(): ActivityChatMessageBinding {
        return ActivityChatMessageBinding.inflate(layoutInflater)
    }

    override fun onSwiped(position: Int) {
        showMessage(listData[position].message.toString())
    }

    override fun onClickReply(id: Int) {
        showMessage("$id reply")
        listData.indexOfFirst { it.id == id }.let {
            if (it != -1) {
                binding.recyclerView.smoothScrollToPosition(it)
            }
        }
    }
}