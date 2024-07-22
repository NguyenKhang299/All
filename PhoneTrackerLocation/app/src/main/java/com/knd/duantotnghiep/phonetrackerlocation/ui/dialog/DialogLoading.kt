package com.knd.duantotnghiep.phonetrackerlocation.ui.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.WindowManager.LayoutParams
import com.knd.duantotnghiep.phonetrackerlocation.R
import com.knd.duantotnghiep.phonetrackerlocation.core.BaseDialogFragment
import com.knd.duantotnghiep.phonetrackerlocation.databinding.FragmentDialogLoadingBinding

class DialogLoading :
    BaseDialogFragment<FragmentDialogLoadingBinding>(R.layout.fragment_dialog_loading) {
private lateinit var view: View
    override fun getViewBinding(view: View): FragmentDialogLoadingBinding {
        this.view=view
        return FragmentDialogLoadingBinding.bind(view)
    }

    override fun initView() {
         dialog!!.window!!.setLayout(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT)
    }
}