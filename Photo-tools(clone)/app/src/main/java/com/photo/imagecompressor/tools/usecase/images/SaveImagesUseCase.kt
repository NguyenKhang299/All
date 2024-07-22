package com.photo.imagecompressor.tools.usecase.images

import com.photo.imagecompressor.tools.action.CompressionQuantity
import com.photo.imagecompressor.tools.data.repository.ImageRepositoryImpl
import com.photo.imagecompressor.tools.domain.model.Image_
import javax.inject.Inject

class SaveImagesUseCase @Inject constructor(
    private val imageRepoImpl: ImageRepositoryImpl
) {
    suspend operator fun invoke(image: Image_, compressionQuantity: CompressionQuantity) = imageRepoImpl.saveImages(image,compressionQuantity)
}