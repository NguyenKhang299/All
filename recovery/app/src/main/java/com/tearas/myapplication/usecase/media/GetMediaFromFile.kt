package com.tearas.myapplication.usecase.media

import com.tearas.myapplication.data.local.MediaRepositoryImpl
import com.tearas.myapplication.domain.model.MediaType

class GetMediaFromFile(private val mediaRepositoryImpl: MediaRepositoryImpl) {
    suspend operator fun invoke(path: String, type: MediaType) =
        mediaRepositoryImpl.getMediaFromFolder(path, listOf(type))
}