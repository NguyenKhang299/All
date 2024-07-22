package com.photo.image.picture.tools.compressor.usecase.images

import com.photo.image.picture.tools.compressor.action.CompressionQuantity
import com.photo.image.picture.tools.compressor.data.repository.ImageRepositoryImpl
import com.photo.image.picture.tools.compressor.domain.model.Image
import javax.inject.Inject

class SaveImagesUseCase @Inject constructor(
    private val imageRepoImpl: ImageRepositoryImpl
) {
    suspend operator fun invoke(image: Image, compressionQuantity: CompressionQuantity) = imageRepoImpl.saveImages(image,compressionQuantity)
}