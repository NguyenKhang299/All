package com.knd.duantotnghiep.testsocket.core

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.knd.duantotnghiep.testsocket.databinding.ItemFileSentBinding
import com.knd.duantotnghiep.testsocket.ui.chat_message.ViewTypeChatMessage

abstract class BaseAdapterMultiView<O : Any> :
    RecyclerView.Adapter<BaseAdapterMultiView<O>.RecyclerViewHolder>() {

    private var onClickListener: ((O) -> Unit)? = null
    private var onLongClickListener: ((O) -> Unit)? = null
    private var onTouchListener: ((O) -> Unit)? = null


    abstract fun getViewBinding(
        inflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): ViewBinding


    var submitData = ArrayList<O>()
        set(newList) {
            field.clear()
            field.addAll(newList)
            notifyDataSetChanged()
        }

    abstract fun onBind(binding: ViewBinding, item: O, viewType: Int)
    abstract fun getItemViewTypeCustom(position: Int): Int


    inner class RecyclerViewHolder(val binding: ViewBinding) : RecyclerView.ViewHolder(binding.root)
    private val viewType = hashMapOf<Int, ViewBinding>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = getViewBinding(inflater, parent, viewType)
        return RecyclerViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return  submitData.size
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val data = submitData[position]
        onBind(holder.binding, data, holder.itemViewType)

        holder.binding.root.setOnClickListener {
            onClickListener?.invoke(data)
        }

        holder.binding.root.setOnLongClickListener {
            onLongClickListener?.invoke(data)
            true
        }
        holder.binding.root.setOnTouchListener { view, motionEvent ->
            onTouchListener?.invoke(data)
            true
        }
    }

    fun setOnTouchListener(onClick: (O) -> Unit) {
        onTouchListener = onClick
    }
    fun setOnClickListener(onClick: (O) -> Unit) {
        onClickListener = onClick
    }

    fun setOnLongClickListener(onClick: (O) -> Unit) {
        onLongClickListener = onClick
    }

    override fun getItemViewType(position: Int): Int {
        return getItemViewTypeCustom(position)
    }
}
