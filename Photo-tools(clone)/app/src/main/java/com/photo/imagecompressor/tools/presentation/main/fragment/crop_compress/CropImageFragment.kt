package com.photo.imagecompressor.tools.presentation.main.fragment.crop_compress

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.photo.imagecompressor.tools.base.BaseFragment
import com.photo.imagecompressor.tools.utils.clickIconNavigation
import com.photo.imagecompressor.tools.utils.popBackStack
import com.photo.imagecompressor.tools.utils.setMenuToolbar
import com.photo.imagecompressor.tools.utils.startToSelectCompressionFromCrop
import com.google.android.material.navigation.NavigationBarView
import com.photo.imagecompressor.tools.R
import com.photo.imagecompressor.tools.databinding.FragmentCropImageBinding
import com.photo.imagecompressor.tools.domain.model.Image_Type
import com.photo.imagecompressor.tools.domain.model.Option_Crop
import com.photo.imagecompressor.tools.presentation.main.BackDialogFragment
import com.photo.imagecompressor.tools.presentation.main.fragment.compressing.CompressingDialogLoadingFragment
import com.photo.imagecompressor.tools.presentation.main.fragment.select_images.MainImageViewModel
import com.yalantis.ucrop.callback.BitmapCropCallback
import com.yalantis.ucrop.view.CropImageView.SOURCE_IMAGE_ASPECT_RATIO
import com.yalantis.ucrop.view.OverlayView
import com.yalantis.ucrop.view.widget.HorizontalProgressWheelView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.File
import java.text.DecimalFormat

import kotlinx.coroutines.*


interface CropImageFragmentEvent {
    fun onClickRotate90()
    fun onClickClose()
}

class CropImageFragment : BaseFragment<FragmentCropImageBinding>(), CropImageFragmentEvent,
    HorizontalProgressWheelView.ScrollingListener,
    NavigationBarView.OnItemSelectedListener {
    override fun provideViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentCropImageBinding.inflate(inflater, container, false)

    private val cropAdapter by lazy { OptionCropAdapter() }
    private val currentRotate get() = cropImageView.currentAngle
    private val mainImageViewModel by activityViewModels<MainImageViewModel>()
    private val firstImage get() = mainImageViewModel.getImagesSelected().first()
    private val uri by lazy {
        firstImage.path.toUri()
    }

    private val cropImageView by lazy { binding.cropImageView.cropImageView }
    override fun setupView(view: View, savedInstanceState: Bundle?) {
        binding.lifecycleOwner = this
        binding.cropImageFragmentEvent = this@CropImageFragment
        binding.apply {
            setUpCropImage()
            setUpToolbar()
            setAdapter()
            bottomNavigation.setOnItemSelectedListener(this@CropImageFragment)
            horizontalProgressWheelView.setScrollingListener(this@CropImageFragment)
        }
    }

    private fun setUpCropImage() {
        val path = requireActivity().cacheDir.path + "/cropImage/"
        val directory = File(path)

        if (!directory.exists()) {
            directory.mkdirs()
        }

        val file = File(directory, "${System.currentTimeMillis()}.jpg")
        binding.cropImageView.overlayView.freestyleCropMode =
            OverlayView.FREESTYLE_CROP_MODE_ENABLE
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.Main) {
                cropImageView.setImageUri(uri, file.toUri())
            }
        }
    }

    companion object {
        var FILE_SIZE: Long = 0
    }

    var i = 0
    private fun setUpToolbar() {
        binding.apply {
            title = getString(R.string.edit)

            includeToolbar.clickIconNavigation {
                BackDialogFragment.show(requireActivity())
            }

            includeToolbar.setMenuToolbar(R.menu.menu_done) {
                when (it.itemId) {
                    R.id.done -> {
                        i += 1
                        if (i == 1) {
                            val dialog = CompressingDialogLoadingFragment()
                            dialog.show(
                                requireActivity().supportFragmentManager,
                                "lạda"
                            )
                            this@CropImageFragment.cropImageView.cropAndSaveImage(
                                when (firstImage.mime) {
                                    Image_Type.PNG -> Bitmap.CompressFormat.PNG
                                    Image_Type.JPEG -> Bitmap.CompressFormat.JPEG
                                    Image_Type.WEBP -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                                        Bitmap.CompressFormat.WEBP_LOSSY
                                    } else {
                                        Bitmap.CompressFormat.WEBP
                                    }

                                    else -> Bitmap.CompressFormat.JPEG
                                },
                                80,
                                object : BitmapCropCallback {
                                    override fun onBitmapCropped(
                                        resultUri: Uri,
                                        offsetX: Int,
                                        offsetY: Int,
                                        imageWidth: Int,
                                        imageHeight: Int
                                    ) {

                                        CoroutineScope(Dispatchers.IO).launch {
                                            val bitmaps = BitmapFactory.decodeFile(resultUri.path)
                                            FILE_SIZE = File(resultUri.path).length()
                                            mainImageViewModel.getImagesSelected()[0].bitmap =
                                                bitmaps
                                            launch(Dispatchers.Main) {
                                                delay(500)
                                                dialog.dismiss()
                                                startToSelectCompressionFromCrop()
                                            }
                                        }
                                    }

                                    override fun onCropFailure(t: Throwable) {
                                        i = 0
                                        dialog.dismiss()
                                        showMess("Error cropping")
                                    }
                                }
                            )
                        }
                    }
                }
                true
            }
        }
    }

    private fun setAdapter() {
        var beforePosition = 2
        cropAdapter.submitData = ArrayList(Option_Crop.optionCrops)
        cropImageView.isSoundEffectsEnabled = true
        cropAdapter.setOnClickListener { optionCrop, position ->
            optionCrop.isSelected = !optionCrop.isSelected
            val payload = cropAdapter.submitData[beforePosition]
                .apply { isSelected = false }
            cropAdapter.notifyItemChanged(beforePosition, payload)
            cropAdapter.notifyItemChanged(position, optionCrop)
            beforePosition = position
            if (optionCrop.x == 0 && optionCrop.y == 0) {
                cropImageView.targetAspectRatio = SOURCE_IMAGE_ASPECT_RATIO
                return@setOnClickListener
            }
            cropImageView.targetAspectRatio = optionCrop.y / (optionCrop.x * 1.0f)
        }
        binding.recyclerOptionCrop.adapter = cropAdapter
    }


    override fun onNavigationItemSelected(menu: MenuItem): Boolean {
        return when (menu.itemId) {
            R.id.crop -> {
                binding.recyclerOptionCrop.visibility = View.VISIBLE
                binding.mConstraintRotate.visibility = View.GONE
                cropImageView.isScaleEnabled = false
                cropImageView.isRotateEnabled = false
                isScale = false
                true
            }

            R.id.rotate -> {
                binding.recyclerOptionCrop.visibility = View.GONE
                binding.mConstraintRotate.visibility = View.VISIBLE
                binding.close.visibility = View.VISIBLE
                binding.rotate90.visibility = View.VISIBLE
                cropImageView.isRotateEnabled = true
                true
            }

            R.id.scale -> {
                binding.recyclerOptionCrop.visibility = View.GONE
                binding.mConstraintRotate.visibility = View.VISIBLE
                binding.close.visibility = View.GONE
                binding.degrees.visibility = View.GONE
                binding.rotate90.visibility = View.GONE
                cropImageView.isScaleEnabled = true
                cropImageView.isRotateEnabled = true
                isScale = true

                true
            }

            else -> false
        }
    }


    override fun onClickRotate90() {
        cropImageView.postRotate(90f)
        binding.degrees.text = decimalFormat.format(currentRotate)
    }

    override fun onClickClose() {
        cropImageView.postRotate(360 - currentRotate)
        binding.degrees.text = decimalFormat.format(currentRotate)
    }

    override fun onScrollStart() {

    }

    private val decimalFormat = DecimalFormat("#.#")
    private var isScale = false
    override fun onScroll(delta: Float, totalDistance: Float) {

        if (isScale) {
            if (delta > 0) {
                binding.cropImageView.cropImageView.zoomInImage(
                    binding.cropImageView.cropImageView.getCurrentScale()
                            + delta * ((binding.cropImageView.cropImageView.getMaxScale() - binding.cropImageView.cropImageView.getMinScale()) / 15000)
                )
            } else {
                binding.cropImageView.cropImageView.zoomOutImage(
                    (binding.cropImageView.cropImageView.getCurrentScale()
                            + delta * ((binding.cropImageView.cropImageView.getMaxScale() - binding.cropImageView.cropImageView.getMinScale()) / 15000))
                )
            }
        } else {
            cropImageView.postRotate(delta / 42)

            binding.degrees.text = decimalFormat.format(currentRotate)
        }
    }

    override fun onScrollEnd() {

    }

}