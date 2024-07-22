package com.knd.duantotnghiep.phonetrackerlocation.ui.account

import android.app.Activity
import android.util.Log
import androidx.activity.result.IntentSenderRequest
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.knd.duantotnghiep.phonetrackerlocation.models.LoginRequest
import com.knd.duantotnghiep.phonetrackerlocation.models.TokenRequest
import com.knd.duantotnghiep.phonetrackerlocation.models.UserRequest
import com.knd.duantotnghiep.phonetrackerlocation.models.UserResponse
import com.knd.duantotnghiep.phonetrackerlocation.respository.AuthRepository
import com.knd.duantotnghiep.phonetrackerlocation.utils.NetworkResult
import com.knd.duantotnghiep.phonetrackerlocation.utils.TokenManager
import com.knd.duantotnghiep.phonetrackerlocation.utils.handleApiCall
import dagger.hilt.android.lifecycle.HiltViewModel

import javax.inject.Inject
import com.knd.duantotnghiep.phonetrackerlocation.ui.account.AccountAction.SignUpAction
import com.knd.duantotnghiep.phonetrackerlocation.ui.account.AccountAction.SignInAction
 import com.knd.duantotnghiep.phonetrackerlocation.utils.ActivityExtensions.showMessage
import com.knd.duantotnghiep.phonetrackerlocation.utils.UserPreferencesManager

@HiltViewModel
open class AccountSharedViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val tokenManager: TokenManager,
) : ViewModel() {
    private val _resultSignInWithPass = MutableLiveData<NetworkResult<UserResponse>>()
    val resultSignInWithPass: LiveData<NetworkResult<UserResponse>> = _resultSignInWithPass

    private val _resultSignUpWithPass = MutableLiveData<NetworkResult<UserResponse>>()
    val resultSignUpWithPass: LiveData<NetworkResult<UserResponse>> = _resultSignUpWithPass

    private val _resultAuthenticateFacebookToken = MutableLiveData<NetworkResult<UserResponse>>()
    val resultAuthenticateFacebookToken: LiveData<NetworkResult<UserResponse>> =
        _resultAuthenticateFacebookToken

    private val _resultAuthenticateGoogleToken = MutableLiveData<NetworkResult<UserResponse>>()
    val resultAuthenticateGoogleToken: LiveData<NetworkResult<UserResponse>> =
        _resultAuthenticateGoogleToken

    val _resultSignInWithFaceBookClient = authRepository._resultSignInWithFaceBookClient
    val resultSignInWithFaceBookClient: LiveData<NetworkResult<String>> =
        _resultSignInWithFaceBookClient

    val _resultSignInWithGoogleClient = MutableLiveData<NetworkResult<String>>()

    val intentSenderRequestLiveData = MutableLiveData<NetworkResult<IntentSenderRequest>>()

    val newUser = MutableLiveData<UserRequest>()

    open fun handleAccountAction(event: AccountAction) {

        when (event) {
            is SignInAction.SignInWithGoogle -> signInWithGoogleClient()
            is SignUpAction.SignUpWithGoogle -> signInWithGoogleClient()
            is SignInAction.SignInWithFacebook -> signInWithFacebookClient(event.fragment)
            is SignUpAction.SignUpWithFacebook -> signInWithFacebookClient(event.fragment)
            is AccountAction.AuthenticateFacebookToken -> authenticateFacebookToken(event.tokenRequest)
            is AccountAction.AuthenticateGoogleToken -> authenticateGoogleToken(event.idToken)
            else -> {}
        }
    }

    open fun handleSocialMediaSignInResult(
        result: NetworkResult<String>,
        accountEvent: AccountEvent,
        activity: FragmentActivity
    ) {

        if (result is NetworkResult.Success) {
            val token = result.data.toString()
            Log.d("okokoko",token)
            tokenManager.saveToken(token)
            when (accountEvent) {
                is AccountEvent.ClickSignInFacebook -> {
                    val tokenRequest = TokenRequest(token = token)
                    handleAccountAction(AccountAction.AuthenticateFacebookToken(tokenRequest))
                }

                is AccountEvent.ClickSignInGoogle -> {
                    handleAccountAction(AccountAction.AuthenticateGoogleToken(token))
                }

                else -> {}
            }
        } else if (result is NetworkResult.Error) {
            activity.showMessage(result.message.toString())
        }
    }

    open fun authenticateFacebookToken(tokenRequest: TokenRequest) {
        handleApiCall(_resultAuthenticateFacebookToken, {
            authRepository.authenticateFacebookToken(tokenRequest)
        }){
            tokenManager.saveToken(it)
        }
    }

    open fun authenticateGoogleToken(idToken: String) {
        handleApiCall(_resultAuthenticateGoogleToken, {
            authRepository.authenticateGoogleToken(idToken)
        }){
            tokenManager.saveToken(it)
        }
    }

    open fun signInWithGoogleClient() {
        authRepository.setUpSignInGoogleClient()
        authRepository.signInWithGoogleClient { intentSenderRequest, e ->
            if (intentSenderRequest != null) {
                intentSenderRequestLiveData.postValue(
                    NetworkResult.Success(intentSenderRequest)
                )
            } else {
                intentSenderRequestLiveData.postValue(NetworkResult.Error(e))
            }
        }
    }

    open fun signInWithFacebookClient(fragment: FragmentActivity) {
        authRepository.signInWithFacebookClient(fragment)
    }

    open fun signInWithPassword(loginRequest: LoginRequest) {
        handleApiCall(_resultSignInWithPass, {
            authRepository.signIn(loginRequest)
        }) { token ->
            tokenManager.saveToken(token)
        }
    }

    open fun signUpWithPassword(userRequest: UserRequest) {
        handleApiCall(_resultSignUpWithPass, {
            authRepository.signUp(userRequest)
        })
    }

}