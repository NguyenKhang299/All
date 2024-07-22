package com.photo.imagecompressor.tools.presentation.main

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import com.photo.imagecompressor.tools.base.BaseDialogFragment
import com.photo.imagecompressor.tools.databinding.FragmentBackDialofBinding
import com.photo.imagecompressor.tools.utils.Utils.Companion.startToMain


class BackDialogFragment : BaseDialogFragment<FragmentBackDialofBinding>() {
    override fun provideViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentBackDialofBinding.inflate(inflater, container, false)

    override fun setupView(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            btnExit.setOnClickListener { requireActivity().startToMain() }
            btnCancel.setOnClickListener { dismiss() }
        }
    }

    companion object {
        fun show(context: FragmentActivity) =
            BackDialogFragment().show(context.supportFragmentManager, "BackDialogFragment")
    }
}