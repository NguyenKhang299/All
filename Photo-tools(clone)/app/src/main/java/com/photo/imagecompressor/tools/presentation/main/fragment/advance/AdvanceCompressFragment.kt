package com.photo.imagecompressor.tools.presentation.main.fragment.advance

import android.graphics.Color
import android.os.Bundle
import android.text.format.Formatter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.photo.imagecompressor.tools.base.BaseFragment
import com.photo.imagecompressor.tools.utils.clickIconNavigation
import com.jaygoo.widget.OnRangeChangedListener
import com.jaygoo.widget.RangeSeekBar
import com.photo.imagecompressor.tools.utils.Utils.Companion.startToMain
import com.photo.imagecompressor.tools.R
import com.photo.imagecompressor.tools.databinding.FragmentAdvanceCompressBinding
import com.photo.imagecompressor.tools.domain.model.Image_Type
import com.photo.imagecompressor.tools.presentation.main.BackDialogFragment
import com.photo.imagecompressor.tools.presentation.main.fragment.compressing.CompressingDialogFragment
import com.photo.imagecompressor.tools.presentation.main.fragment.select_images.MainImageViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AdvanceCompressFragment : BaseFragment<FragmentAdvanceCompressBinding>(),
    AdapterView.OnItemSelectedListener, OnRangeChangedListener {
    override fun provideViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentAdvanceCompressBinding.inflate(inflater, container, false)

    private val mainViewModel by activityViewModels<MainImageViewModel>()
    private val advanceViewModel by viewModels<AdvanceViewModel>()
    private val imageSelected by lazy { mainViewModel.getImagesSelected() }

    override
    fun setupView(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            lifecycleOwner = this@AdvanceCompressFragment
            advanceViewModel.imageSelected = imageSelected
            advanceCompressFragmentEvent = advanceViewModel
            quantity = 0
            selected = imageSelected.size
            totalSizeImage = Formatter.formatFileSize(
                requireContext(),
                imageSelected.sumOf {
                    it.size
                })
        }
        binding.seekBarQuantity.setProgress(50f)
        mainViewModel.getCompressionQuantity().quantityDefault = binding.seekBarQuantity.leftSeekBar.progress.toInt()
        binding.quantity = binding.seekBarQuantity.leftSeekBar.progress.toInt()
        mainViewModel.postFormatTo(Image_Type.AUTO)
        setUpToolbar()
        setAdapterSpinner()
        binding.seekBarQuantity.setRange(10f, 90f, 10f)
        binding.seekBarQuantity.setOnRangeChangedListener(this)

    }

    private fun setUpToolbar() {
        binding.title = getString(R.string.advance_compress)
        binding.includeToolbar.clickIconNavigation { BackDialogFragment.show(requireActivity()) }
        binding.fileSizeMedium.isChecked = true
    }

    private fun setAdapterSpinner() {
        val ad = ArrayAdapter(
            requireActivity(),
            android.R.layout.simple_spinner_item,
            advanceViewModel.listSize
        )
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerSize.adapter = ad
        binding.spinnerSize.onItemSelectedListener = this
    }

    override fun setupObservers() {
        advanceViewModel.formatOptionLiveData.observe(this) {
            mainViewModel.postFormatTo(it)
        }

        advanceViewModel.onClickCompress.observe(this) {
            if (it) findNavController().navigate(R.id.action_advanceCompress_to_compressingDialogFragment)
        }
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        mainViewModel.postCompressionQuantity(advanceViewModel.compressionQuantities[p2])
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }

    override fun onRangeChanged(
        view: RangeSeekBar?,
        leftValue: Float,
        rightValue: Float,
        isFromUser: Boolean
    ) {
        binding.quantity = leftValue.toInt()
        mainViewModel.getCompressionQuantity().quantityDefault = leftValue.toInt()
    }

    override fun onStartTrackingTouch(view: RangeSeekBar?, isLeft: Boolean) {

    }

    override fun onStopTrackingTouch(view: RangeSeekBar?, isLeft: Boolean) {

    }

}