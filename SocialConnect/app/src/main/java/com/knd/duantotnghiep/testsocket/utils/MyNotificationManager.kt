package com.knd.duantotnghiep.testsocket.utils

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.res.Resources.NotFoundException
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.graphics.drawable.Icon
import android.os.Build
import android.util.Log
import android.view.Gravity
import androidx.annotation.WorkerThread
import androidx.core.app.NotificationCompat
import androidx.core.app.Person
import androidx.core.app.RemoteInput
import androidx.core.content.LocusIdCompat
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import com.knd.duantotnghiep.testsocket.R
import com.knd.duantotnghiep.testsocket.broadcasr.ReplyReceiver
import com.knd.duantotnghiep.testsocket.enum.TypeChatMessageEnum
import com.knd.duantotnghiep.testsocket.response.ChatResponse
import com.knd.duantotnghiep.testsocket.response.NotificationInfo
import com.knd.duantotnghiep.testsocket.response.NotificationResponse
import com.knd.duantotnghiep.testsocket.response.Room
import com.knd.duantotnghiep.testsocket.service.NotificationService
import com.knd.duantotnghiep.testsocket.ui.call_video.InComingCallActivity
import com.knd.duantotnghiep.testsocket.ui.chat_message.ChatMessageActivity
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import kotlin.collections.HashMap


enum class TypeNotificationEnum {
    VIDEO,
    VOICE,
    MESSAGE,
    FEEDBACK,
    NONE
}

enum class FeedbackEnum {
    ANSWER, REJECT, NO_ANSWER, NONE
}

object MyNotificationManager {
    const val TEXT_KEY_INPUT = "text key input"
    private const val CALL_CHANNEL_ID = "call_channel_id"
    private const val CALL_CHANNEL_NAME = "Call Channel"
    const val MESSAGE_CHANNEL_ID = "message_channel_id"
    private const val MESSAGE_CHANNEL_NAME = "Message Channel"


    @SuppressLint("ServiceCast")
    fun Context.createNotificationsChannelId(id: String, name: String, importance: Int) {
        (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel(id, name, importance)
            } else {
                return
            }.apply {
                createNotificationChannel(this)
            }
        }
    }

    fun createCallNotificationsChannelId(context: Context) =
        context.createNotificationsChannelId(
            CALL_CHANNEL_ID,
            CALL_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        )

    fun createMessageNotificationsChannelId(context: Context) =
        context.createNotificationsChannelId(
            MESSAGE_CHANNEL_ID,
            MESSAGE_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        )

    private fun notificationCompatBuilder(
        context: Context,
        channelId: String,
    ): NotificationCompat.Builder {
        val notificationBuilder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationCompat.Builder(context, channelId)
        } else {
            NotificationCompat.Builder(context)
        }

        return notificationBuilder
    }

    private fun loadBitmap(
        avatarUrl: Map<Int, String>,
        isSuccess: (Map<Int, Bitmap>) -> Unit
    ) {
        val rs = HashMap<Int, Bitmap>()
        avatarUrl.entries.forEach {
            Picasso.get().load(it.value).centerCrop(Gravity.TOP).resize(500, 500)
                .into(object : Target {
                    override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                        if (bitmap != null) {
                            rs[it.key] = bitmap
                            if (rs.size == avatarUrl.size) {
                                isSuccess(rs)
                            }
                        }
                    }

                    override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                    }

                    override fun onPrepareLoad(placeHolderDrawable: Drawable?) {

                    }
                })
        }
    }

    @WorkerThread
    fun updateShortcuts(
        context: Context,
        icon: IconCompat,
        notificationResponse: NotificationResponse
    ): String {
        // Extract relevant information from the notificationResponse
        val notificationInfo = createNotificationInfo(notificationResponse)
        val idShortCut = notificationInfo.id.toString()
        val labelShortCut = notificationInfo.name

        if (ShortcutManagerCompat.getShortcuts(context, ShortcutManagerCompat.FLAG_MATCH_DYNAMIC)
                .any { it.id == idShortCut }
        ) {
            return idShortCut
        }

        // Build a ShortcutInfoCompat using the extracted information
        val shortcut = ShortcutInfoCompat.Builder(context, idShortCut)
            .setLocusId(LocusIdCompat(idShortCut))
            .setActivity(ComponentName(context, ChatMessageActivity::class.java))
            .setShortLabel(labelShortCut)
            .setLongLived(true)
            .setIcon(icon)
            .setCategories(setOf("com.example.android.bubbles.category.TEXT_SHARE_TARGET"))
            .setIntent(
                Intent(context, ChatMessageActivity::class.java)
                    .setAction(Intent.ACTION_VIEW)
            )
            .build()

        // Push the dynamic shortcut to the ShortcutManager
        ShortcutManagerCompat.pushDynamicShortcut(context, shortcut)
        return idShortCut
    }


    private fun flagUpdateCurrent(mutable: Boolean): Int {
        return if (mutable) {
            if (Build.VERSION.SDK_INT >= 31) {
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
            } else {
                PendingIntent.FLAG_UPDATE_CURRENT
            }
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        }
    }

    private val unreadMessages = HashMap<Int, ArrayList<ChatResponse>>()

    fun removeHistoryChat(idSender: Int) {
        unreadMessages.remove(idSender)
    }

    fun clearHistoryChat() = unreadMessages.clear()
    private fun Context.getNotificationManager() =
        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    private fun createNotificationInfo(
        notification: NotificationResponse,
    ): NotificationInfo {// id name image
        var name = ""
        var image = ""
        var id = -1

        val room = notification.room
        val chatResponse = notification.content

        if (notification.isGroup) {
            id = room.id
            name = room.name
            image = room.image
        } else {
            id = chatResponse.senderID
            name = chatResponse.nickName
            image = chatResponse.avatar
        }

        return NotificationInfo(id, name, image, notification.isGroup)
    }


    @WorkerThread
    fun showNotificationMessage(context: Context, notificationResponse: NotificationResponse) {
        val notificationManager = context.getNotificationManager()
        val chatResponse = notificationResponse.content
        val room = notificationResponse.room
        val isGroup = notificationResponse.isGroup

        val newChat = addItemChat(chatResponse, room, isGroup)

        val linkImages = newChat.associate { it.senderID to it.avatar }.toMutableMap()
        if (isGroup) linkImages[room.id] = room.image

        var icon: IconCompat? = null
        var idShortCut: String = ""

        loadBitmap(linkImages) { originBitmap ->
            val lastKey = originBitmap.keys.lastOrNull()
            val resultBitmap = originBitmap[lastKey]

            icon = resultBitmap?.let {
                IconCompat.createWithAdaptiveBitmap(it)
            }

            idShortCut = updateShortcuts(context, icon ?: return@loadBitmap, notificationResponse)

            val messagingStyle =
                newChat.createMessagingStyleForNotification(context, isGroup, originBitmap)
            val remoteAction = createRemoteInput(context)
            val remoteTurnOffNotification = createActionTurnOffForNotification(context)

            val intent = Intent(context, ChatMessageActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                flagUpdateCurrent(true)
            )

            val builder = notificationCompatBuilder(context, MESSAGE_CHANNEL_ID)
                .setBubbleMetadata(
                    NotificationCompat.BubbleMetadata.Builder(
                        pendingIntent,
                        IconCompat.createWithResource(context, R.drawable.logo_app)
                    )
                        .setDesiredHeight(context.resources.getDimensionPixelSize(R.dimen.bubble_height))
                        .setAutoExpandBubble(true)
                        .build()
                )
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.logo_app)
                .setStyle(messagingStyle)
                .setCategory(Notification.CATEGORY_MESSAGE)
                .setShortcutId(idShortCut)
                .setLocusId(LocusIdCompat(idShortCut))
                .setOnlyAlertOnce(true)
                .addAction(remoteAction)
                .addAction(remoteTurnOffNotification)
            notificationManager.notify(idShortCut.toInt(), builder.build())
        }
    }

    fun showNotificationCallIncoming(
        context: Context,
        notificationResponse: NotificationResponse
    ) {
        val notiInfo = createNotificationInfo(notificationResponse)
        val mutableMap = mutableMapOf(notiInfo.id to notiInfo.image)
        loadBitmap(mutableMap) { originBitmap ->

            val intent = Intent(context, InComingCallActivity::class.java)
            val pendingIntentIncoming = PendingIntent.getActivity(
                context, 0, intent,
                PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val declineIntent = Intent(context, ReplyReceiver::class.java)
            declineIntent.action = ReplyReceiver.ACTION_DECLINE
            val declinePendingIntent = PendingIntent.getBroadcast(
                context, 0, declineIntent,
                PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val answerIntent = Intent(context, ReplyReceiver::class.java)
            answerIntent.action = ReplyReceiver.ACTION_ANSWER
            val answerPendingIntent = PendingIntent.getBroadcast(
                context, 0, answerIntent,
                PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val resultBitmap = originBitmap[notiInfo.id]

            val icon = resultBitmap?.let {
                IconCompat.createWithAdaptiveBitmap(it)
            }
            val caller = Person.Builder().setName(notiInfo.name).setIcon(icon).build()

            val notifiBuilder = notificationCompatBuilder(context, CALL_CHANNEL_ID)
                .setContentIntent(pendingIntentIncoming)
                .setSmallIcon(R.drawable.logo_app)
                .setStyle(
                    NotificationCompat.CallStyle.forIncomingCall(
                        caller,
                        declinePendingIntent,
                        answerPendingIntent
                    )
                )
                .setFullScreenIntent(pendingIntentIncoming, true)
                .addPerson(caller)
                .setOngoing(true)
            context.getNotificationManager().notify(notiInfo.id, notifiBuilder.build())

        }
    }

    fun showNotificationCallOngoing(
        context: Context,
        notificationResponse: NotificationResponse
    ) {
        showNotificationActionHangUp(
            context,
            notificationResponse,
            context.getString(R.string.connecting_to_make_call)
        )
    }

    fun showNotificationCallAnswer(
        context: Context,
        notificationResponse: NotificationResponse
    ) {
        showNotificationActionHangUp(
            context,
            notificationResponse,
            context.getString(R.string.tap_to_return_the_call)
        )
    }

    private fun showNotificationActionHangUp(
        context: Context,
        notificationResponse: NotificationResponse,
        contentText: String
    ) {
        val notiInfo = createNotificationInfo(notificationResponse)
        val mutableMap = mutableMapOf(notiInfo.id to notiInfo.image)

        loadBitmap(mutableMap) { originBitmap ->
            val incomingCallIntent = Intent(context, InComingCallActivity::class.java)
            val pendingIntentIncoming = PendingIntent.getActivity(
                context, 0, incomingCallIntent,
                PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val hangUpIntent = Intent(context, ReplyReceiver::class.java).apply {
                action = ReplyReceiver.ACTION_HANG_UP
            }
            val hangUpPendingIntent = PendingIntent.getBroadcast(
                context, 0, hangUpIntent,
                PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val resultBitmap = originBitmap?.get(notiInfo.id)

            val icon = resultBitmap?.let {
                IconCompat.createWithAdaptiveBitmap(it)
            }
            val caller = Person.Builder().setName(notiInfo.name).setIcon(icon).build()

            val notifiBuilder = notificationCompatBuilder(context, CALL_CHANNEL_ID)
                .setContentIntent(pendingIntentIncoming)
                .setSmallIcon(R.drawable.logo_app)
                .setStyle(
                    NotificationCompat.CallStyle.forOngoingCall(
                        caller,
                        hangUpPendingIntent
                    )
                )
                .setFullScreenIntent(pendingIntentIncoming, true)
                .addPerson(caller)
                .setOngoing(true)
            if (contentText.isNotEmpty()) notifiBuilder.setContentText(contentText)

            context.getNotificationManager().notify(notiInfo.id, notifiBuilder.build())
        }
    }

    private fun ArrayList<ChatResponse>.createMessagingStyleForNotification(
        context: Context,
        isGroup: Boolean,
        originBitmap: Map<Int, Bitmap>
    ): NotificationCompat.MessagingStyle {

        val user = Person.Builder().setName("You").build()

        val messagingStyle = NotificationCompat.MessagingStyle(user)
        messagingStyle.setGroupConversation(isGroup)

        forEach { itemChat ->
            val icon = originBitmap[1]
            val person = Person.Builder()
                .setName(itemChat.nickName)
                .setIcon(IconCompat.createWithAdaptiveBitmap(icon!!))
                .build()
            val content = when (TypeChatMessageEnum.valueOf(itemChat.typeMessage)) {
                TypeChatMessageEnum.TEXT -> itemChat.message
                TypeChatMessageEnum.FILE -> context.getString(R.string.attached_files)
                TypeChatMessageEnum.VIDEO -> context.getString(R.string.sent_video)
                TypeChatMessageEnum.IMAGE -> context.getString(R.string.sent_image)
                TypeChatMessageEnum.RECORD -> context.getString(R.string.sent_a_conversation)
                else -> ""
            }
            val message = NotificationCompat.MessagingStyle.Message(
                content,
                itemChat.createAt.time,
                person
            )
            messagingStyle.addMessage(message)
        }

        return messagingStyle
    }

    private fun addItemChat(
        chatResponse: ChatResponse,
        room: Room,
        isGroup: Boolean
    ): ArrayList<ChatResponse> {
        val id = if (isGroup) room.id else chatResponse.senderID
        var listChange = unreadMessages[id]
        if (listChange == null) listChange = ArrayList()
        listChange.add(chatResponse)
        listChange.add(chatResponse)
        unreadMessages[id] = listChange
        return listChange
    }

    private fun createActionTurnOffForNotification(context: Context): NotificationCompat.Action {

        val remoteIntent = Intent(context, ReplyReceiver::class.java)
        remoteIntent.action = ReplyReceiver.ACTION_TURN_OFF_NOTIFICATION

        val remotePendingIntent = PendingIntent.getBroadcast(
            context, 0, remoteIntent,
            flagUpdateCurrent(true)
        )
        val remoteAction = NotificationCompat.Action.Builder(
            IconCompat.createWithResource(
                context,
                R.drawable.ic_reply
            ), "Turn off notifications for 1 hour", remotePendingIntent
        )
        return remoteAction.build()
    }

    private fun createRemoteInput(context: Context): NotificationCompat.Action {
        val remoteIntent = Intent(context, ReplyReceiver::class.java)
        remoteIntent.action = ReplyReceiver.ACTION_REPLY_TEXT
        val remotePendingIntent = PendingIntent.getBroadcast(
            context, 0, remoteIntent,
            flagUpdateCurrent(true)
        )

        val remoteInput = RemoteInput.Builder(TEXT_KEY_INPUT).setLabel("Message").build()
        val remoteAction = NotificationCompat.Action.Builder(
            IconCompat.createWithResource(
                context,
                R.drawable.ic_reply
            ), "Reply", remotePendingIntent
        )
        remoteAction.addRemoteInput(remoteInput)
        return remoteAction.build()
    }

    fun handleNotification(context: Context, notificationResponse: NotificationResponse) {
        if (TypeNotificationEnum.valueOf(notificationResponse.type) == TypeNotificationEnum.NONE) Exception(
            "Type notification not none"
        )
        if (TypeNotificationEnum.valueOf(notificationResponse.type) == TypeNotificationEnum.FEEDBACK)
            handleShowNotificationFeedback(context, notificationResponse)
        else handleShowNotification(context, notificationResponse)
    }

    fun handleShowNotification(
        context: Context,
        notificationResponse: NotificationResponse
    ) {
        when (TypeNotificationEnum.valueOf(notificationResponse.type)) {
            TypeNotificationEnum.MESSAGE -> {
                showNotificationMessage(context, notificationResponse)
            }

            TypeNotificationEnum.VIDEO -> {
                showNotificationCallIncoming(context, notificationResponse)

            }

            TypeNotificationEnum.VOICE -> {
                showNotificationCallOngoing(context, notificationResponse)
            }

            else -> NotFoundException("Unknown type")
        }
    }

    private fun handleShowNotificationFeedback(
        context: Context,
        notificationResponse: NotificationResponse
    ) {
        when (FeedbackEnum.valueOf(notificationResponse.feedback)) {
            FeedbackEnum.ANSWER -> {
                showNotificationCallAnswer(context, notificationResponse)
            }

            FeedbackEnum.NO_ANSWER -> {

            }

            FeedbackEnum.REJECT -> {}
            else -> NotFoundException("Unknown feedback")
        }
    }

}