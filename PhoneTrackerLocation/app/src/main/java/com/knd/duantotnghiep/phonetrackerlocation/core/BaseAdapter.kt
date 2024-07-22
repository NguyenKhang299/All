package com.knd.duantotnghiep.phonetrackerlocation.core

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.knd.duantotnghiep.phonetrackerlocation.utils.AdapterCallBack
import com.knd.duantotnghiep.phonetrackerlocation.utils.UserPreferencesManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

abstract class BaseAdapter<VB : ViewBinding, T> constructor() :
    RecyclerView.Adapter<BaseAdapter<VB, T>.ViewHolder>() {
     lateinit var users: UserPreferencesManager
    open fun isCurrentUserSender(senderUserId: String) =
        senderUserId == users.getCurrentUser()!!._id

    inner class ViewHolder(val binding: VB) : RecyclerView.ViewHolder(binding.root)

    private var onClickItemListener: AdapterCallBack.OnClickItemListener<T>? = null

    fun setOnClickItemListener(onClickItemListener: AdapterCallBack.OnClickItemListener<T>) {
        this.onClickItemListener = onClickItemListener
    }

    protected lateinit var binding: VB
    protected val listData: MutableList<T> = mutableListOf()
    protected abstract fun getItemBinding(parent: ViewGroup): VB
    protected abstract fun bind(data: T, binding: VB)

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: MutableList<T>) {
        listData.clear()
        listData.addAll(data)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = getItemBinding(parent)
        users = UserPreferencesManager(parent.context)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.root.setOnClickListener { onClickItemListener?.onClickItem(listData[position]) }
        bind(listData[position], holder.binding)
    }


}