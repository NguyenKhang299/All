package com.knd.duantotnghiep.phonetrackerlocation.utils

import android.app.Activity
import android.content.Context
import androidx.fragment.app.FragmentActivity
import com.knd.duantotnghiep.phonetrackerlocation.ui.dialog.DialogLoading

object DialogUtils {
    private var dialogLoading: DialogLoading? = null
    private fun setUpDialogLoading() {
        dialogLoading = DialogLoading()

    }

    fun FragmentActivity.showDialogLoading() {
        if (dialogLoading == null) {
            setUpDialogLoading()
        }
        dialogLoading?.show(this.supportFragmentManager, dialogLoading!!::class.java.name)
    }

    fun dismissDialogLoading() {
        dialogLoading?.dismiss()
    }
}
