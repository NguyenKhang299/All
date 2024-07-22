package com.knd.duantotnghiep.phonetrackerlocation.notification

import java.io.Serializable

sealed class NotificationAction:Serializable{
    object ActionDecline :NotificationAction()
    object ActionAnswer :NotificationAction()
}
