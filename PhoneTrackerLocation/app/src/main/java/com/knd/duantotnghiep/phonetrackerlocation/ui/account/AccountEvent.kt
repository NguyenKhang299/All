package com.knd.duantotnghiep.phonetrackerlocation.ui.account

sealed class AccountEvent {
    object ClickSignInGoogle :AccountEvent()
    object ClickSignInFacebook :AccountEvent()
    object ClickSignInPassword :AccountEvent()

}