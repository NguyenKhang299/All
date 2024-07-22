package com.photo.imagecompressor.tools.presentation.compressed_

import android.content.ContentUris
import android.content.Context
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.result.IntentSenderRequest
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.photo.imagecompressor.tools.base.BaseViewModel
import com.photo.imagecompressor.tools.domain.model.Image_
import com.photo.imagecompressor.tools.usecase.images.ImageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject


@HiltViewModel
class Compressed_ViewModel @Inject constructor(private val imageUseCase: ImageUseCase) :
    BaseViewModel() {
    private val _imagesLiveData: MutableLiveData<List<Image_>> = MutableLiveData()
    val imagesLiveData: LiveData<List<Image_>> get() = _imagesLiveData
    val _deleteImage: MutableLiveData<Pair<Boolean, Image_>> = MutableLiveData()
    private val _launchIntentSenderLiveData = MutableLiveData<IntentSenderRequest>()
    val launchIntentSenderLiveData get() = _launchIntentSenderLiveData
    fun getImageSave() {
        viewModelScope.launch(Dispatchers.IO) {
            val images = imageUseCase.getImageSave()
            _imagesLiveData.postValue(images)
        }
    }

    fun deletePhotoFromExternalStorage(context: Context, image: Image_) {
        viewModelScope.launch(Dispatchers.IO) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val intentSender = MediaStore.createDeleteRequest(
                    context.contentResolver,
                    arrayListOf(
                        ContentUris.withAppendedId(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            image.id.toLong()
                        )
                    )
                )
                intentSender.let { sender ->
                    _launchIntentSenderLiveData.postValue(
                        IntentSenderRequest.Builder(sender).build()
                    )
                }

            }
        }
    }
}