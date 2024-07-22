package com.photo.imagecompressor.tools.presentation.main.fragment.intro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.photo.imagecompressor.tools.base.BaseFragment
import com.photo.imagecompressor.tools.databinding.FragmentIntroBinding

class IntroFragment : BaseFragment<FragmentIntroBinding>() {
    override fun provideViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentIntroBinding.inflate(inflater, container, false)

    override fun setupView(view: View, savedInstanceState: Bundle?) {
    }

}