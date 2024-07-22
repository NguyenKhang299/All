package com.photo.image.picture.tools.compressor.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel() {

    val messageString: MutableLiveData<String> = MutableLiveData()
    val messageStringId: MutableLiveData<Int> = MutableLiveData()

    override fun onCleared() {
        super.onCleared()
     }
}