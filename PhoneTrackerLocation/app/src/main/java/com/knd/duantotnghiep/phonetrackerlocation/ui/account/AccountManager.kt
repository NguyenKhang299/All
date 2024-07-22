package com.knd.duantotnghiep.phonetrackerlocation.ui.account

import android.content.Context
import android.util.Log
import com.facebook.AccessToken
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.knd.duantotnghiep.phonetrackerlocation.utils.TokenManager
import com.knd.duantotnghiep.phonetrackerlocation.utils.UserPreferencesManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class AccountManager @Inject constructor(@ApplicationContext private val context: Context) {
    @Inject
    lateinit var userPreferencesManager: UserPreferencesManager

    @Inject
    lateinit var tokenManager: TokenManager

    fun checkSignIn(): Boolean {
        return userPreferencesManager.getCurrentUser() != null
    }

    fun logOut() {
        Identity.getSignInClient(context).signOut()
        userPreferencesManager.removeCurrentUser()
        tokenManager.removeToken()
    }


}