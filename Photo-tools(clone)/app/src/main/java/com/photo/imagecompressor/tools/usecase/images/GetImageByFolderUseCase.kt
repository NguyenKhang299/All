package com.photo.imagecompressor.tools.usecase.images

  import com.photo.imagecompressor.tools.data.repository.ImageRepositoryImpl
 import javax.inject.Inject

class GetImageByFolderUseCase @Inject constructor(
    private val imageRepoImpl: ImageRepositoryImpl
) {
    suspend operator fun invoke(id: Int) = imageRepoImpl.getImagesByIdFolder(id,false)
}