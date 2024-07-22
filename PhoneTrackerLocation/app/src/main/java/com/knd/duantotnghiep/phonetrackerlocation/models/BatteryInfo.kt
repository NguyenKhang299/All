package com.knd.duantotnghiep.phonetrackerlocation.models

data class BatteryInfo(
    val pin: Int = 0,
    val isCharging: Boolean = false
) {
    fun getLevelPin(): LevelPin {
        return when {
            pin <= 25 -> LevelPin.LOW
            pin <= 50 -> LevelPin.MEDIUM
            pin <= 75 -> LevelPin.HIGH
            else -> LevelPin.VERY_HIGH
        }
    }
}

enum class LevelPin {
    LOW,
    MEDIUM,
    HIGH,
    VERY_HIGH
}
