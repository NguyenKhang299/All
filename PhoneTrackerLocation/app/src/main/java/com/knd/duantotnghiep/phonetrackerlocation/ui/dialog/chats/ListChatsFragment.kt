package com.knd.duantotnghiep.phonetrackerlocation.ui.dialog.chats

import android.view.View
import androidx.fragment.app.viewModels
import com.knd.duantotnghiep.phonetrackerlocation.R
import com.knd.duantotnghiep.phonetrackerlocation.core.BaseBottomSheetFragment
import com.knd.duantotnghiep.phonetrackerlocation.databinding.FragmentListChatsBinding
import com.knd.duantotnghiep.phonetrackerlocation.models.ChatsResponse
import com.knd.duantotnghiep.phonetrackerlocation.utils.AdapterCallBack
import com.knd.duantotnghiep.phonetrackerlocation.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
@AndroidEntryPoint
class ListChatsFragment :
    BaseBottomSheetFragment<FragmentListChatsBinding>(R.layout.fragment_list_chats),
    AdapterCallBack.OnClickItemListener<ChatsResponse> {
    override fun getViewBinding(view: View): FragmentListChatsBinding {
        return FragmentListChatsBinding.bind(view)
    }

    @Inject
    lateinit var listChatsAdapter: ListChatsAdapter
    private val listChatViewModel by viewModels<ListChatViewModel>()
    private var listChats = mutableListOf<ChatsResponse>()
    override fun initView() {
        with(binding) {
            listChatViewModel.handle(ListChatAction.GetListChats)
            listChatsAdapter.setOnClickItemListener(this@ListChatsFragment)
            rcy.adapter = listChatsAdapter
        }
    }

    override fun initObserve() {
        with(listChatViewModel) {

            val context = this@ListChatsFragment

            onFriendsOnline.observe(context) { rsId ->
                setStatusUser(true, rsId)
            }

            onFriendsOffline.observe(context) { rsId ->
                setStatusUser(false, rsId)
            }

            onReceiverNewMessage.observe(context) { newChatMessage ->
                val index = listChats.indexOfFirst { it.user._id == newChatMessage.senderId }
                if (index != -1) {
                    listChats[index].messages = listOf(newChatMessage)
                    listChats.removeAt(index)
                    listChats.add(0, listChats[index])
                    listChatsAdapter.notifyItemRemoved(index)
                    listChatsAdapter.notifyItemInserted(0)
                }
            }

            onGetListChats.observe(context) { result ->
                when (result) {
                    is NetworkResult.Success -> {
                        listChats.clear()
                        sortDescendingByTime()
                        listChats.addAll(result.data!!)
                        listChatsAdapter.setData(listChats)
                    }

                    is NetworkResult.Error -> {

                    }

                    is NetworkResult.Loading -> {

                    }
                }
            }
        }
    }

    private fun setStatusUser(status: Boolean, _id: String) {
        val index = listChats.indexOfFirst { it.user._id == _id }
        listChats[index].user.status = status
        listChatsAdapter.notifyItemChanged(index)
    }

    private fun sortDescendingByTime() {
        return listChats.sortByDescending { it.messages[it.messages.size - 1].sentTime }
    }

    override fun onClickItem(item: ChatsResponse) {

    }
}