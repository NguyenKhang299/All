package com.tearas.myapplication.usecase.media

import com.tearas.myapplication.data.local.MediaRepositoryImpl
import com.tearas.myapplication.domain.model.MediaType

class GetFilesRecoverSaved(private val mediaRepositoryImpl: MediaRepositoryImpl) {
    suspend operator fun invoke() =
        mediaRepositoryImpl.getFilesRecoverSaved()
}