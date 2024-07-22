package com.photo.imagecompressor.tools.presentation.main

import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.photo.imagecompressor.tools.R
import com.photo.imagecompressor.tools.base.BaseActivity
import com.photo.imagecompressor.tools.databinding.ActivityMainBinding
import com.photo.imagecompressor.tools.presentation.DialogBackFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

    override fun provideViewBinding() = ActivityMainBinding.inflate(layoutInflater)

    override fun setupView(savedInstanceState: Bundle?) {

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

         binding.bottomNavigationView.setupWithNavController(navController)
    }

    override fun onBackPressed() {
        DialogBackFragment().show(supportFragmentManager, DialogBackFragment::class.simpleName)

    }
}