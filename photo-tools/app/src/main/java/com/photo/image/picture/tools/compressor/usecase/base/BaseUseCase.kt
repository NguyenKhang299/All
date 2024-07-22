package com.photo.image.picture.tools.compressor.usecase.base

 import com.photo.image.picture.tools.compressor.BuildConfig

abstract class BaseUseCase {
    open fun doOnError(throwable: Throwable) {
        if (BuildConfig.DEBUG) {
            throwable.printStackTrace()
        }
    }
}