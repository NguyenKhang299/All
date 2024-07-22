package com.tearas.myapplication.usecase.media

import com.tearas.myapplication.data.local.MediaRepositoryImpl
import com.tearas.myapplication.domain.model.MediaModel
import com.tearas.myapplication.domain.repository.IEventDeleteFiles

class DeleteMedia(private val mediaRepositoryImpl: MediaRepositoryImpl) {
    suspend operator fun invoke(
        list: List<MediaModel>,
        ventsDelete: IEventDeleteFiles
    ) = mediaRepositoryImpl.deleteMedia(list, ventsDelete)
}