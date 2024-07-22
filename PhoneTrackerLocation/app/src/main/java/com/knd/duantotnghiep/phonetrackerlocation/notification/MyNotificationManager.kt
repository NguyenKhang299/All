package com.knd.duantotnghiep.phonetrackerlocation.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import com.knd.duantotnghiep.phonetrackerlocation.R

object MyNotificationManager {
    const val NOTIFICATION_TYPE_MESSENGER = "type_messenger"
    const val NOTIFICATION_TYPE_VIDEO_CALL = "type_video_call"
    const val NOTIFICATION_CHANNEL = "notificationChannel"
    const val NOTIFICATION_CHANNEL_ID = "channel"
    const val ACTION_REPLY: String ="com.knd.duantotnghiep.phonetrackerlocation.notification.REPLY"
    const val ACTION_DECLINE: String ="com.knd.duantotnghiep.phonetrackerlocation.notification.DECLINE"
    const val ACTION_ANSWER: String ="com.knd.duantotnghiep.phonetrackerlocation.notification.ANSWER"

    fun createChanelId(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_ALARM)
                .build()

            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL,
                NotificationManager.IMPORTANCE_HIGH
            )

            channel.setSound(
                Uri.parse("android.resource://" + context.packageName + "/" + R.raw.sound_call),
                audioAttributes
            )
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}