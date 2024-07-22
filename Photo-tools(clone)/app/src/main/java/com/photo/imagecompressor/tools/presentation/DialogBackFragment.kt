package com.photo.imagecompressor.tools.presentation

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import com.photo.imagecompressor.tools.base.BaseBottomSheetFragment
import com.photo.imagecompressor.tools.databinding.FragmentDialogBackBinding
import com.photo.imagecompressor.tools.utils.Const

interface DialogBackFragmentEvent {
    fun onClickStar(position: Int)
}

class DialogBackFragment : BaseBottomSheetFragment<FragmentDialogBackBinding>(),
    DialogBackFragmentEvent {

    override fun onClickStar(position: Int) {
        binding.positionSelected = position
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(Const.linkApp))
        startActivity(intent)
        requireActivity().finishAffinity()
    }

    override fun getViewBinding(view: LayoutInflater, container: ViewGroup?) =
        FragmentDialogBackBinding.inflate(layoutInflater, container, false)

    override fun initView() {
        binding.lifecycleOwner = this
        binding.dialogBackFragmentEvent = this
        binding.btnExit.setOnClickListener {
            requireActivity().finishAffinity()
        }
    }
}