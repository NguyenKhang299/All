package com.photo.imagecompressor.tools.presentation.main.fragment.select_images

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.photo.imagecompressor.tools.base.BaseFragment
import com.photo.imagecompressor.tools.databinding.FragmentImageFolderBinding
import com.photo.imagecompressor.tools.utils.clickIconNavigation
import com.photo.imagecompressor.tools.utils.popBackStack
import dagger.hilt.android.AndroidEntryPoint
import java.util.ArrayList

@AndroidEntryPoint
class ImageFolderFragment : BaseFragment<FragmentImageFolderBinding>() {
    override fun provideViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentImageFolderBinding.inflate(inflater, container, false)

    private val mainImageViewModel by activityViewModels<MainImageViewModel>()
    private val imageAdapter by lazy { ImageAdapter(mainImageViewModel, this) }
    private val args: ImageFolderFragmentArgs by navArgs()

    override fun setupView(view: View, savedInstanceState: Bundle?) {
        mainImageViewModel.getImageByFolder(args.idBucket)
        binding.apply {
            lifecycleOwner = this@ImageFolderFragment
            title = args.nameBucket
            recyclerView.adapter = imageAdapter

            includeToolbar.clickIconNavigation {
                popBackStack()
            }
        }
    }

    override fun setupObservers() {
        mainImageViewModel.imagesByIdFolderLiveData.observe(requireActivity()) {
            if (it.isEmpty()) {
                binding.lottieAnimationView.visibility=if (it.isEmpty()) View.VISIBLE else View.GONE
            }
            imageAdapter.submitData = ArrayList(it)
        }
    }
}