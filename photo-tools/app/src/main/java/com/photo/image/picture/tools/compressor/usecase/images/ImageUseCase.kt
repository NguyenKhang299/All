package com.photo.image.picture.tools.compressor.usecase.images

data class ImageUseCase(
    val getAllImagesUseCase: GetAllImagesUseCase,
    val getImageSave: GetImageSave,
    val getFolderImagesUseCase: GetFolderImagesUseCase,
    val saveImagesUseCase: SaveImagesUseCase,
    val getImageByFolderUseCase: GetImageByFolderUseCase
)