package com.photo.imagecompressor.tools.usecase.base

import com.google.android.datatransport.runtime.BuildConfig


abstract class BaseUseCase {
    open fun doOnError(throwable: Throwable) {
        if (BuildConfig.DEBUG) {
            throwable.printStackTrace()
        }
    }
}