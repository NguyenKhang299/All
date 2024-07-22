package com.knd.duantotnghiep.testsocket.repository

interface AuthRepository {
    suspend fun signIn(username: String, password: String)
    suspend fun signUp(username: String, password: String)
    suspend fun forgotPassword(username: String)
    suspend fun verifyPhoneNumber(phoneNumber: String)
    suspend fun verifyEmail(email: String)
}