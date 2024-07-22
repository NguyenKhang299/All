package com.knd.duantotnghiep.phonetrackerlocation.ui.account.intro

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.knd.duantotnghiep.phonetrackerlocation.R

class IntroAdapter(fm: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fm, lifecycle) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ItemIntroFragment(R.layout.frag_intro_1)
            1 -> ItemIntroFragment(R.layout.frag_intro_2)
            else -> ItemIntroFragment(R.layout.frag_intro_3)
        }
    }
}