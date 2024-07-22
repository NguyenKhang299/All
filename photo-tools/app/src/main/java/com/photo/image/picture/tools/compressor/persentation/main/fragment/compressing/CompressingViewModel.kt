package com.photo.image.picture.tools.compressor.persentation.main.fragment.compressing

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.photo.image.picture.tools.compressor.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel

class CompressingViewModel : BaseViewModel() {
    private val _progressLiveData = MutableLiveData<Float>()
    val progressLiveData: LiveData<Float>
        get() = _progressLiveData

    private var count = 1
    private var countTotal = 1

    fun setCountTotal(countTotal: Int) {
        this.countTotal = countTotal
    }

    fun updateProgress() {
         _progressLiveData.postValue((count / (1.0f * countTotal)) * 100)
        count += 1
        if (_progressLiveData.value == 100f) {
            count = 1
        }
    }
}