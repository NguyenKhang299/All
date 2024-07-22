package com.photo.imagecompressor.tools.presentation.main.fragment.select_images

import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.photo.imagecompressor.tools.R
import com.photo.imagecompressor.tools.base.BaseFragment
import com.photo.imagecompressor.tools.persentation.view.MyTabEvent
import com.photo.imagecompressor.tools.utils.clickIconNavigation
import com.photo.imagecompressor.tools.data.local.service.ContentImageObserver
import com.photo.imagecompressor.tools.databinding.FragmentMainPickBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainPickFragment : BaseFragment<FragmentMainPickBinding>(), MyTabEvent,
  ContentImageObserver.ContentChangeListener {
    override fun provideViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentMainPickBinding.inflate(inflater, container, false)

    private val mainImageViewModel by activityViewModels<MainImageViewModel>()
    private val myContentObserver by lazy { ContentImageObserver(requireContext().contentResolver) }
    override fun setupView(view: View, savedInstanceState: Bundle?) {
        myContentObserver.setListener(this)
        setUpDataImages()
        setUpViewPager()
        setUpToolbar()
        binding.myTab.setOnListenTabSelected(this)
    }
    private fun setUpToolbar() {
        binding.includeToolbar.mTitle =getString(R.string.selecte_images)
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