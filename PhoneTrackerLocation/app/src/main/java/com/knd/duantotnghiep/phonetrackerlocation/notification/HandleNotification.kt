package com.knd.duantotnghiep.phonetrackerlocation.notification

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.RemoteInput
import androidx.core.net.toUri
import com.knd.duantotnghiep.phonetrackerlocation.R
import com.knd.duantotnghiep.phonetrackerlocation.models.Notification
import com.knd.duantotnghiep.phonetrackerlocation.service.HandleSendMessage
import com.knd.duantotnghiep.phonetrackerlocation.utils.Constants
import com.knd.duantotnghiep.phonetrackerlocation.ui.videoCall.CallType
import com.knd.duantotnghiep.phonetrackerlocation.ui.videoCall.VideoCall
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import okio.IOException
import java.net.URL
import javax.annotation.meta.When


object HandleNotification {

    private var notificationManager: NotificationManager? = null

    private fun getNotificationManager(context: Context): NotificationManager {
        if (notificationManager == null) {
            notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        }
        return notificationManager!!
    }

    fun cancelNotification(context: Context, idNotification: Int) {
        getNotificationManager(context).cancel(idNotification)
    }

    fun sendNotification(context: Context, notification: Notification) {
        getNotificationManager(context).notify(
            notification.timeSender,
            handleNotification(context, notification)
        )
    }

    private fun handleNotification(
        context: Context,
        notification: Notification
    ): android.app.Notification? {
        return when (notification.type) {
            MyNotificationManager.NOTIFICATION_TYPE_MESSENGER -> {
                handleConfigNotificationMess(context, notification)
            }

            MyNotificationManager.NOTIFICATION_TYPE_VIDEO_CALL -> {
                if (notification.action != MyNotificationManager.ACTION_REPLY) {
                    handleNotificationBase(context, notification)
                } else {
                    handleConfigNotificationCall(context, notification)
                }
            }

            else -> throw IllegalStateException("Unknown notification type")
        }

    }

    @SuppressLint("NewApi")
    fun handleNotificationBase(
        context: Context,
        notification: Notification
    ): android.app.Notification {
        val intent = Intent(context, VideoCall::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        return android.app.Notification.Builder(
            context,
            MyNotificationManager.NOTIFICATION_CHANNEL_ID
        ).setContentIntent(pendingIntent)
            .setContentTitle(notification.title)
            .setContentText(notification.body)
            .setSmallIcon(R.drawable.ic_call_24)
            .build()
    }

    @SuppressLint("WrongConstant", "RemoteViewLayout")
    private fun handleConfigNotificationCall(
        context: Context,
        data: Notification
    ): android.app.Notification {
        val intent = Intent(context, VideoCall::class.java)

        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val notificationBuilder =
            NotificationCompat.Builder(context, MyNotificationManager.NOTIFICATION_CHANNEL_ID)
        val remoteViews = RemoteViews(context.packageName, R.layout.custom_noti_video_call)
        val remoteViewsSmail =
            RemoteViews(context.packageName, R.layout.custom_noti_video_call_smaill)
        val pendingIntentCall = getPendingIntent(context, data, MyNotificationManager.ACTION_ANSWER)
        val pendingIntentReject =
            getPendingIntent(context, data, MyNotificationManager.ACTION_DECLINE)
        remoteViews.apply {
            setTextViewText(R.id.txtName, data.title)

            setOnClickPendingIntent(
                R.id.imgCall, pendingIntentCall
            )

            setOnClickPendingIntent(
                R.id.imgReject, pendingIntentReject
            )
            setImageViewBitmap(R.id.imgAvatar, applyImageUrl(data.url!!))
        }

        remoteViewsSmail.apply {
            setTextViewText(R.id.txtName, data.title)

            setOnClickPendingIntent(
                R.id.imgCall, pendingIntentCall
            )

            setOnClickPendingIntent(
                R.id.imgReject, pendingIntentReject
            )
            setImageViewBitmap(R.id.imgAvatar, applyImageUrl(data.url!!))
        }
        return notificationBuilder.apply {
            setCustomContentView(remoteViewsSmail)
            setCustomBigContentView(remoteViews)
            setContentIntent(pendingIntentCall)
            setContentIntent(pendingIntent)
            setContentIntent(pendingIntentReject)
            setSmallIcon(R.drawable.ic_call_24)
            setCategory(NotificationCompat.CATEGORY_CALL)
            setDefaults(NotificationCompat.VISIBILITY_PUBLIC)
            setStyle(NotificationCompat.DecoratedCustomViewStyle())
            setOngoing(true)
            setSound(
                Uri.parse("android.resource://" + context.packageName + "/" + R.raw.sound_call)
            )
        }.build()

    }

    private fun applyImageUrl(
        imageUrl: String
    ): Bitmap? {
        return runBlocking {
            val url = URL(imageUrl)
            withContext(Dispatchers.IO) {
                try {
                    val input = url.openStream()
                    BitmapFactory.decodeStream(input)
                } catch (e: IOException) {
                    null
                }
            }
        }
    }

    private fun getPendingIntent(
        context: Context,
        notification: Notification? = null,
        action: String
    ): PendingIntent {
        val intent = Intent(context, HandleSendMessage::class.java)
        intent.action = action
        intent.putExtra("action", action)
        intent.putExtra("InfoSender", notification)
        intent.putExtra("CallType", CallType.INCOMING)

        return PendingIntent.getService(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    @SuppressLint("LaunchActivityFromNotification")
    private fun handleConfigNotificationMess(
        context: Context,
        data: Notification
    ): android.app.Notification {
        val intent = Intent(context, HandleSendMessage::class.java)
        intent.action = MyNotificationManager.ACTION_REPLY
        intent.putExtra("timeSender", data.timeSender)
        val pendingIntent = PendingIntent.getService(
            context, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )
        val remoteInput = RemoteInput.Builder("reply")
            .setLabel("Quick reply")
            .build()

        val action = NotificationCompat.Action
            .Builder(R.drawable.ic_launcher_background, "Reply", pendingIntent)
            .addRemoteInput(remoteInput)
            .build()

        val largeIcon =
            BitmapFactory.decodeResource(context.resources, R.drawable.ic_launcher_background)
        val notificationBuilder = NotificationCompat.Builder(context, "NotificationChannel")
        notificationBuilder.apply {
            setContentTitle(data.title)
            setContentText(data.body)
            setContentIntent(pendingIntent)
            addAction(action)
            setSmallIcon(R.drawable.ic_launcher_background)
            setLargeIcon(largeIcon)
            setCategory(NotificationCompat.CATEGORY_MESSAGE)
        }
        return notificationBuilder.build()
    }
}
