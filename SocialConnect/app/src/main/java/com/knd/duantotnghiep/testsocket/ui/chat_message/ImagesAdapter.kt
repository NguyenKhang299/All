package com.knd.duantotnghiep.testsocket.ui.chat_message

import android.view.LayoutInflater
import android.view.ViewGroup
import com.knd.duantotnghiep.testsocket.core.BaseAdapter
import com.knd.duantotnghiep.testsocket.databinding.ItemImageBinding
import com.knd.duantotnghiep.testsocket.response.UploadResponse

class ImagesAdapter( ) : BaseAdapter<ItemImageBinding, UploadResponse>() {
    override fun getViewBinding(inflater: LayoutInflater, parent: ViewGroup): ItemImageBinding {
        return ItemImageBinding.inflate(inflater, parent, false)
    }

    override fun onBind(binding: ItemImageBinding, item: UploadResponse) {
        binding.urlImage = item.filePath

    }
}