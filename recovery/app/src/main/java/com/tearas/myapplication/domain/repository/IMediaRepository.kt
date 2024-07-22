package com.tearas.myapplication.domain.repository

import com.tearas.myapplication.domain.model.FolderMediaMap
import com.tearas.myapplication.domain.model.FolderModel
import com.tearas.myapplication.domain.model.MediaModel
import com.tearas.myapplication.domain.model.MediaType
import com.tearas.myapplication.domain.model.SetModel
import com.tearas.myapplication.dto.FolderMediaDTO
import com.tearas.myapplication.dto.FolderCleaningDTO
import java.io.File

interface IEventDeleteFiles {
    suspend fun onDelete(index: Int, mediaModel: MediaModel)
    suspend fun onFinish()
    suspend fun onError(message: String)
}

interface IMediaRepository {
    suspend fun scannerAllImages(onScan: suspend (MediaModel) -> Unit): Map<FolderModel, List<MediaModel>>
    suspend fun scannerAllVideo(onScan: suspend (MediaModel) -> Unit): Map<FolderModel, List<MediaModel>>
    suspend fun scannerAllAudio(onScan: suspend (MediaModel) -> Unit): Map<FolderModel, List<MediaModel>>
    suspend fun scannerAllDocument(onScan: suspend (MediaModel) -> Unit): Map<FolderModel, List<MediaModel>>
    suspend fun scanner(
        mediaModel: MutableList<MediaModel>,
        fileParent: FolderModel,
        fileItem: File,
        onScan: (suspend (MediaModel) -> Unit)?
    ): Map<FolderModel, List<MediaModel>>

    suspend fun collectMedia(
        types: List<MediaType>,
        folderParents: List<FolderModel>,
        onScan: (suspend (MediaModel) -> Unit)?
    ): MutableMap<FolderModel, List<MediaModel>>

    suspend fun getMediaFromFolder(folderPath: String, types: List<MediaType>): FolderMediaMap
    suspend fun scannerFilesDuplicated(onScan: (suspend (MediaModel) -> Unit)?): Map<MediaType, List<SetModel>>
    suspend fun deleteMedia(
        list: List<MediaModel>,
        eventsDelete: IEventDeleteFiles
    )

    suspend fun recoveryMedia(mediaModels: List<MediaModel>, onRecovery: suspend (Int) -> Unit)
    suspend fun getFilesRecoverSaved(): Map<FolderMediaDTO, List<MediaModel>>
    suspend fun sweepFilesJunk(onScan: suspend (MediaModel) -> Unit): Map<FolderCleaningDTO, List<MediaModel>>
}