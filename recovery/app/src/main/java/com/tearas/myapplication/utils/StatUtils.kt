package com.tearas.myapplication.utils

import android.app.usage.StorageStatsManager
import android.content.Context
import android.os.storage.StorageManager


object StatUtils {
    fun totalMemory(context: Context): Long {
        val statsManager =
            context.getSystemService(Context.STORAGE_STATS_SERVICE) as StorageStatsManager
        return statsManager.getTotalBytes(StorageManager.UUID_DEFAULT)
    }

    fun freeMemory(context: Context): Long {
        val statsManager =
            context.getSystemService(Context.STORAGE_STATS_SERVICE) as StorageStatsManager
        return statsManager.getFreeBytes(StorageManager.UUID_DEFAULT)
    }

    fun busyMemory(context: Context): Long {
        return totalMemory(context) - freeMemory(context)
    }
}