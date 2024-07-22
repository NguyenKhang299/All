package com.knd.duantotnghiep.testsocket.broadcasr

import android.app.RemoteInput
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.knd.duantotnghiep.testsocket.utils.MyNotificationManager

class ReplyReceiver : BroadcastReceiver() {
    companion object {
        const val ACTION_ANSWER = "com.knd.duantotnghiep.testsocket.ANSWER"
        const val ACTION_DECLINE = "com.knd.duantotnghiep.testsocket.DECLINE"
        const val ACTION_REPLY_TEXT = "com.knd.duantotnghiep.testsocket.REPLY_TEXT"
        const val ACTION_HANG_UP = "com.knd.duantotnghiep.testsocket.HANG_UP"
        const val ACTION_TURN_OFF_NOTIFICATION = "com.knd.duantotnghiep.testsocket.TURN_OFF_NOTIFICATION"
    }

    override fun onReceive(p0: Context?, p1: Intent?) {
        if (p1 == null) return
        when (p1.action) {
            ACTION_ANSWER -> {

            }
            ACTION_DECLINE -> {

            }
            ACTION_REPLY_TEXT -> {
                Toast.makeText(p0!!,RemoteInput.getResultsFromIntent(p1).getString(MyNotificationManager.TEXT_KEY_INPUT), Toast.LENGTH_SHORT).show()
            }
            ACTION_HANG_UP -> {

            }
        }
    }
}