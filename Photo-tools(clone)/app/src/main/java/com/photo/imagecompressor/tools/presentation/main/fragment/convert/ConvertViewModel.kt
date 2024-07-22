package com.photo.imagecompressor.tools.presentation.main.fragment.convert

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.photo.imagecompressor.tools.base.BaseViewModel
import com.photo.imagecompressor.tools.action.CompressionQuantity
import com.photo.imagecompressor.tools.domain.model.Image_Type
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class ConvertViewModel @Inject constructor() : BaseViewModel(), ConvertFragmentEvent {
    private val compressionQuantities = listOf(
        CompressionQuantity.LagerQuality(),
        CompressionQuantity.SmallSize(),
        CompressionQuantity.MediumSize(),
        CompressionQuantity.LagerQuality(),
    )
    val onClickCompress: MutableLiveData<Boolean> = MutableLiveData()

    private val _compressOptionLiveData: MutableLiveData<CompressionQuantity> =
        MutableLiveData(compressionQuantities[0])
    val compressOptionLiveData: LiveData<CompressionQuantity> get() = _compressOptionLiveData

    private val _formatOptionLiveData: MutableLiveData<Image_Type> =
        MutableLiveData(Image_Type.JPEG)
    val formatOptionLiveData: LiveData<Image_Type> get() = _formatOptionLiveData

    private val _isClickFirst: MutableLiveData<Boolean> = MutableLiveData(false)
    val isClickFirst: LiveData<Boolean> get() = _isClickFirst
    override fun onClickFormatOption(imageType: Image_Type) {
        _formatOptionLiveData.postValue(imageType)
    }

    override fun onClickCompress() {
        onClickCompress.postValue(isClickFirst.value == false)
        if (_isClickFirst.value == false) {
            _isClickFirst.value = true
        }
    }

    override fun onClickCompressOption(position: Int) {
        _compressOptionLiveData.postValue(compressionQuantities[position])
    }
}

interface ConvertFragmentEvent {
    fun onClickCompressOption(position: Int)
    fun onClickFormatOption(imageType: Image_Type)
    fun onClickCompress()
}