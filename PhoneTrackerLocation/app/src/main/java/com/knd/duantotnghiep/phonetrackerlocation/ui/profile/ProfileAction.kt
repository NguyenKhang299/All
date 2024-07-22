package com.knd.duantotnghiep.phonetrackerlocation.ui.profile

import com.knd.duantotnghiep.phonetrackerlocation.ui.main.MainViewAction
import okhttp3.MultipartBody

sealed class ProfileAction {
    object UpdateProfileUser : ProfileAction()
    object EnableEditProfile : ProfileAction()
    object DisableEditProfile : ProfileAction()
    class UpdateProfile(val rqBd: MultipartBody.Part) : ProfileAction()

}