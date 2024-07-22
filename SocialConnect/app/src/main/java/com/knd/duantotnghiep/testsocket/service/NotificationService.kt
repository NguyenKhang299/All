package com.knd.duantotnghiep.testsocket.service

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.Person
import androidx.core.graphics.drawable.IconCompat
import com.knd.duantotnghiep.testsocket.R
import com.knd.duantotnghiep.testsocket.response.NotificationResponse
import com.knd.duantotnghiep.testsocket.ui.chat_message.ChatMessageActivity
import com.knd.duantotnghiep.testsocket.utils.MyNotificationManager

class NotificationService : Service() {
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }


    @SuppressLint("NewApi", "MissingPermission", "ForegroundServiceType")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        return START_NOT_STICKY
    }
}