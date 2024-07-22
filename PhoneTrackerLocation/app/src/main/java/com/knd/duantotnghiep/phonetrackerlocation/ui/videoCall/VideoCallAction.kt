package com.knd.duantotnghiep.phonetrackerlocation.ui.videoCall


sealed class VideoCallAction {
     data class GetTokenRtc(val _id_other:String) : VideoCallAction()
}