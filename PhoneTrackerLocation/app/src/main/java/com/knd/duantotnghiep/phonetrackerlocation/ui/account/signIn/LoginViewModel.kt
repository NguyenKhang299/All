package com.knd.duantotnghiep.phonetrackerlocation.ui.account.signIn

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.knd.duantotnghiep.phonetrackerlocation.R
import com.knd.duantotnghiep.phonetrackerlocation.models.LoginRequest
import com.knd.duantotnghiep.phonetrackerlocation.respository.AuthRepository
import com.knd.duantotnghiep.phonetrackerlocation.ui.account.AccountAction
import com.knd.duantotnghiep.phonetrackerlocation.ui.account.AccountSharedViewModel
 import com.knd.duantotnghiep.phonetrackerlocation.utils.TokenManager
import com.knd.duantotnghiep.phonetrackerlocation.utils.ValidateUtils.validatePhoneNumber
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import com.knd.duantotnghiep.phonetrackerlocation.ui.account.AccountAction.SignInAction
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val tokenManager: TokenManager,
    @ApplicationContext private val context: Context
) : AccountSharedViewModel(authRepository, tokenManager) {

    val txtPhoneNumber = MutableLiveData("+8467896418")
    val txtPassword = MutableLiveData("hashed_password")
    val errorPhoneNumber = MutableLiveData<String>()
    val errorPassword = MutableLiveData<String>()


    override fun handleAccountAction(action: AccountAction) {
        super.handleAccountAction(action)
        when (action) {
            is SignInAction.SignInWithPassword -> handelSignInWithPassword()
            else -> {}
        }
    }

    private fun handelSignInWithPassword() {
        val loginRequest = LoginRequest(txtPhoneNumber.value!!, txtPassword.value!!)

        if (!loginRequest.phoneNumber.validatePhoneNumber() && loginRequest.phoneNumber.isNotEmpty()) {
            errorPhoneNumber.postValue(context.getString(R.string.phone_number_format_error))
        }

        if (loginRequest.phoneNumber.isEmpty()) {
            errorPhoneNumber.postValue(context.getString(R.string.phone_number_blank_error))
        }

        if (loginRequest.password.isEmpty()) {
            errorPassword.postValue(context.getString(R.string.password_blank_error))
        }

        if (loginRequest.phoneNumber.validatePhoneNumber() && loginRequest.password.isNotEmpty()) {
            super.signInWithPassword(loginRequest)
        }
    }
}