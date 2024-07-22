package com.pranksound.fartsound.trollandjoke.funnyapp.contract

import com.android.billingclient.api.ProductDetails

interface SubVipInterface {
    interface View {
        fun setBackGroundDefault(draw: Int)
        fun setTextColorDefault(color: Int)
        fun setBackGroundClick(draw: Int, position: Int)
        fun setTextColorClick(color: Int, position: Int)
        fun handelClick(position: Int)
    }

    interface Presenter {

    }
}