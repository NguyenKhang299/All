package com.photo.image.picture.tools.compressor.persentation.main.fragment.select_images

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ImagePickerViewPager(activity: FragmentActivity) :
    FragmentStateAdapter(activity) {
    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        if (position == 0) return ShowImageFragment()
        return ShowFolderFragment()
    }
}