package com.photo.imagecompressor.tools.presentation.main.fragment.select_images

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.photo.imagecompressor.tools.usecase.images.ImageUseCase
import com.photo.imagecompressor.tools.action.ActionImage
import com.photo.imagecompressor.tools.action.CompressionQuantity
import com.photo.imagecompressor.tools.domain.model.Folder_Image
import com.photo.imagecompressor.tools.domain.model.Image_
import com.photo.imagecompressor.tools.domain.model.Image_Type
import com.photo.imagecompressor.tools.domain.model.Option_Image
import com.photo.imagecompressor.tools.utils.FileUtils
import com.photo.imagecompressor.tools.utils.Utils.Companion.getResolutionBySize
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainImageViewModel @Inject constructor(private val imageUseCase: ImageUseCase) :
    ViewModel() {
    private val _imageSelectedLiveData: MutableLiveData<Image_> = MutableLiveData()
    val imageSelectedLiveData: LiveData<Image_> get() = _imageSelectedLiveData

    private val _imageUnSelectedLiveData: MutableLiveData<Image_> = MutableLiveData()
    val imageUnSelectedLiveData: LiveData<Image_> get() = _imageUnSelectedLiveData

    private val _closeSelectedLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val closeSelectedLiveData: LiveData<Boolean> get() = _closeSelectedLiveData

    private val _showSelectedLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val showSelectedLiveData: LiveData<Boolean> get() = _showSelectedLiveData

    private val _saveLiveData: MutableLiveData<Image_?> = MutableLiveData()
    val saveLiveData: LiveData<Image_?> get() = _saveLiveData

    private val _imagesLiveData: MutableLiveData<List<Image_>> = MutableLiveData()
    val imagesLiveData: LiveData<List<Image_>> get() = _imagesLiveData

    private val _folderWithImagesLiveData: MutableLiveData<List<Folder_Image>> = MutableLiveData()
    val folderWithImagesLiveData: LiveData<List<Folder_Image>> get() = _folderWithImagesLiveData

    private val _imagesByIdFolderLiveData: MutableLiveData<List<Image_>> = MutableLiveData()
    val imagesByIdFolderLiveData: LiveData<List<Image_>> get() = _imagesByIdFolderLiveData

    private val _showCheckedLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val showCheckedLiveData: LiveData<Boolean> get() = _showCheckedLiveData

    fun postClose(isClose: Boolean) {
        _closeSelectedLiveData.postValue(isClose)
    }

    private val imageSelected = ArrayList<Image_>()
    private val optionImage = Option_Image()
    fun postFormatTo(imageType: Image_Type) {
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
    fun setSelected(Image_: Image_) {

        imageSelected.indexOfLast { it.id == Image_.id }.takeIf { it != -1 }?.let {
            imageSelected.removeAt(it)
            _imageUnSelectedLiveData.postValue(Image_)
            return
        }
        imageSelected.add(Image_)
        _imageSelectedLiveData.postValue(Image_)
    }

    fun updateImageDataSelected(images: List<Image_>): List<Image_> {
        if (imageSelected.isNotEmpty()) {
            return images.map { Image_ ->
                imageSelected.indexOfLast { it.id == Image_.id }
                    .takeIf { it != -1 }?.let {
                        Image_.isSelected = true
                        Image_
                    }
                Image_
            }
        }
        return images
    }

    val imagesAfter = ArrayList<Image_>()
    private fun saveImages(compressionQuantity: CompressionQuantity, position: Int) {
        if (position == imageSelected.size) {
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            val images =
                imageUseCase.saveImagesUseCase(imageCompress[position], compressionQuantity)
            if (imagesAfter.isEmpty()) delay(350)
            if (images != null) {
                imagesAfter.add(images)
            }
            delay(100)
            _saveLiveData.postValue(images)
            saveImages(compressionQuantity, position + 1)
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

    fun compressImage(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            val actionImage = getActionImage()
            val compressionQuantity = getCompressionQuantity()
            if (optionImage.formatTo != Image_Type.AUTO) {
                imageCompress.forEach { it.mime = optionImage.formatTo }
            }
            when (actionImage) {
                ActionImage.COMPRESS_PHOTO -> {

                    imageCompress.forEach { Image_ ->
                        val nR = Image_.resolution.getResolutionBySize(
                            compressionQuantity,
                            null
                        )
                        val bitmap = FileUtils.getBitMapFromUri(context, Image_.path.toUri())
                        Image_.bitmap = createScaledBitmap(bitmap, nR.rW, nR.rH, true)
                    }

                    saveImages(compressionQuantity, 0)
                }

                ActionImage.ADVANCE_COMPRESS -> {
                    imageCompress.forEach { Image_ ->
                        val nR = Image_.resolution.getResolutionBySize(
                            compressionQuantity,
                            null
                        )
                        val bitmap = FileUtils.getBitMapFromUri(context, Image_.path.toUri())
                        Image_.bitmap = createScaledBitmap(bitmap, nR.rW, nR.rH, true)
                    }
                    saveImages(compressionQuantity, 0)
                }

                ActionImage.CROP_COMPRESS -> {
                    imageCompress.forEach { Image_ ->
                        val nR = Image_.resolution.getResolutionBySize(
                            compressionQuantity,
                            null
                        )
                        Image_.bitmap = createScaledBitmap(
                            Image_.bitmap!!,
                            Image_.bitmap!!.width,
                            Image_.bitmap!!.height,
                            true
                        )
                    }
                    saveImages(compressionQuantity, 0)
                }

                ActionImage.RESIZE_COMPRESS -> {
                    imageCompress.forEach { Image_ ->
                        val nR = Image_.resolution.getResolutionBySize(
                            compressionQuantity,
                            null
                        )
                        var bitmap = FileUtils.getBitMapFromUri(context, Image_.path.toUri())
                        bitmap = createScaledBitmap(bitmap, nR.rW, nR.rH, true)
                        Image_.bitmap = bitmap
                    }
                    saveImages(compressionQuantity, 0)
                }

                ActionImage.CONVERT_PHOTO -> {
                    imageCompress.forEach { Image_ ->
                        val nR = Image_.resolution.getResolutionBySize(
                            compressionQuantity,
                            null
                        )
                        var bitmap = FileUtils.getBitMapFromUri(context, Image_.path.toUri())
                        bitmap = createScaledBitmap(bitmap, nR.rW, nR.rH, true)
                        Image_.bitmap = bitmap
                    }
                    saveImages(compressionQuantity, 0)
                }
            }
        }
    }

    private fun createScaledBitmap(bitmap: Bitmap, rW: Int, rH: Int, b: Boolean): Bitmap {
        return Bitmap.createScaledBitmap(bitmap, rW, rH, true)
    }

}