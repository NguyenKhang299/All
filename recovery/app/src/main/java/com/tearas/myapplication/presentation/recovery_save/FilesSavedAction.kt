package com.tearas.myapplication.presentation.recovery_save

import com.tearas.myapplication.domain.model.MediaModel

sealed class FilesSavedAction {
    data object GetFilesSaved : FilesSavedAction()
    data object DeleteFile : FilesSavedAction()
    data class UpdateMediaSelected(val mediaModel: MediaModel) : FilesSavedAction()
}

