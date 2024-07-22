package com.photo.imagecompressor.tools.presentation.main.fragment.result

import android.content.ContentUris
import android.content.Context
import android.os.Build
import android.provider.MediaStore
import androidx.activity.result.IntentSenderRequest
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.photo.imagecompressor.tools.base.BaseViewModel
 import com.photo.imagecompressor.tools.domain.model.Image_
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RsViewModel : BaseViewModel() {
    fun postResult(before: ArrayList<Image_>, after: ArrayList<Image_>) {
        val results = after.mapIndexed { i, data ->
            Pair(before[i], data)
        }.toList()
        resultLiveData.postValue(java.util.ArrayList(results))
    }

    @RequiresApi(Build.VERSION_CODES.R)
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

    private val _resultLiveData = MutableLiveData<ArrayList<Pair<Image_, Image_>>>()
    val resultLiveData get() = _resultLiveData
}