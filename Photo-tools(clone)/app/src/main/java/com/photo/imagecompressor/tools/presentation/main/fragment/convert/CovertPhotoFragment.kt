package com.photo.imagecompressor.tools.persentation.main.fragment.convert

import android.os.Bundle
import android.text.format.Formatter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.photo.imagecompressor.tools.base.BaseFragment
import com.photo.imagecompressor.tools.utils.Utils.Companion.startToMain
import com.photo.imagecompressor.tools.utils.clickIconNavigation
import com.photo.imagecompressor.tools.R
import com.photo.imagecompressor.tools.databinding.FragmentCovertPhotoBinding
import com.photo.imagecompressor.tools.presentation.main.BackDialogFragment
import com.photo.imagecompressor.tools.presentation.main.fragment.convert.ConvertViewModel
import com.photo.imagecompressor.tools.presentation.main.fragment.select_images.MainImageViewModel


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
            BackDialogFragment.show(requireActivity())
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
                if (it) findNavController().navigate(R.id.action_covertPhotoFragment_to_compressingDialogFragment)
            }
        }
    }
}