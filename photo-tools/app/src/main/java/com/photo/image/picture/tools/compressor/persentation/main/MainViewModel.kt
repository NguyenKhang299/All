package com.photo.image.picture.tools.compressor.persentation.main

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.photo.image.picture.tools.compressor.action.ActionImage
import com.photo.image.picture.tools.compressor.base.BaseViewModel
import com.photo.image.picture.tools.compressor.domain.model.OptionImage
import com.photo.image.picture.tools.compressor.utils.PermUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : BaseViewModel() {
    private val _openDrawerLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val openDrawerLiveData: LiveData<Boolean> = _openDrawerLiveData

    fun openDrawer() {
        _openDrawerLiveData.postValue(true)
    }

    interface MainFragmentEvent {
        fun clickActionCompress(actionImage: ActionImage)
    }
}