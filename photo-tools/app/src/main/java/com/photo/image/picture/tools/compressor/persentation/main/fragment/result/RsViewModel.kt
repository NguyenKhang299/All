package com.photo.image.picture.tools.compressor.persentation.main.fragment.result

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.result.IntentSenderRequest
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.photo.image.picture.tools.compressor.base.BaseViewModel
import com.photo.image.picture.tools.compressor.domain.model.Image
import com.photo.image.picture.tools.compressor.utils.XToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RsViewModel : BaseViewModel() {
    fun postResult(before: ArrayList<Image>, after: ArrayList<Image>) {
        val results = after.mapIndexed { i, data ->
            Pair(before[i], data)
        }.toList()
        resultLiveData.postValue(java.util.ArrayList(results))
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

    private val _resultLiveData = MutableLiveData<ArrayList<Pair<Image, Image>>>()
    val resultLiveData get() = _resultLiveData
}