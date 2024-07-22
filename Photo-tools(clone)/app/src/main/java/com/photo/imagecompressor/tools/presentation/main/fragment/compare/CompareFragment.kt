package com.photo.imagecompressor.tools.presentation.main.fragment.compare

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.photo.imagecompressor.tools.base.BaseFragment
import com.photo.imagecompressor.tools.utils.Utils.Companion.fileToUri
import com.photo.imagecompressor.tools.utils.Utils.Companion.shareMultiple
import com.photo.imagecompressor.tools.utils.clickIconNavigation
import com.photo.imagecompressor.tools.utils.popBackStack
import com.photo.imagecompressor.tools.utils.setColorItemMenu
import com.photo.imagecompressor.tools.utils.setMenuToolbar
import com.photo.imagecompressor.tools.R
import com.photo.imagecompressor.tools.databinding.FragmentCompareBinding
import com.photo.imagecompressor.tools.presentation.main.fragment.select_images.MainImageViewModel
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
    }

    @SuppressLint("IntentReset")
    private fun setUpToolBar() {
        binding.title = getString(R.string.image_compressed)
        binding.includeToolbar.setMenuToolbar(R.menu.menu_share) {
            val file = File(imageSelected.path)
            when (it.itemId) {
                R.id.share -> {
                    val listUri = mainViewModel.imagesAfter.map {
                        requireContext().fileToUri(File(it.path))
                    }.toList()
                    requireContext().shareMultiple(listUri)
                    true
                }
            }
            false
        }
        binding.includeToolbar.clickIconNavigation { popBackStack() }
//        binding.includeToolbar.setColorItemMenu(R.id.share,Color.WHITE)
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