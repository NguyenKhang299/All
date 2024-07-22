package com.photo.imagecompressor.tools.presentation.main.fragment.advance

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.photo.imagecompressor.tools.base.BaseViewModel
 
import com.photo.imagecompressor.tools.utils.Utils.Companion.getResolutionBySize
import com.photo.imagecompressor.tools.action.CompressionQuantity
import com.photo.imagecompressor.tools.domain.model.Image_
import com.photo.imagecompressor.tools.domain.model.Image_Type
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AdvanceViewModel @Inject constructor() : BaseViewModel(), AdvanceCompressFragmentEvent {
    val compressionQuantities = listOf(
        CompressionQuantity.VerySmallSize(),
        CompressionQuantity.SmallSize(),
        CompressionQuantity.MediumSize(),
        CompressionQuantity.LagerQuality(),
     ).reversed()

    private val _formatOptionLiveData: MutableLiveData<Image_Type> = MutableLiveData(Image_Type.AUTO)
    val formatOptionLiveData: LiveData<Image_Type> get() = _formatOptionLiveData
    val onClickCompress: MutableLiveData<Boolean> = MutableLiveData()
    private val _isClickFirst: MutableLiveData<Boolean> = MutableLiveData(false)
    val isClickFirst: LiveData<Boolean> get() = _isClickFirst

    var imageSelected = ArrayList<Image_>()
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
        onClickCompress.postValue(isClickFirst.value == false)
        if (_isClickFirst.value == false) {
            _isClickFirst.value = true
        }
    }

    override fun onClickFormatOption(Image_: Image_Type) {
         _formatOptionLiveData.postValue(Image_)
    }
}

interface AdvanceCompressFragmentEvent {
    fun onClickCompress()
    fun onClickFormatOption(Image_: Image_Type)
}