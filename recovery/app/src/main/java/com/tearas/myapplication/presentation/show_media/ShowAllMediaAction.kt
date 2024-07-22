package com.tearas.myapplication.presentation.show_media

import com.tearas.myapplication.domain.model.MediaModel
import com.tearas.myapplication.domain.model.OptionEnum
import com.tearas.myapplication.presentation.show_media.component.OptionsSort

sealed class ShowAllMediaAction {

    data class UpdateItemSelected(val mediaModel: MediaModel) : ShowAllMediaAction()
    data object Recovery : ShowAllMediaAction()
    data object Delete : ShowAllMediaAction()
    data class SortMedia(val option: OptionsSort) : ShowAllMediaAction()
}