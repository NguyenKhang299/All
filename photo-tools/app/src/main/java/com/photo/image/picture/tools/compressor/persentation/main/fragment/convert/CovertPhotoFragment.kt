package com.photo.image.picture.tools.compressor.persentation.main.fragment.convert

import android.os.Bundle
import android.text.format.Formatter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.photo.image.picture.tools.compressor.R
import com.photo.image.picture.tools.compressor.base.BaseFragment
import com.photo.image.picture.tools.compressor.databinding.FragmentCovertPhotoBinding
import com.photo.image.picture.tools.compressor.persentation.main.fragment.compressing.CompressingDialogFragment
import com.photo.image.picture.tools.compressor.persentation.main.fragment.select_images.MainImageViewModel
import com.photo.image.picture.tools.compressor.utils.Utils.Companion.startToMain
import com.photo.image.picture.tools.compressor.utils.clickIconNavigation


class CovertPhotoFragment : BaseFragment<FragmentCovertPhotoBinding>() {
    override fun provideViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentCovertPhotoBinding.inflate(inflater, container, false)

    private val mainViewModel by activityViewModels<MainImageViewModel>()
    private val convertViewModel by viewModels<ConvertViewModel>()

    override fun setupView(view: View, savedInstanceState: Bundle?) {
        binding.lifecycleOwner = this
        binding.convertFragmentEvent = convertViewModel
        binding.selected = mainViewModel.getImagesSelected().count()

        binding.totalSizeImage = Formatter.formatFileSize(
            requireContext(),
            mainViewModel.getImagesSelected().sumOf { it.size })

        setUpToolbar()
    }

    private fun setUpToolbar() {
        binding.title = getString(R.string.convert_photo_compress)
        binding.includeToolbar.clickIconNavigation {
            requireActivity().startToMain()
        }
    }

    override fun setupObservers() {
        convertViewModel.apply {
            formatOptionLiveData.observe(this@CovertPhotoFragment) {
                mainViewModel.postFormatTo(it)
            }

            compressOptionLiveData.observe(this@CovertPhotoFragment) {
                mainViewModel.postCompressionQuantity(it)
            }

            onClickCompress.observe(this@CovertPhotoFragment) {
                findNavController().navigate(R.id.action_covertPhotoFragment_to_compressingDialogFragment)
            }
        }
    }
}