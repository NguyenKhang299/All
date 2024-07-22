package com.tearas.myapplication.usecase.media

import com.tearas.myapplication.data.local.MediaRepositoryImpl
import com.tearas.myapplication.domain.model.MediaModel

class ScanAllImage(private val mediaRepositoryImpl: MediaRepositoryImpl) {
    suspend operator fun invoke(onScannerFileChanged: suspend (MediaModel) -> Unit) =
        mediaRepositoryImpl.scannerAllImages(onScannerFileChanged)
}