package com.photo.imagecompressor.tools.data.repository

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import com.photo.imagecompressor.tools.di.FileDirectory
import com.photo.imagecompressor.tools.domain.model.Folder_Image
import com.photo.imagecompressor.tools.domain.model.Image_
import com.photo.imagecompressor.tools.domain.model.Image_Type
import com.photo.imagecompressor.tools.domain.model.Resolution_
import com.photo.imagecompressor.tools.domain.repository.ImageRepository
import com.photo.imagecompressor.tools.utils.FileUtils
import com.photo.imagecompressor.tools.action.CompressionQuantity
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileOutputStream
import java.util.UUID
import javax.inject.Inject

class ImageRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    @FileDirectory val directory: File,
) : ImageRepository {

    private val contentResolver = context.contentResolver
    private val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    override suspend fun saveImages(
        imageEntity: Image_,
        quantity: CompressionQuantity
    ): Image_? {
        val fileSave =
            File(
                directory.path + "/${UUID.randomUUID()}.${
                    imageEntity.mime.toString().lowercase()
                }"
            )
        val fileOutputStream = FileOutputStream(fileSave)
        val bitmap = imageEntity.bitmap ?: return null
        bitmap.compress(
            when (imageEntity.mime) {
                Image_Type.PNG -> Bitmap.CompressFormat.PNG
                Image_Type.JPEG -> Bitmap.CompressFormat.JPEG
                Image_Type.WEBP -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    Bitmap.CompressFormat.WEBP_LOSSY
                } else {
                    Bitmap.CompressFormat.WEBP
                }

                else -> Bitmap.CompressFormat.JPEG
            }, mapRange(quantity.quantityDefault), fileOutputStream
        )
        fileOutputStream.flush();
        fileOutputStream.close()
        FileUtils.exportImageToGallery(context, fileSave.path)
        return imageEntity.copy(path = fileSave.path, size = fileSave.readBytes().size.toLong())
    }

    fun mapRange(
        value: Int,
        inMin: Float = 10f,
        inMax: Float = 90f,
        outMin: Float = 10f,
        outMax: Float = 50f
    ): Int {
        return (((value - inMin) * (outMax - outMin) / (inMax - inMin)) + outMin).toInt()
    }

    override suspend fun getImagesSave(): List<Image_> {
        val selection = "${MediaStore.Images.Media.DATA} like ? "
        val selectionArgs = arrayOf("${directory.path}%")
        return queryImages(selection, selectionArgs)
    }

    override suspend fun getImages(): List<Image_> {
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

    override suspend fun getImagesByIdFolder(id: Int, isFirstImage: Boolean): List<Image_> {
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
    override suspend fun getFolderImages(): List<Folder_Image> {
        val listFolderImage = mutableListOf<Folder_Image>()
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
                    try {
                        val id =
                            cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID))
                        val name =
                            cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME))
                        val totalImage = getImagesByIdFolder(id, true)
                        val folder = Folder_Image(
                            id,
                            name,
                            countImagesInFolder(id),
                            if (totalImage.isNotEmpty()) totalImage[0].path else null,
                        )
                        listFolderImage.add(folder)
                    } catch (e: Exception) {

                    }
                    break
                }
                cursor.close()
            }
        }
        return listFolderImage
    }

    fun getMimeType(url: String, context: Context): String? {
        val index = url.lastIndexOf(".")
        if (index == -1) return null
        val extension = url.substring(index)
        val mimeTypeMap = MimeTypeMap.getFileExtensionFromUrl(extension)
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(mimeTypeMap)
    }

    @SuppressLint("Recycle", "Range")
    override suspend fun queryImages(
        selection: String?,
        selectionArgs: Array<String>?, isFirstImage: Boolean
    ): List<Image_> {
        val imageList: MutableList<Image_> = mutableListOf()
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
                val resolution = Resolution_(
                    cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media.WIDTH)),
                    cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media.HEIGHT))
                )
                val size = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.SIZE))
                val mime = try {
                    cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.MIME_TYPE))
                        .let { it.split("/")[1] }
                } catch (e: Exception) {
                    ""
                }

                val dateAdd =
                    cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED))
                val image = Image_(
                    id,
                    name,
                    ContentUris.withAppendedId(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        id.toLong()
                    ).toString(),
                    resolution,
                    size,
                    FileUtils.getImageType(mime),
                    dateAdd
                )
                if (image.resolution.rH > 0 && image.resolution.rW > 0) imageList.add(image)
                if (isFirstImage) break
            }
        }
        return imageList
    }
}