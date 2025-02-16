package com.video.mini.tools.zip.compress.convert.simple.tiny.utils

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.media.MediaScannerConnection
import android.os.Build
import android.os.Environment
import android.os.storage.StorageManager
import android.provider.MediaStore
import android.provider.MediaStore.Video.Media
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import com.arthenica.ffmpegkit.FFprobeKit
import com.arthenica.ffmpegkit.MediaInformation
import com.video.mini.tools.zip.compress.convert.simple.tiny.model.FolderInfo
import com.video.mini.tools.zip.compress.convert.simple.tiny.model.MediaInfo
import com.video.mini.tools.zip.compress.convert.simple.tiny.model.Resolution
import com.video.mini.tools.zip.compress.convert.simple.tiny.model.isLandscapeVideo
import com.video.mini.tools.zip.compress.convert.simple.tiny.model.isPortraitVideo
import java.io.File
import java.io.FileInputStream
import java.util.Locale


interface IVideo {

    fun getPathStorageExternal(): String
    fun getVideoByIdBucket(
        id: String, orderBy: String = Media.DATE_ADDED + " DESC"
    ): ArrayList<MediaInfo>

    fun getAllVideo(
        orderBy: String = Media.DATE_ADDED + " DESC"
    ): ArrayList<MediaInfo>

    fun getVideo(
        selection: String? = null,
        selectionArg: Array<String>? = null,
        orderBy: String = Media.DATE_ADDED + " DESC"
    ): ArrayList<MediaInfo>

    fun getVideoByPath(name: String): MediaInfo?
    fun getVideoSave(): ArrayList<MediaInfo>
    fun getAudioSave(): ArrayList<MediaInfo>
    fun getVideoUnSave(): ArrayList<MediaInfo>
    fun getFolderContainVideo(orderBy: String = Media.DATE_ADDED + " DESC"): ArrayList<FolderInfo>
    fun countVideoInFolder(id: String): Int
    fun compareQuantity(quantity: Int): Boolean
    fun getPathExternalFolderVideo(): String
    fun createFolderVideo(): String
    fun createFolderAudio(): String
    fun getPathVideoCacheFolder(): String
    fun getPathExternalFolderAudio(): String
    fun createFolder(nameFolder: String, isVideo: Boolean): String
    fun replaceFiles(pathInput: String, pathOutput: String): File?
    fun getPathAudioCacheFolder(): String
}

class HandleMediaVideo(private val context: Context) : IVideo {
    override fun replaceFiles(pathInput: String, pathOutput: String): File? {
        val fileOriginal = File(getPathExternalFolderVideo() + "/" + File(pathOutput).name)
        val fileInputStream = FileInputStream(pathOutput)
        return try {
            fileInputStream.use { input ->
                fileOriginal.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            exportMp4ToGallery(context, fileOriginal.path)
            Toast.makeText(context, "Replace success", Toast.LENGTH_SHORT).show()
            fileOriginal
        } catch (e: Exception) {
            Toast.makeText(context, "Replace error", Toast.LENGTH_SHORT).show()
            null
        }
    }

    @RequiresApi(Build.VERSION_CODES.FROYO)
    private fun exportMp4ToGallery(context: Context, filePath: String) {
        val file = File(filePath)
        MediaScannerConnection.scanFile(
            context, arrayOf(file.toString()),
            null, null
        )
    }

    fun saveFileToExternalStorage(
        context: Context, isVideo: Boolean, inputStream: FileInputStream, fileName: String
    ): File? {
        val folder = File(
            if (isVideo) getPathExternalFolderVideo()
            else getPathExternalFolderAudio()
        )
        val fileSave = File(folder, fileName)

        return try {
            if (fileSave.exists()) {
                Toast.makeText(context, "File exists", Toast.LENGTH_SHORT).show()
                return fileSave
            }
//            val outputStream = FileOutputStream(fileSave)
//            val buffer = ByteArray(1024)
//            var bytesRead: Int = 0
//            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
//                outputStream.write(buffer, 0, bytesRead)
//            }
            inputStream.use { input ->
                fileSave.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            inputStream.close()
            exportMp4ToGallery(context, fileSave.path)
            Toast.makeText(context, "Save Successfully", Toast.LENGTH_SHORT).show()
            return fileSave
        } catch (e: Exception) {
            Toast.makeText(context, "Error when save", Toast.LENGTH_SHORT).show()
            return null
        }

    }


    override fun getPathVideoCacheFolder(): String {
        val file = File(context.cacheDir.path, "Videos")
        if (file.exists() || file.mkdirs()) return file.path
        return ""
    }

    override fun getPathAudioCacheFolder(): String {
        val file = File(context.cacheDir.path, "Audio")
        if (file.exists() || file.mkdirs()) return file.path
        return ""
    }

    override fun getPathExternalFolderVideo(): String {
        return createFolderVideo()
    }

    override fun getPathExternalFolderAudio(): String {
        return createFolderAudio()
    }

    override fun createFolderAudio(): String {
        return createFolder("TerasResizeAudio", false)
    }

    override fun createFolderVideo(): String {
        return createFolder("TerasResizeVideo", true)
    }

    override fun createFolder(nameFolder: String, isVideo: Boolean): String {
        val path = getPathStorageExternal() + if (isVideo) "/Movies" else "/Music"
        val folder = File(path, nameFolder)
        return if (folder.exists() || folder.mkdirs()) {
            folder.path
        } else {
            throw IllegalStateException("Unable to create directory $nameFolder")
        }
    }


    override fun getPathStorageExternal(): String {
        val storeMediaMng = context.getSystemService(Context.STORAGE_SERVICE) as StorageManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            storeMediaMng.storageVolumes[0].directory!!.path
        } else {
            Environment.getExternalStorageDirectory().path
        }
    }

    override fun getVideoByIdBucket(id: String, orderBy: String): ArrayList<MediaInfo> {
        val selection = Media.BUCKET_ID + " = ? "
        val selectionArg = arrayOf(id)
        return getVideo(selection, selectionArg, orderBy)
    }

    @SuppressLint("Recycle", "Range")
    override fun getAllVideo(orderBy: String): ArrayList<MediaInfo> {
        return getVideo(null, null, orderBy)
    }

    @SuppressLint("Range", "Recycle")
    override fun getVideo(
        selection: String?, selectionArg: Array<String>?, orderBy: String
    ): ArrayList<MediaInfo> {
        val videos = ArrayList<MediaInfo>()
        val contentResolver = context.contentResolver
        var projection =
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
                arrayOf(
                    Media._ID,
                    Media.BUCKET_ID,
                    Media.DISPLAY_NAME,
                    Media.DATA,
                    Media.SIZE,
                    Media.WIDTH,
                    Media.HEIGHT,
                    Media.DURATION,
                    Media.MIME_TYPE,
                    Media.DATE_ADDED,
                    Media.ORIENTATION,
                    Media.RESOLUTION
                )
            } else {
                arrayOf(
                    Media._ID,
                    Media.BUCKET_ID,
                    Media.DISPLAY_NAME,
                    Media.DATA,
                    Media.SIZE,
                    Media.WIDTH,
                    Media.HEIGHT,
                    Media.DURATION,
                    Media.MIME_TYPE,
                    Media.BITRATE,
                    Media.DATE_ADDED,
                    Media.ORIENTATION,
                    Media.RESOLUTION
                )
            }
        val cursor = contentResolver.query(
            Media.EXTERNAL_CONTENT_URI, projection, selection, selectionArg, orderBy
        )
        cursor?.let { cursorNonNull ->
            while (cursorNonNull.moveToNext()) {
                cursorNonNull.apply {
                    val resolutionString = getString(getColumnIndex(Media.RESOLUTION))
                    if (resolutionString != null) {
                        var resolutionSplit = resolutionString.split("×")
                        if (resolutionSplit.size != 2) resolutionSplit = resolutionString.split("x")
                        val width = resolutionSplit[0].toInt()
                        val height = resolutionSplit[1].toInt()

                        val resolution =
                            if (getInt(getColumnIndex(Media.ORIENTATION)) == 0 && width > height) {
                                Resolution(
                                    maxOf(width, height),
                                    minOf(width, height)
                                )
                            } else {
                                Resolution(
                                    minOf(width, height),
                                    maxOf(width, height)
                                )
                            }

                        val formattedTime =
                            Utils.formatTime(getLong(getColumnIndex(Media.DURATION)))

                        val mediaInfo = MediaInfo(
                            getLong(getColumnIndex(Media._ID)),
                            getString(getColumnIndex(Media.DISPLAY_NAME)),
                            getString(getColumnIndex(Media.DATA)),
                            getLong(getColumnIndex(Media.SIZE)),
                            resolution,
                            formattedTime,
                            getString(getColumnIndex(Media.MIME_TYPE)).split("/")[1],
                            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) 0L else getLong(
                                getColumnIndex(Media.BITRATE)
                            ),
                            date = Utils.formatDate(getLong(getColumnIndex(Media.DATE_ADDED)) * 1000),
                            true
                        )
                        if (formattedTime != "00:00:00" && mediaInfo.mime.lowercase(Locale.ROOT) == "mp4") {
                            videos.add(mediaInfo)
                        }
                    }
                }
            }
        }

        return videos
    }

    override fun getVideoByPath(name: String): MediaInfo? {
        val selection = "${Media.DATA} like ?"
        val selectionArg = arrayOf("$name")
        return if (getVideo(selection, selectionArg).isNotEmpty()) getVideo(
            selection,
            selectionArg
        )[0] else null
    }

    override fun getVideoSave(): ArrayList<MediaInfo> {
        val path = File(getPathExternalFolderVideo())
        val selection = "${Media.DATA} like ?"
        val selectionArg = arrayOf("$path%")
        return getVideo(selection, selectionArg)
    }

    override fun getAudioSave(): ArrayList<MediaInfo> {
        val path = File(getPathExternalFolderAudio())
        val selection = "${Media.DATA} like ?"
        val selectionArg = arrayOf("$path%")
        return getVideo(selection, selectionArg)
    }

    private fun createMediaInfo(
        mediaInfo: MediaInformation,
        executionId: Long,
    ): MediaInfo {
        val filename = mediaInfo.filename.substringAfterLast("/")
        val size = mediaInfo.size.toLong()
        val bitrate = mediaInfo.bitrate.toFloat().toLong()

        val resolution =
            Resolution(mediaInfo.streams[0].width.toInt(), mediaInfo.streams[0].height.toInt())
        val durationFormatted = Utils.formatTime(mediaInfo.duration.toFloat().toLong() * 1000)
        val extension = mediaInfo.filename.substringAfterLast(".")

        return MediaInfo(
            executionId,
            filename,
            mediaInfo.filename,
            size,
            resolution,
            durationFormatted,
            extension,
            bitrate,
            Utils.formatDate(System.currentTimeMillis()),
        )
    }

    override fun getVideoUnSave(): ArrayList<MediaInfo> {
        val cacheDir = File(getPathVideoCacheFolder())
        val videos = ArrayList<MediaInfo>()
        cacheDir.listFiles()?.forEachIndexed { index, file ->
            val formatTime = Utils.formatTime(file.length())
            try {
                val fFmpegKit = FFprobeKit.getMediaInformation(file.path).mediaInformation
                val mediaInfo = createMediaInfo(fFmpegKit, index.toLong())
                if (formatTime != "00:00:00") {
                    videos.add(mediaInfo)
                }
            } catch (e: Exception) {

            }
        }
        return videos
    }

    @SuppressLint("Range", "Recycle")
    override fun getFolderContainVideo(orderBy: String): ArrayList<FolderInfo> {
        val folders = ArrayList<FolderInfo>()
        val contentResolver = context.contentResolver
        val projection = arrayOf(
            Media.BUCKET_ID, Media.BUCKET_DISPLAY_NAME, Media.SIZE
        )
        val cursor = contentResolver.query(
            Media.EXTERNAL_CONTENT_URI, projection, null, null, orderBy
        )
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val id = cursor.getString(cursor.getColumnIndex(Media.BUCKET_ID))
                if (folders.none { id == it.id }) {
                    val resolution = FolderInfo(
                        id,
                        cursor.getString(cursor.getColumnIndex(Media.BUCKET_DISPLAY_NAME)),
                        countVideoInFolder(id).toString(),
                    )
                    folders.add(resolution)
                }
            }
        }
        return folders
    }

    override fun countVideoInFolder(id: String): Int {
        return getVideoByIdBucket(id).size
    }

    override fun compareQuantity(quantity: Int): Boolean {
        return quantity < getAllVideo().size
    }
}