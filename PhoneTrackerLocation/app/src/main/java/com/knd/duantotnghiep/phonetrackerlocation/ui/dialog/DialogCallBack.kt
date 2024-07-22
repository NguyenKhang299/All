package com.knd.duantotnghiep.phonetrackerlocation.ui.dialog

import android.content.DialogInterface

interface DialogCallBack {
    interface OnDismissListener {
        fun onDismiss(var1: DialogInterface?)

    }
    interface OnResultListener <T> {
        fun onResult(rs: T)

    }
    interface OnSearchListener {
        fun onSearchSuccess(var1: String)
    }
}