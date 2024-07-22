package com.tearas.myapplication.usecase.media

import com.tearas.myapplication.data.local.MediaRepositoryImpl
import com.tearas.myapplication.domain.model.MediaModel

class SweepFilesJunk(private val mediaRepositoryImpl: MediaRepositoryImpl) {
    suspend operator fun invoke(onScan: suspend (MediaModel) -> Unit) =
        mediaRepositoryImpl.sweepFilesJunk(onScan)
}