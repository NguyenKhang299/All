package com.photo.image.picture.tools.compressor.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("UNCHECKED_CAST")
inline fun <VM : ViewModel> viewModelFactory(crossinline function: () -> VM) =
    object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T = function() as T
    }
