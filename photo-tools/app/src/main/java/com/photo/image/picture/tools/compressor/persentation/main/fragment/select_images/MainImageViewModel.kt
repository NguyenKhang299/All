package com.photo.image.picture.tools.compressor.persentation.main.fragment.select_images

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.photo.image.picture.tools.compressor.action.ActionImage
import com.photo.image.picture.tools.compressor.action.CompressionQuantity
import com.photo.image.picture.tools.compressor.domain.model.FolderImage
import com.photo.image.picture.tools.compressor.domain.model.Image
import com.photo.image.picture.tools.compressor.domain.model.ImageType
import com.photo.image.picture.tools.compressor.domain.model.OptionImage
import com.photo.image.picture.tools.compressor.usecase.images.ImageUseCase
import com.photo.image.picture.tools.compressor.utils.Utils.Companion.getResolutionBySize
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainImageViewModel @Inject constructor(private val imageUseCase: ImageUseCase) :
    ViewModel() {
    private val _imageSelectedLiveData: MutableLiveData<Image> = MutableLiveData()
    val imageSelectedLiveData: LiveData<Image> get() = _imageSelectedLiveData

    private val _imageUnSelectedLiveData: MutableLiveData<Image> = MutableLiveData()
    val imageUnSelectedLiveData: LiveData<Image> get() = _imageUnSelectedLiveData

    private val _closeSelectedLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val closeSelectedLiveData: LiveData<Boolean> get() = _closeSelectedLiveData

    private val _showSelectedLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val showSelectedLiveData: LiveData<Boolean> get() = _showSelectedLiveData

    private val _saveLiveData: MutableLiveData<Image?> = MutableLiveData()
    val saveLiveData: LiveData<Image?> get() = _saveLiveData

    private val _imagesLiveData: MutableLiveData<List<Image>> = MutableLiveData()
    val imagesLiveData: LiveData<List<Image>> get() = _imagesLiveData

    private val _folderWithImagesLiveData: MutableLiveData<List<FolderImage>> = MutableLiveData()
    val folderWithImagesLiveData: LiveData<List<FolderImage>> get() = _folderWithImagesLiveData

    private val _imagesByIdFolderLiveData: MutableLiveData<List<Image>> = MutableLiveData()
    val imagesByIdFolderLiveData: LiveData<List<Image>> get() = _imagesByIdFolderLiveData

    private val _showCheckedLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val showCheckedLiveData: LiveData<Boolean> get() = _showCheckedLiveData

    fun postClose(isClose: Boolean) {
        _closeSelectedLiveData.postValue(isClose)
    }

    private val imageSelected = ArrayList<Image>()
    private val optionImage = OptionImage()
    fun postFormatTo(imageType: ImageType) {
        optionImage.formatTo = imageType
    }

    fun postActionImage(actionImage: ActionImage) {
        optionImage.actionImage = actionImage
    }

    fun postCompressionQuantity(compressionQuantity: CompressionQuantity) {
        optionImage.compressionQuantity = compressionQuantity
    }

    fun getImagesSelected() = imageSelected
    fun getActionImage() = optionImage.actionImage
    private fun getFormatTo() = optionImage.formatTo
    fun getCompressionQuantity() = optionImage.compressionQuantity
    private val imageCompress get() = ArrayList(imageSelected)

    fun showSelect() = _showCheckedLiveData.postValue(imageSelected.isNotEmpty())
    fun setSelected(image: Image) {
        if (getActionImage() == ActionImage.CROP_COMPRESS) {
            if (imageSelected.size > 0) return
        }
        imageSelected.indexOfLast { it.id == image.id }.takeIf { it != -1 }?.let {
            imageSelected.removeAt(it)
            _imageUnSelectedLiveData.postValue(image)
            return
        }
        imageSelected.add(image)
        _imageSelectedLiveData.postValue(image)
    }

    fun updateImageDataSelected(images: List<Image>): List<Image> {
        if (imageSelected.isNotEmpty()) {
            return images.map { image ->
                imageSelected.indexOfLast { it.id == image.id }
                    .takeIf { it != -1 }?.let {
                        image.isSelected = true
                        image
                    }
                image
            }
        }
        return images
    }

    val imagesAfter = ArrayList<Image>()
    private fun saveImages(compressionQuantity: CompressionQuantity, position: Int) {
        if (position == imageSelected.size) {
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            val images = imageUseCase.saveImagesUseCase(imageCompress[position], compressionQuantity)
            if (images != null) {
                imagesAfter.add(images)
            }
            _saveLiveData.postValue(images)
            saveImages(compressionQuantity, position + 1)
            delay(200)
        }
    }

    fun getAllImages() {
        viewModelScope.launch(Dispatchers.IO) {
            val images = imageUseCase.getAllImagesUseCase()
            val newImages = updateImageDataSelected(images)
            _imagesLiveData.postValue(newImages)
        }
    }

    fun getImageByFolder(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val images = imageUseCase.getImageByFolderUseCase(id)
            val newImages = updateImageDataSelected(images)
            _imagesByIdFolderLiveData.postValue(newImages)
        }
    }

    fun getFoldersWithImages() {
        viewModelScope.launch(Dispatchers.IO) {
            val folders = imageUseCase.getFolderImagesUseCase()
            _folderWithImagesLiveData.postValue(folders)
        }
    }

    fun compressImage() {
        val actionImage = getActionImage()
        val compressionQuantity = getCompressionQuantity()
        val isCustom = optionImage.compressionQuantity is CompressionQuantity.Custom
        if (optionImage.formatTo != ImageType.AUTO) {
            imageCompress.forEach { it.mime = optionImage.formatTo!! }
        }
        when (actionImage) {
            ActionImage.COMPRESS_PHOTO -> {

                imageCompress.forEach { image ->
                    val nR = image.resolution.getResolutionBySize(
                        compressionQuantity,
                        if (isCustom) image else null
                    )
                    val bitmap = BitmapFactory.decodeFile(image.path)
                    if (compressionQuantity is CompressionQuantity.Custom) {
                        image.bitmap = createScaledBitmap(
                            bitmap,
                            (compressionQuantity.size / (image.size * 1.0f) * image.resolution.rW).toInt(),
                            (compressionQuantity.size / (image.size * 1.0f) * image.resolution.rH).toInt(),
                            true
                        )
                    } else {
                        image.bitmap = createScaledBitmap(bitmap, nR.rW, nR.rH, true)
                    }
                }
                saveImages(compressionQuantity, 0)
            }

            ActionImage.ADVANCE_COMPRESS -> {
                imageCompress.forEach { image ->
                    val nR = image.resolution.getResolutionBySize(
                        compressionQuantity,
                        if (isCustom) image else null
                    )
                    val bitmap = BitmapFactory.decodeFile(image.path)
                    image.bitmap = createScaledBitmap(bitmap, nR.rW, nR.rH, true)
                }
                saveImages(compressionQuantity, 0)
            }

            ActionImage.CROP_COMPRESS -> {
                imageCompress.forEach { image ->
                    val nR = image.resolution.getResolutionBySize(
                        compressionQuantity,
                        if (isCustom) image else null
                    )
                    if (compressionQuantity is CompressionQuantity.Custom) {
                        image.bitmap = createScaledBitmap(
                            image.bitmap!!,
                            (compressionQuantity.size / (image.bitmap!!.byteCount * 1.0f) * image.bitmap!!.width).toInt(),
                            (compressionQuantity.size / (image.bitmap!!.byteCount * 1.0f) * image.bitmap!!.height).toInt(),
                            true
                        )
                    } else {
                        image.bitmap = createScaledBitmap(
                            image.bitmap!!,
                            image.bitmap!!.width,
                            image.bitmap!!.height,
                            true
                        )
                    }
                }
                saveImages(compressionQuantity, 0)
            }

            ActionImage.RESIZE_COMPRESS -> {
                imageCompress.forEach { image ->
                    val nR = image.resolution.getResolutionBySize(
                        compressionQuantity,
                        if (isCustom) image else null
                    )
                    var bitmap = BitmapFactory.decodeFile(image.path)
                    bitmap = createScaledBitmap(bitmap, nR.rW, nR.rH, true)
                    image.bitmap = bitmap
                }
                saveImages(compressionQuantity, 0)
            }

            ActionImage.CONVERT_PHOTO -> {
                imageCompress.forEach { image ->
                    val nR = image.resolution.getResolutionBySize(
                        compressionQuantity,
                        if (isCustom) image else null
                    )
                    var bitmap = BitmapFactory.decodeFile(image.path)
                    bitmap = createScaledBitmap(bitmap, nR.rW, nR.rH, true)
                    image.bitmap = bitmap
                }
                saveImages(compressionQuantity, 0)
            }
        }
    }

    private fun createScaledBitmap(bitmap: Bitmap, rW: Int, rH: Int, b: Boolean): Bitmap {
        return Bitmap.createScaledBitmap(bitmap, rW, rH, true)
    }

}