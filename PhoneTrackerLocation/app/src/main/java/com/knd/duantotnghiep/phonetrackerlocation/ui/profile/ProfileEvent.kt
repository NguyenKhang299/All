package com.knd.duantotnghiep.phonetrackerlocation.ui.profile

sealed class ProfileEvent {
    object ClickMenuEdit:ProfileEvent()
    object ClickMenuCancel:ProfileEvent()
    object ClickMenuSave:ProfileEvent()
    object ClickMenuHome:ProfileEvent()
}