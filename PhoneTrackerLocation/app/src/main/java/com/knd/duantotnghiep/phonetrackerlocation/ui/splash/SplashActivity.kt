package com.knd.duantotnghiep.phonetrackerlocation.ui.splash

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.knd.duantotnghiep.phonetrackerlocation.R
import com.knd.duantotnghiep.phonetrackerlocation.respository.SocketRepository
import com.knd.duantotnghiep.phonetrackerlocation.ui.dialog.DialogLoading
import com.knd.duantotnghiep.phonetrackerlocation.ui.account.AccountManager
import com.knd.duantotnghiep.phonetrackerlocation.ui.account.intro.IntroActivity
import com.knd.duantotnghiep.phonetrackerlocation.ui.main.MainActivity
import com.knd.duantotnghiep.phonetrackerlocation.utils.ActivityExtensions.startActivity
import com.knd.duantotnghiep.phonetrackerlocation.utils.SocketCallBack
import com.knd.duantotnghiep.phonetrackerlocation.utils.SocketExtensions.onConnect
import dagger.hilt.android.AndroidEntryPoint
import io.socket.client.Socket
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    @Inject
    lateinit var accountManager: AccountManager

    @Inject
    lateinit var socket: Socket
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        socket.onConnect(object : SocketCallBack.OnEventConnectionListener {
            override fun onConnection() {

            }

            override fun onDisconnect() {

            }

            override fun onConnectionError() {

            }

        })

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(
                if (accountManager.checkSignIn())
                    MainActivity::class.java
                else
                    IntroActivity::class.java
            )
        }, 5000)
    }
}