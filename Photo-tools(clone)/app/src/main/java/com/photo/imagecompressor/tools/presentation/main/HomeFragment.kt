package com.photo.imagecompressor.tools.presentation.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.photo.imagecompressor.tools.R
import com.photo.imagecompressor.tools.action.ActionImage
import com.photo.imagecompressor.tools.base.BaseFragment
import com.photo.imagecompressor.tools.databinding.FragmentHomeBinding
import com.photo.imagecompressor.tools.presentation.main.fragment.intro.IntroAdapter
import com.photo.imagecompressor.tools.presentation.main.fragment.select_images.MainPickImageActivity
import com.photo.imagecompressor.tools.utils.Const
import com.photo.imagecompressor.tools.utils.LanguageConfig
import com.photo.imagecompressor.tools.utils.LanguageEnum
import com.photo.imagecompressor.tools.utils.PermUtils
import com.photo.imagecompressor.tools.utils.passActionImage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.math.abs

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(), MainViewModel.HomeFragmentEvent {
    override fun provideViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentHomeBinding.inflate(inflater, container, false)

    private lateinit var actionImage: ActionImage

    private val reqPerm =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            if (it.isNotEmpty() && it.values.all { it }) {
                startPicker(actionImage)
            } else {
                showMess(getString(R.string.perm_denied))
            }
        }

    private fun startPicker(actionImage: ActionImage) {
        startActivity(Intent(requireContext(), MainPickImageActivity::class.java).apply {
            passActionImage(actionImage)
        })
    }

    @Inject
    lateinit var languageConfig: LanguageConfig
    override fun setupView(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            lifecycleOwner = this@HomeFragment
            event = this@HomeFragment
            setUpAdapterIntro()
            circleIndicator3.setViewPager(rcyIntro)
            binding.mMenu.setOnClickListener { showPopupDialog() }
        }
    }

    private fun setUpAdapterIntro() {
        binding.apply {
            val introAdapter = IntroAdapter(this@HomeFragment)
            rcyIntro.offscreenPageLimit = 3
            rcyIntro.clipChildren = false
            rcyIntro.clipToPadding = false
            rcyIntro.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            val transformer = CompositePageTransformer()
            transformer.addTransformer(MarginPageTransformer(15))
            transformer.addTransformer { page, position ->
                val r = 1 - abs(position)
                page.scaleY = 0.85f + r * 0.14f
            }
            rcyIntro.setPageTransformer(transformer)
            rcyIntro.adapter = introAdapter
        }
    }

    private fun showPopupDialog() {
        val popup = PopupMenu(requireContext(), binding.mMenu)
        popup.inflate(R.menu.popup_menu_main)

        val menu: Menu = popup.menu
        if (languageConfig.getLocal() == LanguageEnum.ENGLISH ) {
            menu.findItem(R.id.en).isChecked = true
        }
        if (languageConfig.getLocal() == LanguageEnum.VIETNAM ) {
            menu.findItem(R.id.vn).isChecked = true
        }
        popup.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.share -> shareApp()
                R.id.vn -> {
                    it.isChecked = true
                    languageConfig.setLocal(LanguageEnum.VIETNAM)
                }

                R.id.en -> {
                    it.isChecked = true
                    languageConfig.setLocal(LanguageEnum.ENGLISH)
                }
            }

            true
        }
        popup.show()
    }

    private fun shareApp() {
        val textToShare = "Please download and experience our app ${Const.linkApp}"

        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, textToShare)
            type = "text/plain"
        }
        startActivity(Intent.createChooser(shareIntent, "Share:"))
    }

    override fun clickActionCompress(actionImage: ActionImage) {
        this.actionImage = actionImage
        if (!PermUtils.checkImageAccessPermissions(requireContext())) {
            val permissions = PermUtils.getImageAccessPermissions()
            reqPerm.launch(permissions)
        } else {
            startPicker(actionImage)
        }
    }
}