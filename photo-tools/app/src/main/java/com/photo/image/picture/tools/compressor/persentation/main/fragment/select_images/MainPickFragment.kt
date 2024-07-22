package com.photo.image.picture.tools.compressor.persentation.main.fragment.select_images

import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.photo.image.picture.tools.compressor.R
import com.photo.image.picture.tools.compressor.base.BaseFragment
import com.photo.image.picture.tools.compressor.data.local.service.MyContentObserver
import com.photo.image.picture.tools.compressor.databinding.FragmentMainPickBinding
import com.photo.image.picture.tools.compressor.persentation.view.MyTabEvent
import com.photo.image.picture.tools.compressor.utils.clickIconNavigation
import com.yalantis.ucrop.UCrop
import dagger.hilt.android.AndroidEntryPoint
import java.io.File


@AndroidEntryPoint
class MainPickFragment : BaseFragment<FragmentMainPickBinding>(), MyTabEvent,
    MyContentObserver.ContentChangeListener {
    override fun provideViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentMainPickBinding.inflate(inflater, container, false)

    private val mainImageViewModel by activityViewModels<MainImageViewModel>()
    private val myContentObserver by lazy { MyContentObserver(requireContext().contentResolver) }
    override fun setupView(view: View, savedInstanceState: Bundle?) {
        myContentObserver.setListener(this)
        setUpDataImages()
        setUpViewPager()
        setUpToolbar()
        binding.myTab.setOnListenTabSelected(this)
    }
    private fun setUpToolbar() {
        binding.includeToolbar.mTitle = "Select Images"
        binding.includeToolbar.clickIconNavigation {
            requireActivity().finish()
        }
    }

    private fun setUpDataImages() {
        mainImageViewModel.getAllImages()
        mainImageViewModel.getFoldersWithImages()
    }

    private val callback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            binding.myTab.selected = position
        }
    }

    private fun setUpViewPager() {
        val pager = ImagePickerViewPager(requireActivity())
        binding.mViewPager2.adapter = pager
    }

    override fun onResume() {
        super.onResume()
        myContentObserver.register(MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        binding.mViewPager2.registerOnPageChangeCallback(callback)
    }

    override fun onPause() {
        super.onPause()
        myContentObserver.unregister()
        binding.mViewPager2.unregisterOnPageChangeCallback(callback)
    }

    override fun tabSelected(position: Int) {
        binding.mViewPager2.setCurrentItem(position, true)
    }

    override fun onContentChanged() {
        setUpDataImages()
    }
}