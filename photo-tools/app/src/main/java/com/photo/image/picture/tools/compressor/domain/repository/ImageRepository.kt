package com.photo.image.picture.tools.compressor.domain.repository

 import com.photo.image.picture.tools.compressor.action.CompressionQuantity
 import com.photo.image.picture.tools.compressor.domain.model.FolderImage
import com.photo.image.picture.tools.compressor.domain.model.Image


interface ImageRepository {
    suspend fun saveImages(imageEntity: Image, quantity: CompressionQuantity): Image?
    suspend fun getImagesSave(): List<Image>
    suspend fun getImages(): List<Image>
    suspend fun getBucketId(): List<Int>
    suspend fun getImagesByIdFolder(id: Int, isFirstImage: Boolean): List<Image>
    suspend fun getFolderImages(): List<FolderImage>
    suspend fun countImagesInFolder(folderId: Int): Int
    suspend fun queryImages(
        selection: String? = null,
        selectionArgs: Array<String>? = null,
        isFirstImage: Boolean = false
    ): List<Image>
}