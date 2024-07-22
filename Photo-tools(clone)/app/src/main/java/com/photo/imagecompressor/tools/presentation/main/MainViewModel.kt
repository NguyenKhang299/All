package com.photo.imagecompressor.tools.presentation.main

import com.photo.imagecompressor.tools.base.BaseViewModel
import com.photo.imagecompressor.tools.action.ActionImage
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : BaseViewModel() {


    interface HomeFragmentEvent {
        fun clickActionCompress(actionImage: ActionImage)
    }
}