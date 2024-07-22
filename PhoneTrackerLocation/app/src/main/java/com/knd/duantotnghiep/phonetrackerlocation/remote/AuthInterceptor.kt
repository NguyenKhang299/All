package com.knd.duantotnghiep.phonetrackerlocation.remote

import android.util.Log
import com.knd.duantotnghiep.phonetrackerlocation.utils.TokenManager
import com.knd.duantotnghiep.phonetrackerlocation.utils.UserPreferencesManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor() : Interceptor {
    @Inject
    lateinit var tokenManager: TokenManager
    @Inject
    lateinit var userPreferencesManager: UserPreferencesManager
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
        val token = tokenManager.getToken()
        request.addHeader("Authorization", "Bearer $token")
        return chain.proceed(request.build())
    }
}