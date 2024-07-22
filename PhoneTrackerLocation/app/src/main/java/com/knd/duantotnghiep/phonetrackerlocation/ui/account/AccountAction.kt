package com.knd.duantotnghiep.phonetrackerlocation.ui.account

import android.app.Activity
import androidx.fragment.app.FragmentActivity
import com.knd.duantotnghiep.phonetrackerlocation.models.TokenRequest
import com.knd.duantotnghiep.phonetrackerlocation.models.VerifyOTPRequest

sealed class AccountAction {
    class AuthenticateFacebookToken(val tokenRequest: TokenRequest) : AccountAction()
    class AuthenticateGoogleToken(val idToken:String) : AccountAction()

    sealed class SignInAction : AccountAction() {
        object SignInWithPassword : SignInAction()
        class SignInWithFacebook(val fragment: FragmentActivity) : SignInAction()
        class SignInWithGoogle(val activity: Activity) : SignInAction()
    }

    sealed class SignUpAction : AccountAction() {

        object SignUpWithPassWord : SignUpAction()
        class SignUpWithFacebook(val fragment: FragmentActivity) : SignUpAction()
        class SignUpWithGoogle(val activity: Activity) : SignUpAction()
        sealed class ConfirmPhoneNumber : AccountAction() {
            class SendOTP(val phoneNumber: String) : ConfirmPhoneNumber()
             object Confirm : ConfirmPhoneNumber()
            class VerifyOTPAndSignUp(val verifyOTPRequest: VerifyOTPRequest) : ConfirmPhoneNumber()
        }


        sealed class ConfirmNameAction : AccountAction() {
            class ConfirmName(val phoneNumber: String) : ConfirmNameAction()
        }
    }


}
