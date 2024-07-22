package com.photo.image.picture.tools.compressor.persentation.main.fragment.compare

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.photo.image.picture.tools.compressor.R
import com.photo.image.picture.tools.compressor.base.BaseFragment
import com.photo.image.picture.tools.compressor.databinding.FragmentCompareBinding
import com.photo.image.picture.tools.compressor.databinding.FragmentCompressingBinding
import com.photo.image.picture.tools.compressor.databinding.FragmentResizeBinding
import com.photo.image.picture.tools.compressor.persentation.main.fragment.select_images.MainImageViewModel
import com.photo.image.picture.tools.compressor.persentation.main.fragment.select_images.MainPickImageActivity
import com.photo.image.picture.tools.compressor.utils.Utils
import com.photo.image.picture.tools.compressor.utils.Utils.Companion.fileToUri
import com.photo.image.picture.tools.compressor.utils.Utils.Companion.shareMultiple
import com.photo.image.picture.tools.compressor.utils.clickIconNavigation
import com.photo.image.picture.tools.compressor.utils.popBackStack
import com.photo.image.picture.tools.compressor.utils.setColorItemMenu
import com.photo.image.picture.tools.compressor.utils.setMenuToolbar
import java.io.File


class CompareFragment : BaseFragment<FragmentCompareBinding>() {
    override fun provideViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentCompareBinding.inflate(inflater, container, false)

    private val mainViewModel by activityViewModels<MainImageViewModel>()
    private val compressedAdapter by lazy { CompressedAdapter(requireContext()) }
    private val imageSelected by lazy { mainViewModel.getImagesSelected()[0] }
    override fun setupView(view: View, savedInstanceState: Bundle?) {
        setUpToolBar()
        setUpAdapter()
        (requireActivity() as MainPickImageActivity).hideBanner(false)
    }

    @SuppressLint("IntentReset")
    private fun setUpToolBar() {
        binding.title = "Compressed Images"
        binding.includeToolbar.setMenuToolbar(R.menu.menu_share) {
            val file = File(imageSelected.path)
            when (it.itemId) {
                R.id.share -> {
                    requireContext().shareMultiple(listOf(requireContext().fileToUri(file)))
                    true
                }
            }
            false
        }
        binding.includeToolbar.clickIconNavigation { popBackStack() }
        binding.includeToolbar.setColorItemMenu(R.id.share,Color.WHITE)
    }

    private fun setUpAdapter() {
        val snapHelper: SnapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(binding.rcyRs)
        binding.rcyRs.apply {
            setHasFixedSize(true)
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    val linearLayoutMng = layoutManager as LinearLayoutManager
                    val posistion = linearLayoutMng.findFirstVisibleItemPosition()
                    imageSelected == mainViewModel.getImagesSelected()[posistion]
                }
            })
        }
        compressedAdapter.submitData = mainViewModel.imagesAfter
        binding.rcyRs.adapter = compressedAdapter

    }
}