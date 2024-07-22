package com.knd.duantotnghiep.phonetrackerlocation.utils;

interface AdapterCallBack {
    interface OnClickItemListener<T>{
        fun onClickItem(item: T)
    }
}
