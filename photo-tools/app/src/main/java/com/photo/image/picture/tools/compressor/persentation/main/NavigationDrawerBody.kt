package com.photo.image.picture.tools.compressor.persentation.main

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import com.photo.image.picture.tools.compressor.R
import com.photo.image.picture.tools.compressor.base.BaseFragment
import com.photo.image.picture.tools.compressor.databinding.BodyNavigationBinding
import com.google.android.material.button.MaterialButton
import kotlin.reflect.KClass

interface OnCLickItemListener {
    fun onClickItem(id: Int)
}

class NavigationDrawerBody : BaseFragment<BodyNavigationBinding>(), OnClickListener {
    override fun provideViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = BodyNavigationBinding.inflate(inflater, container, false)

    private val mainViewModel: MainViewModel by activityViewModels<MainViewModel>()

    private var onCLickItemListener: OnCLickItemListener? = null
    var checked: Int = -1
    var checkedBefore: Int = -1

    override fun setupView(view: View, savedInstanceState: Bundle?) {
        binding.home.setOnClickListener(this)
        binding.compressPhoto.setOnClickListener(this)
        binding.changeLanguage.setOnClickListener(this)
        binding.support.setOnClickListener(this)
        binding.rateUp.setOnClickListener(this)
        binding.shareApp.setOnClickListener(this)
        binding.policy.setOnClickListener(this)
        binding.termsConditions.setOnClickListener(this)
        setOnChecked(R.id.home)
    }


    private fun MaterialButton.updateButtonStyle(isCheckedBefore: Boolean = false) {
        val colorBg = if (!isCheckedBefore) {
            requireActivity().getColor(R.color.bg_item_nav_click)
        } else {
            Color.WHITE
        }
        backgroundTintList = ColorStateList.valueOf(colorBg)
    }

    fun setOnChecked(id: Int) {
        if (id != checkedBefore) {
            with(binding.root) {
                findViewById<MaterialButton>(id)?.updateButtonStyle(false)
                if (checkedBefore != -1) {
                    findViewById<MaterialButton>(checkedBefore)?.updateButtonStyle(true)
                }
            }
            checkedBefore = id
        }

    }

    fun setOnClickItemListener(onCLickItemListener: OnCLickItemListener) {
        this.onCLickItemListener = onCLickItemListener
    }

    override fun onClick(v: View?) {
        checked = v!!.id
        onCLickItemListener?.onClickItem(v.id)
    }


}