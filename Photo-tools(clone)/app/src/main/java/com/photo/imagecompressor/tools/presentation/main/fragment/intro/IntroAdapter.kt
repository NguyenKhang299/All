package com.photo.imagecompressor.tools.presentation.main.fragment.intro

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class IntroAdapter(private val fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> IntroFragment()
            1 -> Intro2Fragment()
            2 -> Intro3Fragment()
            else -> IntroFragment()
        }
    }
}