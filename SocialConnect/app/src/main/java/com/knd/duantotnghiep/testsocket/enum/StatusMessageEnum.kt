package com.knd.duantotnghiep.testsocket.enum

enum class StatusMessageEnum {
    SEEN, ERROR, RECEIVED, SENT,MISSED_CALL,DELETED,
}
enum class StatusWithdraw{
    WITHDRAW_SENDER_SIDE,
    WITHDRAW_RECEIVER_SIDE,
    WITHDRAW_ALL,//Thu hồi tất cả
    NONE
}