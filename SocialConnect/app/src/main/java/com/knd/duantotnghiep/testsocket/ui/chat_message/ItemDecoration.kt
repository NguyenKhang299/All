package com.knd.duantotnghiep.testsocket.ui.chat_message

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class ItemDecoration(private val space: Int) :
    RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.bottom = space
        if (parent.getChildLayoutPosition(view) != 0) {
            outRect.top = space
        } else {
            outRect.top = 0
        }
    }
}