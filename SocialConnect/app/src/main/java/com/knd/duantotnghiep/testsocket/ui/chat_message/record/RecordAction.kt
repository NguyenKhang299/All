package com.knd.duantotnghiep.testsocket.ui.chat_message.record

sealed class RecordAction {
    data object Start : RecordAction()
    data object Stop : RecordAction()
    data object Pause : RecordAction()
    data object Resume : RecordAction()
}