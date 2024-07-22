package com.knd.duantotnghiep.phonetrackerlocation.di

import android.content.Context
import android.util.Log
import com.knd.duantotnghiep.phonetrackerlocation.remote.AuthApi
import com.knd.duantotnghiep.phonetrackerlocation.remote.AuthInterceptor
import com.knd.duantotnghiep.phonetrackerlocation.remote.ChatsApi
import com.knd.duantotnghiep.phonetrackerlocation.remote.MapInfoAPI
import com.knd.duantotnghiep.phonetrackerlocation.remote.UploadApi
import com.knd.duantotnghiep.phonetrackerlocation.remote.UserAPI
import com.knd.duantotnghiep.phonetrackerlocation.respository.AuthRepository
import com.knd.duantotnghiep.phonetrackerlocation.utils.Constants
import com.knd.duantotnghiep.phonetrackerlocation.utils.TokenManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.socket.client.IO
import io.socket.client.Socket
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {
    @Singleton
    @Provides
    fun providesSocket(@ApplicationContext context: Context): Socket {
        val opts = IO.Options()
        val tokenManager = TokenManager(context)
        val socket: Socket = IO.socket(
            Constants.BASE_URL,
            opts.apply { query = tokenManager.getToken() }
        )
        socket.connect()
        return socket
    }

    @Singleton
    @Provides
    fun providesContext(@ApplicationContext context: Context) = context

    @Singleton
    @Provides
    fun providesRetrofit(): Retrofit.Builder {
        return Retrofit.Builder().baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
    }

    @Singleton
    @Provides
    fun provideOkHttpClientBuilder(): OkHttpClient.Builder {
        return OkHttpClient.Builder()
            .callTimeout(3, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
    }

    @Singleton
    @Provides
    fun provideOkHttpClientInterceptor(
        interceptor: AuthInterceptor,
        okHttpClientBuilder: OkHttpClient.Builder
    ): OkHttpClient {
        return okHttpClientBuilder
            .addInterceptor(interceptor)
            .build()
    }

    @Singleton
    @Provides
    fun providesUserAPI(retrofitBuilder: Retrofit.Builder, okHttpClient: OkHttpClient): UserAPI {
        return retrofitBuilder.client(okHttpClient).build().create(UserAPI::class.java)
    }

    @Singleton
    @Provides
    fun provideChatsAPI(retrofitBuilder: Retrofit.Builder, okHttpClient: OkHttpClient): ChatsApi {
        return retrofitBuilder.client(okHttpClient).build().create(ChatsApi::class.java)
    }

    @Singleton
    @Provides
    fun providesAuthAPI(
        retrofitBuilder: Retrofit.Builder,
        okHttpClientBuilder: OkHttpClient.Builder
    ): AuthApi {
        return retrofitBuilder.client(okHttpClientBuilder.build()).build()
            .create(AuthApi::class.java)
    }


    @Singleton
    @Provides
    fun providesUpload(
        retrofitBuilder: Retrofit.Builder,
    ): UploadApi {
        return retrofitBuilder.build().create(UploadApi::class.java)
    }

    @Singleton
    @Provides
    fun providesMapInfoAPI(
        retrofitBuilder: Retrofit.Builder,
        okHttpClient: OkHttpClient
    ): MapInfoAPI {
        return retrofitBuilder.client(okHttpClient).build().create(MapInfoAPI::class.java)
    }
}
