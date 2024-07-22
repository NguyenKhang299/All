package com.tearas.myapplication.presentation.files_duplicated

import com.tearas.myapplication.domain.model.MediaModel

sealed class FilesDuplicatedAction {
    data object ScanFilesDuplicated : FilesDuplicatedAction()
    data object DeleteFile : FilesDuplicatedAction()
    data class UpdateMediaClick(val idSet: String, val media: MediaModel) : FilesDuplicatedAction()
    data class UpdateSetClick(val idSet: String, val isSelected: Boolean) : FilesDuplicatedAction()
}