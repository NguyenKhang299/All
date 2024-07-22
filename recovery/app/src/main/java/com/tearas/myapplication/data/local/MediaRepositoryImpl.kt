package com.tearas.myapplication.data.local;

import android.content.ContentValues
import android.content.Context
import android.media.MediaScannerConnection
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import com.tearas.myapplication.domain.model.FolderMediaMap
import com.tearas.myapplication.domain.model.FolderModel
import com.tearas.myapplication.domain.model.MediaModel
import com.tearas.myapplication.domain.model.MediaType
import com.tearas.myapplication.domain.model.SetModel
import com.tearas.myapplication.domain.model.getApkInfo
import com.tearas.myapplication.domain.repository.IEventDeleteFiles
import com.tearas.myapplication.domain.repository.IMediaRepository
import com.tearas.myapplication.dto.FolderMediaDTO
import com.tearas.myapplication.dto.FolderCleaningDTO
import com.tearas.myapplication.utils.Utils
import com.tearas.myapplication.utils.Utils.resultFormat
import com.tearas.myapplication.utils.Utils.types
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

class MediaRepositoryImpl(private val context: Context, private val directorySave: String) :
    IMediaRepository {
    private val fileStorage = Environment.getExternalStorageDirectory()
    private val folderParent: List<FolderModel>
        get() {
            val files = fileStorage.listFiles() ?: arrayOf<File>()
            return files.mapNotNull { fileItem ->
                if (fileItem.isDirectory) FolderModel(
                    id = fileItem.name.hashCode().toString(),
                    name = fileItem.name ?: "",
                    path = fileItem.path ?: "",
                ) else null
            }.toMutableList().apply {
                add(
                    FolderModel(
                        id = fileStorage.hashCode().toString(),
                        name = fileStorage.name ?: "",
                        path = fileStorage.path ?: "",
                    )
                )
            }
        }


    override suspend fun scannerAllImages(onScan: suspend (MediaModel) -> Unit): Map<FolderModel, List<MediaModel>> {
        return collectMedia(listOf(MediaType.IMAGE), folderParent, onScan)
    }

    override suspend fun scannerAllVideo(onScan: suspend (MediaModel) -> Unit): Map<FolderModel, List<MediaModel>> {
        return collectMedia(listOf(MediaType.VIDEO), folderParent, onScan)
    }

    override suspend fun scannerAllAudio(onScan: suspend (MediaModel) -> Unit): Map<FolderModel, List<MediaModel>> {
        return collectMedia(listOf(MediaType.AUDIO), folderParent, onScan)
    }

    override suspend fun scannerAllDocument(onScan: suspend (MediaModel) -> Unit): Map<FolderModel, List<MediaModel>> {
        return collectMedia(listOf(MediaType.FILES), folderParent, onScan)
    }

    override suspend fun scanner(
        mediaModel: MutableList<MediaModel>,
        fileParent: FolderModel,
        fileItem: File,
        onScan: (suspend (MediaModel) -> Unit)?
    ): Map<FolderModel, List<MediaModel>> {

        suspend fun handleAddFile(file: File) {
            val mime = Utils.getMimeType(file.path, context) ?: file.extension
            if (resultFormat.any { mime.startsWith(it) } || resultFormat.contains(file.extension)) {
                val media = MediaModel(
                    folder = fileParent,
                    name = file.name,
                    mime = mime,
                    path = file.path,
                    size = file.length(),
                    date = file.lastModified(),
                    typeMedia = when {
                        Utils.isApk(mime) -> MediaType.APK
                        Utils.isFileLarge(types, file.length()) -> MediaType.FILE_LARGE
                        Utils.isJunkFile(mime) -> MediaType.JUNK_FILES
                        Utils.isTemporaryFile(mime) -> MediaType.TEMPORARY_FILES
                        Utils.isAudio(mime) -> MediaType.AUDIO
                        Utils.isImage(mime) -> MediaType.IMAGE
                        Utils.isVideo(mime) -> MediaType.VIDEO
                        Utils.isFileOrDocument(mime) -> MediaType.FILES
                        else -> MediaType.FILES
                    },
                    contentHash = Utils.getFileContentHashCodes(file)
                )
                mediaModel.add(media)
                onScan?.invoke(media)
            }
        }

        fileItem.listFiles()?.let { files ->
            for (file in files) {
                if (file.isFile) {
                    handleAddFile(file)
                } else if (file.isDirectory && fileParent.path != fileStorage.path) {
                    scanner(mediaModel, fileParent, file, onScan)
                }
            }
        }
        return mediaModel.groupBy { it.folder }
    }

    override suspend fun getMediaFromFolder(
        folderPath: String, types: List<MediaType>
    ): FolderMediaMap {
        val file = File(folderPath)
        val folderModel = FolderModel(
            id = file.hashCode().toString(),
            name = file.name ?: "",
            path = file.path ?: "",
        )
        val mapMedia = collectMedia(types, listOf(folderModel)) {}

        return FolderMediaMap(folderModel, mapMedia.values.flatten())
    }

    override suspend fun scannerFilesDuplicated(onScan: (suspend (MediaModel) -> Unit)?): Map<MediaType, List<SetModel>> {
        val filesAll = collectMedia(listOf(MediaType.ALL), folderParent, onScan).values.flatten()

        val mediaGroups = filesAll.groupBy { it.typeMedia }
        val map = mutableMapOf<MediaType, List<SetModel>>()

        mediaGroups.forEach { (mediaType, mediaModels) ->
            val duplicatedFilesMap = mediaModels.groupBy { mediaModel ->
                mediaModel.contentHash
            }.filterValues { it.size > 1 }

            val listSet = duplicatedFilesMap.values.map {
                SetModel(listMedia = it)
            }
            map[mediaType] = listSet
        }

        return map
    }

    override suspend fun deleteMedia(list: List<MediaModel>, eventsDelete: IEventDeleteFiles) {
        withContext(Dispatchers.IO) {
            list.forEachIndexed { index, mediaModel ->
                try {
                    File(mediaModel.path).delete()
                } catch (e: Exception) {
                    eventsDelete.onError(e.localizedMessage ?: "Unknown error")
                } finally {
                    eventsDelete.onDelete(index, mediaModel)
                }
            }
            eventsDelete.onFinish()
        }
    }

    override suspend fun recoveryMedia(
        mediaModels: List<MediaModel>, onRecovery: suspend (Int) -> Unit
    ) {
        mediaModels.forEachIndexed { index, mediaModel ->
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, mediaModel.name)
                put(MediaStore.MediaColumns.MIME_TYPE, mediaModel.mime)
                put(MediaStore.MediaColumns.DATE_ADDED, System.currentTimeMillis())
                put(MediaStore.MediaColumns.DATA, directorySave + "/${mediaModel.name}")
            }
            val uri = MediaStore.Files.getContentUri("external")
            withContext(Dispatchers.IO) {
                val resolver = context.contentResolver
                val query = resolver.insert(uri, contentValues)
                val file = File(mediaModel.path)
                query?.let {
                    resolver.openOutputStream(query)?.use { outputStream ->
                        outputStream.write(Files.readAllBytes(Paths.get(file.path)))
                        outputStream.flush()
                        outputStream.close()
                    }
                    MediaScannerConnection.scanFile(
                        context, arrayOf(query.path), arrayOf(mediaModel.mime), null
                    )
                }

                onRecovery(index)
            }
        }
    }

    override suspend fun getFilesRecoverSaved(): Map<FolderMediaDTO, List<MediaModel>> {
        val file = File(directorySave)
        val folderModel = FolderModel(
            id = file.hashCode().toString(), name = file.name, path = file.path
        )

        val allMediaFolder =
            collectMedia(listOf(MediaType.ALL), listOf(folderModel)) {}.values.flatten()
                .groupBy { it.typeMedia }

        return Utils.createFolderMediaList(context).associateWith { dto ->
            allMediaFolder[dto.type]?.also {
                dto.count = it.size
            } ?: emptyList()
        }
    }

    override suspend fun sweepFilesJunk(onScan: suspend (MediaModel) -> Unit): Map<FolderCleaningDTO, List<MediaModel>> {
        val listType = listOf(MediaType.APK, MediaType.JUNK_FILES, MediaType.FILE_LARGE)
        Utils.setType(listType)
        val allMediaFolder = collectMedia(listType, folderParent, onScan).values.flatten()
            .sortedByDescending { it.size }.map {
                it.isSelected = true
                when (it.typeMedia) {
                    MediaType.APK -> {
                        val info = getApkInfo(context, File(it.path))
                        it.copy(appIcon = info?.appIcon)
                    }

                    else -> it
                }
            }.groupBy { it.typeMedia }
        return Utils.createFolderCleanList(context).associateWith { dto ->
            allMediaFolder[dto.type]?.also { it ->
                dto.allSize = it.sumOf { it.size }
            } ?: emptyList()
        }
    }

    override suspend fun collectMedia(
        types: List<MediaType>,
        folderParents: List<FolderModel>,
        onScan: (suspend (MediaModel) -> Unit)?
    ): MutableMap<FolderModel, List<MediaModel>> {
        val list = mutableListOf<MediaModel>()
        val mapMedia = mutableMapOf<FolderModel, List<MediaModel>>()
        Utils.setType(types)

        folderParents.forEach {
            val map = scanner(list, it, File(it.path), onScan)
            mapMedia.putAll(map)
        }
        return mapMedia
    }
}

