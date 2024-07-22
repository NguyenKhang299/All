package com.photo.image.picture.tools.compressor.persentation

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import com.photo.image.picture.tools.compressor.base.BaseBottomSheetFragment
import com.photo.image.picture.tools.compressor.databinding.FragmentDialogBackBinding
import com.photo.image.picture.tools.compressor.persentation.main.MainActivity
import com.photo.image.picture.tools.compressor.persentation.main.fragment.select_images.MainPickImageActivity
import com.photo.image.picture.tools.compressor.utils.Const

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
        (requireActivity() as MainPickImageActivity).showNativeAds(binding.nativeAds)
        binding.btnExit.setOnClickListener {
            requireActivity().finishAffinity()
        }
    }
}