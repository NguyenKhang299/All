package com.photo.imagecompressor.tools.usecase.images

  import com.photo.imagecompressor.tools.data.repository.ImageRepositoryImpl
 import javax.inject.Inject

class GetAllImagesUseCase @Inject constructor(
    private val imageRepoImpl: ImageRepositoryImpl
) {
    suspend operator fun invoke() = imageRepoImpl.getImages()
}
