package com.photo.imagecompressor.tools.usecase.images

data class ImageUseCase(
    val getAllImagesUseCase: GetAllImagesUseCase,
    val getImageSave: GetImageSave,
    val getFolderImagesUseCase: GetFolderImagesUseCase,
    val saveImagesUseCase: SaveImagesUseCase,
    val getImageByFolderUseCase: GetImageByFolderUseCase
)