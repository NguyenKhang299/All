package com.knd.duantotnghiep.phonetrackerlocation.ui.videoCall

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.knd.duantotnghiep.phonetrackerlocation.models.MessageResponse
import com.knd.duantotnghiep.phonetrackerlocation.models.RtcResponse
import com.knd.duantotnghiep.phonetrackerlocation.models.UserResponse
import com.knd.duantotnghiep.phonetrackerlocation.respository.UserRepository
import com.knd.duantotnghiep.phonetrackerlocation.utils.NetworkResult
import com.knd.duantotnghiep.phonetrackerlocation.utils.TokenManager
import com.knd.duantotnghiep.phonetrackerlocation.utils.handleApiCall
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class VideoCallViewModel @Inject
constructor(
    private val userRepository: UserRepository,
    private val tokenManager: TokenManager
) : ViewModel() {
    val tokenRtc = MutableLiveData<String>()

    //LiveData for Get token rtc
    private val _onGetTokenRtc = MutableLiveData<NetworkResult<RtcResponse>>()
    val onGetTokenRtc: LiveData<NetworkResult<RtcResponse>> get() = _onGetTokenRtc

    fun handle(action: VideoCallAction) {
        when (action) {
            is VideoCallAction.GetTokenRtc -> handleGetTokenRtc(action._id_other)
        }
    }

    private fun handleGetTokenRtc(_id_other: String) {
        handleApiCall(_onGetTokenRtc, { userRepository.getTokenRtc(_id_other) }) {
            tokenRtc.postValue(it)
        }
    }
}