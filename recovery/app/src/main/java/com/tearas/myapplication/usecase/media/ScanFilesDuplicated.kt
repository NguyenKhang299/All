package com.tearas.myapplication.usecase.media

import com.tearas.myapplication.data.local.MediaRepositoryImpl
import com.tearas.myapplication.domain.model.MediaModel

class ScanFilesDuplicated(private val mediaRepositoryImpl: MediaRepositoryImpl) {
    suspend operator fun invoke(onScannerFileChanged: (MediaModel) -> Unit) =
        mediaRepositoryImpl.scannerFilesDuplicated(onScannerFileChanged)
}