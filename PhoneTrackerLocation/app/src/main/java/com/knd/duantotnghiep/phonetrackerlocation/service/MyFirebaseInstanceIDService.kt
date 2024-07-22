package com.knd.duantotnghiep.phonetrackerlocation.service

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.RemoteInput
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.knd.duantotnghiep.phonetrackerlocation.R
import com.knd.duantotnghiep.phonetrackerlocation.application.MyApplication
import com.knd.duantotnghiep.phonetrackerlocation.application.StatusApp
 import com.knd.duantotnghiep.phonetrackerlocation.models.Notification
import com.knd.duantotnghiep.phonetrackerlocation.notification.HandleNotification
import com.knd.duantotnghiep.phonetrackerlocation.notification.MyNotificationManager
import com.knd.duantotnghiep.phonetrackerlocation.ui.main.MainActivity
import com.knd.duantotnghiep.phonetrackerlocation.ui.videoCall.CallType
import com.knd.duantotnghiep.phonetrackerlocation.ui.videoCall.VideoCall


@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class MyFirebaseInstanceIDService : FirebaseMessagingService() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val data = message.data
        val notificationRs = Notification(
            data["_id_user_sender"]!!,
            data["title"]!!,
            data["body"]!!,
            data["url"],
            data["timeSender"]!!.toInt(),
            data["type"]!!,
            data["action"]!!
        )
        if (notificationRs.type == MyNotificationManager.NOTIFICATION_TYPE_VIDEO_CALL && MyApplication.statusApp is StatusApp.Foreground
        ) {
            val intent = Intent(applicationContext, VideoCall::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("InfoSender",notificationRs)
            intent.putExtra("CallType", CallType.INCOMING)
            applicationContext.startActivity(intent)
        } else {
            HandleNotification.sendNotification(applicationContext, notificationRs)
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }
}