package com.photo.image.picture.tools.compressor.persentation.main.fragment.convert

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.photo.image.picture.tools.compressor.action.CompressionQuantity
import com.photo.image.picture.tools.compressor.base.BaseViewModel
import com.photo.image.picture.tools.compressor.domain.model.ImageType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class ConvertViewModel @Inject constructor() : BaseViewModel(), ConvertFragmentEvent {
    private val compressionQuantities = listOf(
        CompressionQuantity.BestQuality(),
        CompressionQuantity.SmallSize(),
        CompressionQuantity.MediumSize(),
    )
    val onClickCompress: MutableLiveData<Boolean> = MutableLiveData()

    private val _compressOptionLiveData: MutableLiveData<CompressionQuantity> = MutableLiveData(compressionQuantities[0])
    val compressOptionLiveData: LiveData<CompressionQuantity> get() = _compressOptionLiveData

    private val _formatOptionLiveData: MutableLiveData<ImageType> = MutableLiveData(ImageType.JPEG)
    val formatOptionLiveData: LiveData<ImageType> get() = _formatOptionLiveData

    override fun onClickFormatOption(imageType: ImageType) {
        _formatOptionLiveData.postValue(imageType)
    }

    override fun onClickCompress() {
        onClickCompress.postValue(true)
    }

    override fun onClickCompressOption(position: Int) {
        _compressOptionLiveData.postValue(compressionQuantities[position])
    }
}

interface ConvertFragmentEvent {
    fun onClickCompressOption(position: Int)
    fun onClickFormatOption(imageType: ImageType)
    fun onClickCompress()
}