package com.photo.imagecompressor.tools.presentation.main.fragment.compressing

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.photo.imagecompressor.tools.base.BaseViewModel

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