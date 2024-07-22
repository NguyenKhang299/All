package com.photo.imagecompressor.tools.presentation.main.fragment.select_images

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.photo.imagecompressor.tools.base.BaseFragment
 import com.photo.imagecompressor.tools.utils.startToImageFolder
import com.photo.imagecompressor.tools.databinding.FragmentShowFolderBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShowFolderFragment : BaseFragment<FragmentShowFolderBinding>() {
    override fun provideViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentShowFolderBinding.inflate(inflater, container, false)

    private val folderImageAdapter by lazy { FolderImageAdapter() }
    private val mainImageViewModel by activityViewModels<MainImageViewModel>()

    override fun setupView(view: View, savedInstanceState: Bundle?) {
        binding.mRcy.adapter = folderImageAdapter
        folderImageAdapter.setOnClickListener { folderImage, i ->
            startToImageFolder(folderImage.id,folderImage.name)
        }
    }

    override fun setupObservers() {
        mainImageViewModel.folderWithImagesLiveData.observe(this) {
            if (it.isEmpty()) {
                binding.mLayoutNoFiles.visibility=if (it.isEmpty()) View.VISIBLE else View.GONE
            }
            folderImageAdapter.submitData = ArrayList(it)
        }
    }
}