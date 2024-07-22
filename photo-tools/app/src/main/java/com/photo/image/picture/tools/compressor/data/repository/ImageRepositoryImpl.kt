package com.photo.image.picture.tools.compressor.data.repository

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources.NotFoundException
import android.graphics.Bitmap
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import com.photo.image.picture.tools.compressor.action.CompressionQuantity
import com.photo.image.picture.tools.compressor.di.FileDirectory
import com.photo.image.picture.tools.compressor.domain.model.FolderImage
import com.photo.image.picture.tools.compressor.domain.model.Image
import com.photo.image.picture.tools.compressor.domain.model.ImageType
import com.photo.image.picture.tools.compressor.domain.model.Resolution
import com.photo.image.picture.tools.compressor.domain.repository.ImageRepository
import com.photo.image.picture.tools.compressor.utils.FileUtils
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileOutputStream
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    @FileDirectory val directory: File,
) : ImageRepository {

    private val contentResolver = context.contentResolver
    private val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    override suspend fun saveImages(
        imageEntity: Image,
        quantity: CompressionQuantity
    ): Image? {
        return try {
            val fileSave =
                File(
                    directory.path + "/IMG_TERAS_${UUID.randomUUID()}.${
                        imageEntity.mime.toString().lowercase()
                    }"
                )
            val fileOutputStream = FileOutputStream(fileSave)
            val bitmap = imageEntity.bitmap ?: return null
            bitmap.compress(
                when (imageEntity.mime) {
                    ImageType.PNG -> Bitmap.CompressFormat.PNG
                    ImageType.JPEG -> Bitmap.CompressFormat.JPEG
                    ImageType.WEBP -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        Bitmap.CompressFormat.WEBP_LOSSY
                    } else {
                        Bitmap.CompressFormat.WEBP
                    }

                    else -> Bitmap.CompressFormat.JPEG
                }, quantity.quantityDefault, fileOutputStream
            )
            fileOutputStream.close()
            FileUtils.exportImageToGallery(context, fileSave.path)
            imageEntity.copy(path = fileSave.path, size = fileSave.readBytes().size.toLong())
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun getImagesSave(): List<Image> {
        val selection = "${MediaStore.Images.Media.DATA} like ? "
        val selectionArgs = arrayOf("${directory.path}%")
        return queryImages(selection, selectionArgs)
    }

    override suspend fun getImages(): List<Image> {
        return queryImages()
    }

    @SuppressLint("Range")
    override suspend fun getBucketId(): List<Int> {
        val listFolderImage = mutableListOf<Int>()
        val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} ASC"
        val cursor = contentResolver.query(
            uri,
            arrayOf(MediaStore.Images.Media.BUCKET_ID),
            null,
            null,
            sortOrder
        )
        cursor?.use { cursor ->
            while (cursor.moveToNext()) {
                val id = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID))
                if (listFolderImage.none { id == it }) {
                    listFolderImage.add(id)
                }
            }
        }
        cursor?.close();
        return listFolderImage
    }

    override suspend fun getImagesByIdFolder(id: Int, isFirstImage: Boolean): List<Image> {
        val selection = "${MediaStore.Images.Media.BUCKET_ID} = ?"
        val selectionArgs = arrayOf(id.toString())
        return queryImages(selection, selectionArgs, isFirstImage)
    }

    override suspend fun countImagesInFolder(folderId: Int): Int {
        val selection = "${MediaStore.Images.Media.BUCKET_ID} = ?"
        val selectionArgs = arrayOf(folderId.toString())

        val cursor = contentResolver.query(uri, null, selection, selectionArgs, null)
        cursor?.use { cursor ->
            return cursor.count
        }
        return 0
    }

    @SuppressLint("Range", "Recycle")
    override suspend fun getFolderImages(): List<FolderImage> {
        val listFolderImage = mutableListOf<FolderImage>()
        val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} ASC"
        val projection = arrayOf(
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.SIZE
        )
        val bucketIds = getBucketId()

        bucketIds.forEach { bucketId ->
            val selection = "${MediaStore.Images.Media.BUCKET_ID} = $bucketId"
            val cursor = contentResolver.query(uri, projection, selection, null, sortOrder)
            cursor?.use { cursor ->
                while (cursor.moveToNext()) {
                    val id = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID))
                    val name =
                        cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME))
                    val totalImage = getImagesByIdFolder(id, true)
                    val folder = FolderImage(
                        id,
                        name,
                        countImagesInFolder(id),
                        if (totalImage.isNotEmpty()) totalImage[0].path else null,
                    )
                    listFolderImage.add(folder)
                    break
                }
                cursor.close()
            }
        }
        return listFolderImage
    }

    @SuppressLint("Recycle", "Range")
    override suspend fun queryImages(
        selection: String?,
        selectionArgs: Array<String>?, isFirstImage: Boolean
    ): List<Image> {
        val imageList: MutableList<Image> = mutableListOf()
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.WIDTH,
            MediaStore.Images.Media.HEIGHT,
            MediaStore.Images.Media.SIZE,
            MediaStore.Images.Media.MIME_TYPE,
            MediaStore.Images.Media.DATE_ADDED
        )

        val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"

        val cursor = contentResolver.query(uri, projection, selection, selectionArgs, sortOrder)
        cursor?.use { cursor ->
            while (cursor.moveToNext()) {
                val id = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media._ID))
                val name =
                    cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME))
                val path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
                val resolution = Resolution(
                    cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media.WIDTH)),
                    cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media.HEIGHT))
                )
                val size = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.SIZE))
                val mime =
                    cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.MIME_TYPE))
                        .let { it.split("/")[1] }
                val dateAdd =
                    cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED))
                val image = Image(
                    id,
                    name,
                    path,
                    resolution,
                    size,
                    FileUtils.getImageType(mime),
                    dateAdd
                )
                imageList.add(image)
                if (isFirstImage) break
            }
        }
        return imageList
    }
}