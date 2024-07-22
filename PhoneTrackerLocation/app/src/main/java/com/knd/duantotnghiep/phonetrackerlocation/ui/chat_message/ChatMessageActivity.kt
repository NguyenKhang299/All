package com.knd.duantotnghiep.phonetrackerlocation.ui.chat_message

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.knd.duantotnghiep.phonetrackerlocation.R
import com.knd.duantotnghiep.phonetrackerlocation.core.BaseActivity
import com.knd.duantotnghiep.phonetrackerlocation.databinding.ActivityChatMessageBinding
import com.knd.duantotnghiep.phonetrackerlocation.models.ChatMessage
import com.knd.duantotnghiep.phonetrackerlocation.models.UserRequest
import com.knd.duantotnghiep.phonetrackerlocation.socket.SocketAction
import com.knd.duantotnghiep.phonetrackerlocation.utils.ActivityExtensions.showMessage
import com.knd.duantotnghiep.phonetrackerlocation.utils.NetworkResult
import com.knd.duantotnghiep.phonetrackerlocation.utils.UserPreferencesManager
import com.knd.duantotnghiep.phonetrackerlocation.utils.getSerializableExtra
import javax.inject.Inject

class ChatMessageActivity : BaseActivity<ActivityChatMessageBinding>() {
    private lateinit var messageAdapter: MessageAdapter
    private val chatMessageViewModel by viewModels<ChatMessageViewModel>()
    private lateinit var userOther: UserRequest
    private lateinit var idUserCurrent: String
    private lateinit var nameUserCurrent: String
    private lateinit var listChatMessage: MutableList<ChatMessage>

    @Inject
    lateinit var uPm: UserPreferencesManager
    override fun getViewBinding(): ActivityChatMessageBinding {
        return ActivityChatMessageBinding.inflate(layoutInflater)
    }

    override fun initData() {
        messageAdapter = MessageAdapter()
        userOther = intent.getSerializableExtra<UserRequest>("user")!!
        uPm.getCurrentUser()!!.apply {
            idUserCurrent = _id!!
            nameUserCurrent = name
        }

    }

    override fun initView() {

        with(binding) {

            rcy.adapter = messageAdapter

            bnSend.setOnClickListener {
                val chatMessage = ChatMessage(
                    senderId = idUserCurrent,
                    receiverId = userOther._id!!,
                    messageText = txtMess.text.toString(),
                    senderNickname = nameUserCurrent,
                    receiverNickname = userOther.name
                )
                chatMessageViewModel.handleSocket(SocketAction.SendMessage(chatMessage))
            }

        }

    }

    override fun initObserver() {
        with(chatMessageViewModel) {
            onReceiverNewMessage.observe(this@ChatMessageActivity) {
                listChatMessage.add(it)
                val lastPosition = listChatMessage.size - 1
                val layoutManager = binding.rcy.layoutManager!!
                val checkLastPosition = (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()==lastPosition
                if (checkLastPosition){
                    layoutManager.scrollToPosition(lastPosition)
                }else{
                    showMessage(it.messageText)
                }
                messageAdapter.notifyItemInserted(lastPosition)
            }

             onGetChatMessage.observe(this@ChatMessageActivity){ result->
                 when (result) {
                     is NetworkResult.Success -> {
                         listChatMessage = result.data!!.toMutableList()
                         messageAdapter.setData(listChatMessage)
                     }
                     is NetworkResult.Error -> {
                         // Handle error: $result.message$
                     }
                     is NetworkResult.Loading -> {
                         // Show loading UI
                     }
                 }

             }
            onChangeNetwork.observe(this@ChatMessageActivity) {
                when (it) {
                    true -> {
                        chatMessageViewModel.handle(ChatMessageAction.GetChatMessage(userOther._id!!))
                    }

                    false -> {
                        showMessage("Network error")
                    }
                }
            }

        }
    }
}