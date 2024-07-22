package com.photo.image.picture.tools.compressor.persentation.main.fragment.resize_compress

import android.os.Bundle
import android.text.format.Formatter
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.photo.image.picture.tools.compressor.R
import com.photo.image.picture.tools.compressor.base.BaseFragment
import com.photo.image.picture.tools.compressor.databinding.FragmentResizeBinding
import com.photo.image.picture.tools.compressor.databinding.FragmentResultBinding
import com.photo.image.picture.tools.compressor.persentation.main.fragment.convert.ConvertViewModel
import com.photo.image.picture.tools.compressor.persentation.main.fragment.select_images.MainImageViewModel
import com.photo.image.picture.tools.compressor.utils.Utils.Companion.startToMain
import com.photo.image.picture.tools.compressor.utils.clickIconNavigation

class ResizeFragment : BaseFragment<FragmentResizeBinding>(), AdapterView.OnItemSelectedListener {
    override fun provideViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentResizeBinding.inflate(inflater, container, false)

    private val mainViewModel by activityViewModels<MainImageViewModel>()
    private val resizeViewModel by viewModels<ResizeCompressViewModel>()
    private val imageSelected by lazy { mainViewModel.getImagesSelected() }

    override fun setupView(view: View, savedInstanceState: Bundle?) {
        binding.lifecycleOwner = this
        resizeViewModel.imageSelected = imageSelected
        binding.resizeCompressFragmentEvent = resizeViewModel
        binding.title = getString(R.string.resize_compress)
        binding.selected = mainViewModel.getImagesSelected().size
        binding.totalSizeImage = Formatter.formatFileSize(
            requireContext(),
            mainViewModel.getImagesSelected().sumOf { it.size })
        binding.includeToolbar.clickIconNavigation { requireActivity().startToMain() }
        setAdapterSpinner()
    }

    private fun setAdapterSpinner() {
        val ad = ArrayAdapter(
            requireActivity(),
            android.R.layout.simple_spinner_item,
            resizeViewModel.listSize
        )
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerSize.adapter = ad
        binding.spinnerSize.onItemSelectedListener = this
    }


    override fun setupObservers() {
        resizeViewModel.clickCompress.observe(this) {
            findNavController().navigate(R.id.action_resizeFragment_to_compressingDialogFragment)
        }

        resizeViewModel.clickSizeLiveData.observe(this) {
            mainViewModel.getCompressionQuantity().quantityDefault = it
        }

        resizeViewModel.compressOptionLiveData.observe(this) {
             mainViewModel.postCompressionQuantity(it)
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        mainViewModel.postCompressionQuantity(resizeViewModel.compressionQuantities[position])
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }
}