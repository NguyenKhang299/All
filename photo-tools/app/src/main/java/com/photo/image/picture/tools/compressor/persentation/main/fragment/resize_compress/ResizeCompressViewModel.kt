package com.photo.image.picture.tools.compressor.persentation.main.fragment.resize_compress

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.photo.image.picture.tools.compressor.action.CompressionQuantity
import com.photo.image.picture.tools.compressor.base.BaseViewModel
import com.photo.image.picture.tools.compressor.domain.model.Image
import com.photo.image.picture.tools.compressor.utils.Utils.Companion.getResolutionBySize

class ResizeCompressViewModel : BaseViewModel(), ResizeFragmentEvent {
    private val quantities = listOf(25, 50, 100)
    val compressionQuantities = listOf(
        CompressionQuantity.VerySmallSize(),
        CompressionQuantity.SmallSize(),
        CompressionQuantity.MediumSize(),
        CompressionQuantity.LagerQuality(),
        CompressionQuantity.BestQuality()
    ).reversed()

    private val _compressOptionLiveData: MutableLiveData<CompressionQuantity> = MutableLiveData(compressionQuantities[0])
    val compressOptionLiveData: LiveData<CompressionQuantity> get() = _compressOptionLiveData

    private val _clickSizeLiveData: MutableLiveData<Int> = MutableLiveData(0)
    val clickSizeLiveData: LiveData<Int> get() = _clickSizeLiveData

    private val _clickCompress: MutableLiveData<Boolean> = MutableLiveData()
    val clickCompress: LiveData<Boolean> get() = _clickCompress

    init {
        _clickSizeLiveData.postValue(quantities[0])
    }

    override fun onClickCompressOption(position: Int) {
        _compressOptionLiveData.postValue(compressionQuantities[position])
    }

    override fun onClickFileSize(position: Int) {
         _clickSizeLiveData.postValue(quantities[position])
    }

    override fun onClickCompress() {
        _clickCompress.postValue(true)
    }

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

}

interface ResizeFragmentEvent {
    fun onClickCompressOption(position: Int)
    fun onClickFileSize(position: Int)
    fun onClickCompress()
}