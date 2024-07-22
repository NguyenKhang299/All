package com.knd.duantotnghiep.phonetrackerlocation.ui.account.signUp.phoneNumber

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.knd.duantotnghiep.phonetrackerlocation.R
import com.knd.duantotnghiep.phonetrackerlocation.models.MessageResponse
import com.knd.duantotnghiep.phonetrackerlocation.respository.AuthRepository
import com.knd.duantotnghiep.phonetrackerlocation.ui.account.AccountAction
import com.knd.duantotnghiep.phonetrackerlocation.ui.account.AccountAction.SignUpAction.ConfirmPhoneNumber
import com.knd.duantotnghiep.phonetrackerlocation.ui.account.signUp.otp.ConfirmOTPViewModel
import com.knd.duantotnghiep.phonetrackerlocation.utils.NetworkResult
import com.knd.duantotnghiep.phonetrackerlocation.utils.TokenManager
import com.knd.duantotnghiep.phonetrackerlocation.utils.UserPreferencesManager
import com.knd.duantotnghiep.phonetrackerlocation.utils.ValidateUtils.validatePassword
import com.knd.duantotnghiep.phonetrackerlocation.utils.ValidateUtils.validatePhoneNumber
import com.knd.duantotnghiep.phonetrackerlocation.utils.handleApiCall
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.ResponseBody
import javax.inject.Inject

@HiltViewModel
class ConfirmPhoneNumberViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    @ApplicationContext val context: Context
) : ConfirmOTPViewModel(authRepository, context) {
    val password = MutableLiveData("")
    val errorPhoneNumber = MutableLiveData("")
    val errorPassword = MutableLiveData("")
    override fun handel(action: AccountAction) {
        super.handel(action)
        when (action) {
             is ConfirmPhoneNumber.Confirm -> handelConfirmPhoneNumber()
            else -> {}
        }
    }


    private fun handelConfirmPhoneNumber() {
        val phoneNumber = phoneNumber.value ?: ""
        val password = password.value ?: ""

        if (phoneNumber.validatePhoneNumber() && password.validatePassword()) {
            handel(ConfirmPhoneNumber.SendOTP(phoneNumber))
        } else {
            if (phoneNumber.isEmpty()) {
                errorPhoneNumber.postValue(context.getString(R.string.phone_number_blank_error))
            }

            if (password.isEmpty()) {
                errorPassword.postValue(context.getString(R.string.password_blank_error))
            }

            if (password.isNotEmpty() && !password.validatePassword()) {
                errorPassword.postValue(context.getString(R.string.password_requirements))
            }

            if (phoneNumber.isNotEmpty() && !phoneNumber.validatePhoneNumber()) {
                errorPhoneNumber.postValue(context.getString(R.string.phone_number_format_error))
            }
        }
    }


}