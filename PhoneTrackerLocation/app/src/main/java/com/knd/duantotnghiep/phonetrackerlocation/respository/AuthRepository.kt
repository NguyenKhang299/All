package com.knd.duantotnghiep.phonetrackerlocation.respository

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.util.Base64
import android.util.Log
import androidx.activity.result.IntentSenderRequest
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.knd.duantotnghiep.phonetrackerlocation.R
import com.knd.duantotnghiep.phonetrackerlocation.models.LoginRequest
import com.knd.duantotnghiep.phonetrackerlocation.models.TokenRequest
import com.knd.duantotnghiep.phonetrackerlocation.models.UserRequest
import com.knd.duantotnghiep.phonetrackerlocation.models.VerifyOTPRequest
import com.knd.duantotnghiep.phonetrackerlocation.remote.AuthApi
import com.knd.duantotnghiep.phonetrackerlocation.utils.Constants
import com.knd.duantotnghiep.phonetrackerlocation.utils.NetworkResult
import dagger.hilt.android.qualifiers.ApplicationContext
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import javax.inject.Inject



class AuthRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val authApi: AuthApi
) {
    private val callbackFb = CallbackManager.Factory.create()
    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest
    private val resultSignInWithFaceBookClient = MutableLiveData<NetworkResult<String>>()
    val _resultSignInWithFaceBookClient get() = resultSignInWithFaceBookClient
    suspend fun sendOTP(phoneNumber: String) = authApi.sendOTP(phoneNumber)
    suspend fun verifyOTPAndSignUp(verifyOTPRequest: VerifyOTPRequest) = authApi.verifyOTPAndSignUp(verifyOTPRequest)
    suspend fun signIn(loginRequest: LoginRequest) = authApi.signIn(loginRequest)
    suspend fun signUp(userRequest: UserRequest) = authApi.signUp(userRequest)
    suspend fun authenticateFacebookToken(tokenRequest: TokenRequest) =
        authApi.authenticateFacebookToken(tokenRequest)
    suspend fun authenticateGoogleToken(idToken:String) =
        authApi.authenticateGoogleToken(idToken)
    fun setUpSignInGoogleClient() {
        oneTapClient = Identity.getSignInClient(context);
        signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    // Your server's client ID, not your Android client ID.
                    .setServerClientId(context.getString(R.string.default_client_id))
                    // Only show accounts previously used to sign in.
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            )
            // Automatically sign in when exactly one credential is retrieved.
//            .setAutoSelectEnabled(true)
            .build();
    }


    fun signInWithGoogleClient(call: (IntentSenderRequest?, String?) -> Unit) {
        oneTapClient.beginSignIn(signInRequest).addOnSuccessListener { result ->
            val intentSender = result.pendingIntent.intentSender
            val intentSenderRequest = IntentSenderRequest.Builder(intentSender).build()
            call.invoke(intentSenderRequest, null)
        }
            .addOnFailureListener { e ->
                 call.invoke(null, e.localizedMessage)
             }
    }


    fun signInWithFacebookClient(fragment: FragmentActivity) {
        LoginManager.getInstance().logInWithReadPermissions(fragment, listOf("public_profile"))

        LoginManager.getInstance()
            .registerCallback(callbackFb, object : FacebookCallback<LoginResult> {
                override fun onCancel() {
                    resultSignInWithFaceBookClient.postValue(NetworkResult.Error(context.getString(R.string.cancel_login_message)))
                }

                override fun onError(error: FacebookException) {
                    resultSignInWithFaceBookClient.postValue(NetworkResult.Error(error.message))
                }

                override fun onSuccess(result: LoginResult) {
                    resultSignInWithFaceBookClient.postValue(NetworkResult.Success(result.accessToken.token))
                }
            })

    }


    fun printSHA() {
        try {
            val info: PackageInfo = context.packageManager.getPackageInfo(
                Constants.AUTHORITY,
                PackageManager.GET_SIGNATURES
            )
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.d(
                    "KeyHash:",
                    Base64.encodeToString(
                        md.digest(),
                        Base64.DEFAULT
                    )
                )
            }
        } catch (e: PackageManager.NameNotFoundException) {

        } catch (e: NoSuchAlgorithmException) {

        }
    }
}

