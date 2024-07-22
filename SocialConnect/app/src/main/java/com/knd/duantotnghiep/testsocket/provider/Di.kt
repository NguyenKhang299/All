package com.knd.duantotnghiep.testsocket.provider

import android.content.Context
import com.knd.duantotnghiep.testsocket.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Di {
    @Provides
    @Singleton
    fun providesSocket(@ApplicationContext context: Context): StompClient {
        val mStompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, Constants.Base_URL_Socket)
        mStompClient.connect()
        return mStompClient
    }
}