package com.tearas.myapplication.presentation.clean_files

import com.tearas.myapplication.domain.model.MediaModel
import com.tearas.myapplication.dto.FolderCleaningDTO

sealed class CleanFilesAction {
    data object ScanFilesJunk : CleanFilesAction()
    data object DeleteFilesJunk : CleanFilesAction()
    data class UpdateSelectedMedia(val media: MediaModel) : CleanFilesAction()
    data class UpdateSelectedFolderMedia(
        val folderCleaningDTO: FolderCleaningDTO,
        val isSelected: Boolean
    ) : CleanFilesAction()
}