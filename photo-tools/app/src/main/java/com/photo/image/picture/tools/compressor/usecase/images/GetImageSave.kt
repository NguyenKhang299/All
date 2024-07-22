package com.photo.image.picture.tools.compressor.usecase.images

import com.photo.image.picture.tools.compressor.data.repository.ImageRepositoryImpl
import javax.inject.Inject


class GetImageSave @Inject constructor(
    private val imageRepoImpl: ImageRepositoryImpl
) {
    suspend operator fun invoke() = imageRepoImpl.getImagesSave()
}