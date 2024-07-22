package com.photo.imagecompressor.tools.domain.repository

 import com.photo.imagecompressor.tools.action.CompressionQuantity
 import com.photo.imagecompressor.tools.domain.model.Folder_Image
import com.photo.imagecompressor.tools.domain.model.Image_


interface ImageRepository {
    suspend fun saveImages(imageEntity: Image_, quantity: CompressionQuantity): Image_?
    suspend fun getImagesSave(): List<Image_>
    suspend fun getImages(): List<Image_>
    suspend fun getBucketId(): List<Int>
    suspend fun getImagesByIdFolder(id: Int, isFirstImage: Boolean): List<Image_>
    suspend fun getFolderImages(): List<Folder_Image>
    suspend fun countImagesInFolder(folderId: Int): Int
    suspend fun queryImages(
        selection: String? = null,
        selectionArgs: Array<String>? = null,
        isFirstImage: Boolean = false
    ): List<Image_>
}