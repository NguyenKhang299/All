package com.knd.duantotnghiep.phonetrackerlocation.ui.account.signUp.otp

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.knd.duantotnghiep.phonetrackerlocation.R
import com.knd.duantotnghiep.phonetrackerlocation.models.MessageResponse
import com.knd.duantotnghiep.phonetrackerlocation.models.VerifyOTPRequest
import com.knd.duantotnghiep.phonetrackerlocation.respository.AuthRepository
import com.knd.duantotnghiep.phonetrackerlocation.ui.account.AccountAction
import com.knd.duantotnghiep.phonetrackerlocation.ui.account.AccountAction.SignUpAction.ConfirmPhoneNumber
import com.knd.duantotnghiep.phonetrackerlocation.utils.NetworkResult
import com.knd.duantotnghiep.phonetrackerlocation.utils.handleApiCall

import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
open class ConfirmOTPViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private @ApplicationContext val context: Context
) : ViewModel() {

    private val _verificationAndSignUpStatus = MutableLiveData<NetworkResult<MessageResponse>>()
    val verificationAndSignUpStatus: LiveData<NetworkResult<MessageResponse>> = _verificationAndSignUpStatus
    val enableError = MutableLiveData(false)
    val txtOtp1 = MutableLiveData("")
    val txtOtp2 = MutableLiveData("")
    val txtOtp3 = MutableLiveData("")
    val txtOtp4 = MutableLiveData("")
    val txtOtp5 = MutableLiveData("")
    val txtOtp6 = MutableLiveData("")
    val phoneNumber = MutableLiveData("")
    val error = MutableLiveData("")


    private val _sendOtpStatus = MutableLiveData<NetworkResult<MessageResponse>>()
    val sendOtpStatus: LiveData<NetworkResult<MessageResponse>> = _sendOtpStatus
    open fun handel(action: AccountAction) {
        when (action) {
            is ConfirmPhoneNumber.VerifyOTPAndSignUp -> handelVerifyOTP(action.verifyOTPRequest)
            is ConfirmPhoneNumber.SendOTP -> handelSendOTP(action.phoneNumber)
            else -> {}
        }
    }

    open fun handelSendOTP(phoneNumber: String) =
        handleApiCall(_sendOtpStatus, { authRepository.sendOTP(phoneNumber) })

    private fun handelVerifyOTP(verifyOTPRequest: VerifyOTPRequest) {
        when {
            verifyOTPRequest.otp.isEmpty() -> {
                error.postValue(context.getString(R.string.otp_blank_error))
                enableError.postValue(true)
            }

            verifyOTPRequest.otp.length < 6 -> {
                error.postValue(context.getString(R.string.pls_info))
                enableError.postValue(true)
            }

            else -> {
                enableError.postValue(false)
                handleApiCall(_verificationAndSignUpStatus, {
                    authRepository.verifyOTPAndSignUp(verifyOTPRequest)
                })
            }
        }
    }
}