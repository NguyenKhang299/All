package com.photo.image.picture.tools.compressor.persentation.main.fragment.compress_images

import android.os.Bundle
import android.text.format.Formatter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.photo.image.picture.tools.compressor.R
import com.photo.image.picture.tools.compressor.action.ActionImage

import com.photo.image.picture.tools.compressor.databinding.FragmentSelectCompressionBinding

import com.photo.image.picture.tools.compressor.action.CompressionQuantity
import com.photo.image.picture.tools.compressor.base.BaseFragment
import com.photo.image.picture.tools.compressor.domain.model.Resolution
import com.photo.image.picture.tools.compressor.persentation.main.fragment.select_images.MainImageViewModel
import com.photo.image.picture.tools.compressor.utils.Utils.Companion.getResolutionBySize
import com.photo.image.picture.tools.compressor.utils.Utils.Companion.startToMain
import com.photo.image.picture.tools.compressor.utils.clickIconNavigation
import dagger.hilt.android.AndroidEntryPoint

interface SelectCompressionFragmentEvent {
    fun onClickCompress()
    fun onClickMediumSizeOption()
    fun onClickLagerSizeOption()
    fun onClickSmailSizeOption()
    fun onClickCustomSizeOption()
}

@AndroidEntryPoint
class SelectCompressionFragment : BaseFragment<FragmentSelectCompressionBinding>(),
    SelectCompressionFragmentEvent, ConfirmedFileSizeListener {
    override fun provideViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentSelectCompressionBinding.inflate(inflater, container, false)

    private val activityViewModel by activityViewModels<MainImageViewModel>()
    private val imageSelectedAdapter by lazy { ImageSelectedAdapter() }
    private val imagesSelected by lazy { activityViewModel.getImagesSelected() }

    override fun setupView(view: View, savedInstanceState: Bundle?) {
        binding.lifecycleOwner = this
        binding.selectCompressionFragmentEvent = this
        binding.optionSelected = CompressionQuantity.MediumSize()
        binding.smailSize = CompressionQuantity.SmallSize()
        binding.mediumSize = CompressionQuantity.MediumSize()
        binding.lagerSize = CompressionQuantity.BestQuality()
        binding.customFile = CompressionQuantity.Custom()
        binding.optionSelected = CompressionQuantity.MediumSize()
        binding.selected = imagesSelected.size
        if (activityViewModel.getActionImage() == ActionImage.CROP_COMPRESS) {
            binding.customFileSize.visibility = View.GONE
            setResolution(
                Resolution(
                    imagesSelected[0].bitmap!!.width,
                    imagesSelected[0].bitmap!!.height
                )
            )
        } else {
            setResolution(imagesSelected[0].resolution)
        }
        setUpImageSelectedAdapter()
        setUpToolbar()
    }

    private fun setUpToolbar() {
        binding.includeToolbar.mTitle = "Select Compression"
        binding.includeToolbar.clickIconNavigation {
            requireActivity().startToMain()
        }
    }

    private fun setUpImageSelectedAdapter() {
        binding.apply {
            val snapHelper: SnapHelper = PagerSnapHelper()
            snapHelper.attachToRecyclerView(rcyImage)
            rcyImage.setHasFixedSize(true)
            imageSelectedAdapter.submitData = ArrayList(imagesSelected)

            rcyImage.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {

                }

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    val linearLayoutMng = rcyImage.layoutManager as LinearLayoutManager
                    val firstVisibleItemPosition = linearLayoutMng.findFirstVisibleItemPosition()
                    if (firstVisibleItemPosition != -1) {
                        val image = imagesSelected[firstVisibleItemPosition]
                        resolution = image.resolution.toString()
                        sizeImage =
                            if (activityViewModel.getActionImage() == ActionImage.CROP_COMPRESS) {
                                setResolution(
                                    Resolution(
                                        imagesSelected[0].bitmap!!.width,
                                        imagesSelected[0].bitmap!!.height
                                    )
                                )
                                Formatter.formatFileSize(
                                    context,
                                    imagesSelected[0].bitmap!!.byteCount.toLong()
                                )
                            } else {
                                setResolution(image.resolution)
                                Formatter.formatFileSize(context, image.size)
                            }
                    }
                }
            })
            rcyImage.adapter = imageSelectedAdapter
        }
    }

    fun setResolution(resolution: Resolution) {
        binding.apply {
            binding.resolution = resolution.toString()
            resolutionSmail = "(" +
                    resolution.getResolutionBySize(CompressionQuantity.SmallSize()).toString() +
                    ")"
            resolutionLarge = "(" +
                    resolution.getResolutionBySize(CompressionQuantity.BestQuality()).toString() +
                    ")"
            resolutionMedium = "(" +
                    resolution.getResolutionBySize(CompressionQuantity.MediumSize()).toString() +
                    ")"
        }
    }

    override fun onClickCompress() {
        findNavController().navigate(R.id.action_selectCompression_to_compressingDialogFragment)
    }

    override fun onClickMediumSizeOption() {
        activityViewModel.postCompressionQuantity(CompressionQuantity.MediumSize())
        binding.optionSelected = CompressionQuantity.MediumSize()
    }

    override fun onClickLagerSizeOption() {
        activityViewModel.postCompressionQuantity(CompressionQuantity.BestQuality())
        binding.optionSelected = CompressionQuantity.BestQuality()
    }

    override fun onClickSmailSizeOption() {
        activityViewModel.postCompressionQuantity(CompressionQuantity.SmallSize())
        binding.optionSelected = CompressionQuantity.SmallSize()
    }

    override fun onClickCustomSizeOption() {
        CustomFileSizeDialogFragment(this).show(
            parentFragmentManager,
            CustomFileSizeDialogFragment::class.simpleName
        )
    }

    override fun onConfirmed(size: Long, unit: String) {
        activityViewModel.postCompressionQuantity(CompressionQuantity.Custom(if (unit == "MB") 1024 * size else size * 1024))
        binding.optionSelected = CompressionQuantity.Custom()
    }
}