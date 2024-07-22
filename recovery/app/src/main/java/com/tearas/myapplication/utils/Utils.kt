package com.tearas.myapplication.utils

import android.content.Context
import android.content.Intent
import android.media.MediaMetadataRetriever
import android.webkit.MimeTypeMap
import androidx.core.content.FileProvider
import androidx.navigation.NavHostController
import com.tearas.myapplication.R
import com.tearas.myapplication.domain.model.MediaModel
import com.tearas.myapplication.domain.model.MediaType
import com.tearas.myapplication.domain.model.OptionEnum
import com.tearas.myapplication.dto.FolderCleaningDTO
import com.tearas.myapplication.dto.FolderMediaDTO
import java.io.File
import java.io.Serializable
import java.security.MessageDigest

object Utils {
    fun createFolderCleanList(context: Context) = listOf(
        FolderCleaningDTO(
            title = context.getString(R.string.files_apk),
            icon = R.drawable.android,
            allSize = 0,
            type = MediaType.APK
        ), FolderCleaningDTO(
            title = context.getString(R.string.files_large),
            icon = R.drawable.document,
            allSize = 0,
            type = MediaType.FILE_LARGE
        ), FolderCleaningDTO(
            title = context.getString(R.string.temporary_files),
            icon = R.drawable.temporary_files,
            allSize = 0,
            type = MediaType.TEMPORARY_FILES
        ), FolderCleaningDTO(
            title = context.getString(R.string.junk_files),
            icon = R.drawable.junk_files,
            allSize = 0,
            type = MediaType.JUNK_FILES
        )
    )

    fun createFolderMediaList(context: Context) = listOf(
        FolderMediaDTO(
            title = context.getString(R.string.images),
            icon = R.drawable.image,
            count = 0,
            type = MediaType.IMAGE
        ), FolderMediaDTO(
            title = context.getString(R.string.videos),
            icon = R.drawable.video,
            count = 0,
            type = MediaType.VIDEO
        ), FolderMediaDTO(
            title = context.getString(R.string.audios),
            icon = R.drawable.audio,
            count = 0,
            type = MediaType.AUDIO
        ), FolderMediaDTO(
            title = context.getString(R.string.documents),
            icon = R.drawable.document,
            count = 0,
            type = MediaType.FILES
        ), FolderMediaDTO(
            title = "Apk's",
            icon = R.drawable.android,
            count = 0,
            type = MediaType.APK
        )
    )

    fun createFolderCleaningMap(context: Context) = mapOf(
        FolderCleaningDTO(
            title = context.getString(R.string.files_apk),
            icon = R.drawable.android,
            allSize = 0,
            type = MediaType.APK
        ) to emptyList(),
        FolderCleaningDTO(
            title = context.getString(R.string.files_large),
            icon = R.drawable.document,
            allSize = 0,
            type = MediaType.FILE_LARGE
        ) to emptyList<MediaModel>(),
        FolderCleaningDTO(
            title = context.getString(R.string.temporary_files),
            icon = R.drawable.temporary_files,
            allSize = 0,
            type = MediaType.TEMPORARY_FILES
        ) to emptyList(),
        FolderCleaningDTO(
            title = context.getString(R.string.junk_files),
            icon = R.drawable.junk_files,
            allSize = 0,
            type = MediaType.JUNK_FILES
        ) to emptyList(),
    )


    val videoFormats = listOf("video")
    val imageFormats = listOf("image")
    val audioFormats = listOf("audio")
    val filesFormats = listOf("application")
    var resultFormat = mutableListOf<String>()
    val apkFormats = listOf("application/vnd.android.package-archive")
    val temporaryFiles = listOf(
        "tmp",
        "bak",
        "old",
        "dmp",
        "chk",
        "gid",
        "~*",
        "part",
        "crdownload",
        "download",
        "ds_store",
        "_*",
        "thumb",
        "sqlite",
        "journal",
        "diff",
        "swo",
        "swp",
        "bak",
        "old",
        "save",
        "err",
        "dump",
        "ilk",
        "log1",
        "log2",
        "repx",
        "wrk",
        "temp"
    )

    val fileRac = listOf(
        "log",
        "cache"
    )
    val types = mutableListOf<MediaType>()

    fun setType(types: List<MediaType>) {
        this.types.clear()
        resultFormat.clear()
        this.types.addAll(types)
        types.forEach { type ->
            resultFormat.addAll(
                when (type) {
                    MediaType.TEMPORARY_FILES -> temporaryFiles.toList()
                    MediaType.JUNK_FILES -> fileRac.toList()
                    MediaType.APK -> apkFormats.toList()
                    MediaType.VIDEO -> videoFormats.toList()
                    MediaType.AUDIO -> audioFormats.toList()
                    MediaType.FILES -> filesFormats.toList()
                    MediaType.IMAGE -> imageFormats.toList()
                    MediaType.FILE_LARGE -> mutableListOf<String>().apply {
                        addAll(audioFormats)
                        addAll(filesFormats)
                    }

                    MediaType.ALL -> {
                        mutableListOf<String>().apply {
                            addAll(videoFormats)
                            addAll(audioFormats)
                            addAll(filesFormats)
                            addAll(imageFormats)
                        }
                    }
                }
            )
        }
    }

    fun isTemporaryFile(extension: String): Boolean {
        return extension in temporaryFiles
    }

    fun isJunkFile(extension: String): Boolean {
        return extension in fileRac
    }

    fun isFileLarge(types: List<MediaType>, size: Long): Boolean {
        return MediaType.FILE_LARGE in types && size > 10 * 1024 * 1024
    }

    fun isApk(mime: String): Boolean {
        return mime in apkFormats
    }

    fun isVideo(mime: String): Boolean {
        return videoFormats.any { mime.startsWith(it) }
    }

    fun isImage(mime: String): Boolean {
        return imageFormats.any { mime.startsWith(it) }
    }

    fun isAudio(mime: String): Boolean {
        return audioFormats.any { mime.startsWith(it) }
    }

    fun isFileOrDocument(mime: String): Boolean {
        return filesFormats.any { mime.startsWith(it) }
    }

    fun getMimeType(url: String, context: Context): String? {
        val file = File(url)
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(file.extension)
    }

    fun getFileContentHashCodes(file: File): Int {
        val md = MessageDigest.getInstance("SHA-256")
        file.inputStream().use { inputStream ->
            val bufferSize = 8192
            val buffer = ByteArray(bufferSize)
            var bytesRead: Int
            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                md.update(buffer, 0, bytesRead)
            }
            inputStream.close()
        }
        return md.digest().contentHashCode()
    }

    fun getTimeVideo(file: File?): Long {
        val retriever = MediaMetadataRetriever()
        return try {
            if (file != null) retriever.setDataSource(file.absolutePath)
            val time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
            time?.toLong() ?: -1
        } catch (e: Exception) {
            -1
        } finally {
            retriever.release()
        }
    }


    fun calculateAngle(index: Int, total: Int) = ((index + 1) / total.toFloat()) * 360
}

fun Context.shareMedia(path: String) {
    val uri = FileProvider.getUriForFile(
        this,
        "com.tearas.myapplication.provider",
        File(path)
    )
    val intent = Intent(Intent.ACTION_SEND).apply {
        putExtra(Intent.EXTRA_STREAM, uri)
        flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
    }
    startActivity(Intent.createChooser(intent, "Share Media"))
}

fun OptionEnum.getTypeMedia() = when (this) {
    OptionEnum.SCANNER_AUDIO -> MediaType.AUDIO
    OptionEnum.SCANNER_VIDEO -> MediaType.VIDEO
    OptionEnum.SCANNER_IMAGE -> MediaType.IMAGE
    OptionEnum.SCANNER_DOCUMENT -> MediaType.FILES
    else -> MediaType.FILES
}

fun <O> NavHostController.setObject(name: String, o: O) {
    currentBackStackEntry?.savedStateHandle?.set(name, o)
}

fun <O> NavHostController.getObject(name: String): O? {
    return previousBackStackEntry?.savedStateHandle?.get<O>(name)
}
