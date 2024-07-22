package com.tearas.myapplication.usecase.media

import com.tearas.myapplication.domain.model.MediaModel
import com.tearas.myapplication.domain.repository.IMediaRepository

class RecoveryMedia(private val mediaRepository: IMediaRepository) {
    suspend operator fun invoke(
        mediaModels: List<MediaModel>,
        onRecovery: suspend (Int) -> Unit
    ) = mediaRepository.recoveryMedia(mediaModels, onRecovery)
}