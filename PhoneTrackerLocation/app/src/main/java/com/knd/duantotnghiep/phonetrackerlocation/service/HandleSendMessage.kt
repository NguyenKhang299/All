package com.knd.duantotnghiep.phonetrackerlocation.service

import android.app.RemoteInput
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.widget.Toast
import com.knd.duantotnghiep.phonetrackerlocation.models.Notification
import com.knd.duantotnghiep.phonetrackerlocation.models.UserRequest
import com.knd.duantotnghiep.phonetrackerlocation.notification.HandleNotification
import com.knd.duantotnghiep.phonetrackerlocation.notification.MyNotificationManager
import com.knd.duantotnghiep.phonetrackerlocation.respository.UserRepository
import com.knd.duantotnghiep.phonetrackerlocation.utils.getSerializableExtra
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InterruptedIOException
import java.net.SocketException
import java.net.SocketTimeoutException
import javax.inject.Inject

@AndroidEntryPoint
class HandleSendMessage @Inject constructor() : Service() {
    @Inject
    lateinit var userRepository: UserRepository
    private lateinit var userCurrent: UserRequest
    private lateinit var intent: Intent

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null) {
            this.intent = intent
            userCurrent = UserRequest(_id = "oknhe", name = "Nguyễn Duy Khang", avatar = "oc")
            val notificationReceived = intent.getSerializableExtra<Notification>("InfoSender")!!
            val notificationSent = setUpNotificationSent(userCurrent, intent.action!!)

            when (intent.action) {
                MyNotificationManager.ACTION_REPLY -> handleActionReply(
                    notificationSent,
                    notificationReceived
                )

                MyNotificationManager.ACTION_ANSWER -> handleActionAnswer(
                    notificationSent,
                    notificationReceived
                )

                MyNotificationManager.ACTION_DECLINE -> handleActionDecline(
                    notificationSent,
                    notificationReceived
                )
            }
        }

        return START_NOT_STICKY
    }

    private fun handleActionAnswer(
        notificationSent: Notification,
        notificationReceived: Notification
    ) {
        notificationSent.setContentNotification(
            "Cuộc gọi đang diễn ra",
            "Bạn đang gọi với ${userCurrent.name}"
        )
        sendNotification(notificationSent, notificationReceived)
    }

    private fun handleActionDecline(
        notificationSent: Notification,
        notificationReceived: Notification
    ) {
        notificationSent.setContentNotification(
            "Người dùng từ chối cuộc gọi",
            "${userCurrent.name} đã từ chối cuộc gọi"
        )
        sendNotification(notificationSent, notificationReceived)
    }

    private fun handleActionReply(
        notificationSent: Notification,
        notificationReceived: Notification
    ) {
        notificationSent.body = RemoteInput.getResultsFromIntent(intent).getString("reply")!!
        sendNotification(notificationSent, notificationReceived)
    }

    private fun sendNotification(
        notificationSent: Notification,
        notificationReceived: Notification
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = userRepository.sendNotification(
                    notificationSent,
                    notificationReceived._id_user_sender
                )
                if (response.isSuccessful && response.errorBody() != null) {
                    HandleNotification.cancelNotification(
                        applicationContext,
                        notificationReceived.timeSender
                    )
                    showMessage("Tin nhắn đã được gửi")
                }
            } catch (e: InterruptedIOException) {
                showMessage("Lỗi kết nối")
            } catch (e: SocketException) {
                showMessage("Lỗi kết nối")
            }
        }
    }

    private suspend fun showMessage(s: String) {
        withContext(Dispatchers.Main) {
            Toast.makeText(applicationContext, s, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setUpNotificationSent(userCurrent: UserRequest, action: String): Notification {
        return Notification(
            userCurrent._id!!,
            userCurrent.name,
            "",
            userCurrent.avatar,
            System.currentTimeMillis().toInt(),
            MyNotificationManager.NOTIFICATION_TYPE_MESSENGER,
            action
        )
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}


