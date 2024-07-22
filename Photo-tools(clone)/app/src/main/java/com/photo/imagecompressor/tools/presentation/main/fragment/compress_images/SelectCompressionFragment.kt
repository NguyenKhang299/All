package com.photo.imagecompressor.tools.presentation.main.fragment.compress_images

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
import com.photo.imagecompressor.tools.R
import com.photo.imagecompressor.tools.base.BaseFragment
import com.photo.imagecompressor.tools.utils.Utils.Companion.getResolutionBySize
import com.photo.imagecompressor.tools.utils.Utils.Companion.startToMain
import com.photo.imagecompressor.tools.utils.clickIconNavigation
import com.photo.imagecompressor.tools.action.ActionImage
import com.photo.imagecompressor.tools.action.CompressionQuantity
import com.photo.imagecompressor.tools.databinding.FragmentSelectCompressionBinding
import com.photo.imagecompressor.tools.domain.model.Resolution_
import com.photo.imagecompressor.tools.presentation.main.BackDialogFragment
import com.photo.imagecompressor.tools.presentation.main.fragment.crop_compress.CropImageFragment
import com.photo.imagecompressor.tools.presentation.main.fragment.select_images.MainImageViewModel
import dagger.hilt.android.AndroidEntryPoint

interface SelectCompressionFragmentEvent {
    fun onClickCompress()
    fun onClickMediumSizeOption()
    fun onClickLagerSizeOption()
    fun onClickSmailSizeOption()
    fun onClickVerySmailSizeOption()
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
        binding.verySmallSize = CompressionQuantity.VerySmallSize()
        binding.smailSize = CompressionQuantity.SmallSize()
        binding.mediumSize = CompressionQuantity.MediumSize()
        binding.lagerSize = CompressionQuantity.LagerQuality()
        binding.optionSelected = CompressionQuantity.VerySmallSize()
        activityViewModel.postCompressionQuantity(CompressionQuantity.VerySmallSize())

        binding.selected = imagesSelected.size
        if (activityViewModel.getActionImage() == ActionImage.CROP_COMPRESS) {
            setResolution(
                Resolution_(
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
        binding.includeToolbar.mTitle = getString(R.string.select_compression)
        binding.includeToolbar.clickIconNavigation {
            BackDialogFragment.show(requireActivity())
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
                                    Resolution_(
                                        imagesSelected[0].bitmap!!.width,
                                        imagesSelected[0].bitmap!!.height
                                    )
                                )
                                Formatter.formatFileSize(
                                    context,
                                    CropImageFragment.FILE_SIZE
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

    fun setResolution(resolution: Resolution_) {
        binding.apply {
            binding.resolution = resolution.toString()
            resolutionVerySmail = "(" +
                    resolution.getResolutionBySize(CompressionQuantity.VerySmallSize()).toString() +
                    ")"
            resolutionSmail = "(" +
                    resolution.getResolutionBySize(CompressionQuantity.SmallSize()).toString() +
                    ")"
            resolutionLarge = "(" +
                    resolution.getResolutionBySize(CompressionQuantity.LagerQuality()).toString() +
                    ")"
            resolutionMedium = "(" +
                    resolution.getResolutionBySize(CompressionQuantity.MediumSize()).toString() +
                    ")"
        }
    }

    var i = 0
    override fun onClickCompress() {
        i += 1
        if (i == 1) {
            findNavController().navigate(R.id.action_selectCompression_to_compressingDialogFragment)
        }
    }

    override fun onClickMediumSizeOption() {
        activityViewModel.postCompressionQuantity(CompressionQuantity.MediumSize())
        binding.optionSelected = CompressionQuantity.MediumSize()
    }

    override fun onClickLagerSizeOption() {
        activityViewModel.postCompressionQuantity(CompressionQuantity.LagerQuality())
        binding.optionSelected = CompressionQuantity.LagerQuality()
    }

    override fun onClickSmailSizeOption() {
        activityViewModel.postCompressionQuantity(CompressionQuantity.SmallSize())
        binding.optionSelected = CompressionQuantity.SmallSize()
    }

    override fun onClickVerySmailSizeOption() {
        activityViewModel.postCompressionQuantity(CompressionQuantity.VerySmallSize())
        binding.optionSelected = CompressionQuantity.VerySmallSize()
    }

    override fun onClickCustomSizeOption() {
        CustomFileSizeDialogFragment(this).show(
            parentFragmentManager,
            CustomFileSizeDialogFragment::class.simpleName
        )
    }

    override fun onConfirmed(size: Long, unit: String) {

    }
}