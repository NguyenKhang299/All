package com.photo.image.picture.tools.compressor.persentation.compressed

import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore
import androidx.activity.result.IntentSenderRequest
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.photo.image.picture.tools.compressor.base.BaseViewModel
import com.photo.image.picture.tools.compressor.domain.model.Image
import com.photo.image.picture.tools.compressor.usecase.images.ImageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompressedViewModel @Inject constructor(private val imageUseCase: ImageUseCase) :
    BaseViewModel() {
    private val _imagesLiveData: MutableLiveData<List<Image>> = MutableLiveData()
    val imagesLiveData: LiveData<List<Image>> get() = _imagesLiveData
    fun getImageSave() {
        viewModelScope.launch(Dispatchers.IO) {
            val images = imageUseCase.getImageSave()
            _imagesLiveData.postValue(images)
        }
    }
    fun deletePhotoFromExternalStorage(context: Context, listID: List<Long>) {
        viewModelScope.launch(Dispatchers.IO) {
            val intentSender = MediaStore.createDeleteRequest(
                context.contentResolver,
                listID.map {
                    ContentUris.withAppendedId(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        it
                    )
                }
            )
            intentSender.let { sender ->
                _launchIntentSenderLiveData.postValue(
                    IntentSenderRequest.Builder(sender).build()
                )
            }
        }
    }

    private val _launchIntentSenderLiveData = MutableLiveData<IntentSenderRequest>()
    val launchIntentSenderLiveData get() = _launchIntentSenderLiveData
}