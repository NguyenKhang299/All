package com.photo.image.picture.tools.compressor.persentation.main.fragment.advance

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.photo.image.picture.tools.compressor.action.CompressionQuantity
import com.photo.image.picture.tools.compressor.base.BaseViewModel
import com.photo.image.picture.tools.compressor.domain.model.Image
import com.photo.image.picture.tools.compressor.domain.model.ImageType
import com.photo.image.picture.tools.compressor.utils.Utils.Companion.getResolutionBySize
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AdvanceViewModel @Inject constructor() : BaseViewModel(), AdvanceCompressFragmentEvent {
    val compressionQuantities = listOf(
        CompressionQuantity.VerySmallSize(),
        CompressionQuantity.SmallSize(),
        CompressionQuantity.MediumSize(),
        CompressionQuantity.LagerQuality(),
        CompressionQuantity.BestQuality()
    ).reversed()

    private val _formatOptionLiveData: MutableLiveData<ImageType> = MutableLiveData()
    val formatOptionLiveData: LiveData<ImageType> get() = _formatOptionLiveData
    val onClickCompress: MutableLiveData<Boolean> = MutableLiveData()

    var imageSelected = ArrayList<Image>()
    val listSize: ArrayList<String>
        get() = ArrayList<String>().apply {
            compressionQuantities.forEachIndexed { index, compressionQuantity ->
                if (imageSelected.size > 1) {
                    add("(${compressionQuantity.quantityDefault}%)")
                }
                if (imageSelected.size == 1) {
                    add("${imageSelected[0].resolution.getResolutionBySize(compressionQuantity)} (${compressionQuantity.quantityDefault}%)")
                }
            }
        }

    override fun onClickCompress() {
         onClickCompress.postValue(true)
    }

    override fun onClickFormatOption(imageType: ImageType) {
         _formatOptionLiveData.postValue(imageType)
    }
}

interface AdvanceCompressFragmentEvent {
    fun onClickCompress()
    fun onClickFormatOption(imageType: ImageType)
}