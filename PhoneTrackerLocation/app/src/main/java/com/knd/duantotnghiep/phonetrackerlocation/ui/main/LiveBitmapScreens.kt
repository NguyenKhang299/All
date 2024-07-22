package com.knd.duantotnghiep.phonetrackerlocation.ui.main

import android.graphics.Bitmap
import android.util.Log
import android.view.View
import com.knd.duantotnghiep.phonetrackerlocation.utils.BitmapHandel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LiveBitmapScreens @Inject constructor() {
    private val captureInterval: Long = 100 // interval in milliseconds
    private val handlerScope = CoroutineScope(Dispatchers.Main)
    private val coroutines = HashMap<String, Job>()
    fun startCapture(
        binding: View, id: String, oneShotCapture: Boolean = true,
        call: (Bitmap, String) -> Unit
    ) {
        try {
            val coroutine = handlerScope.launch {
                while (true) {
                    val bitmap = BitmapHandel.loadBitmapFromView(binding)
                    call(bitmap!!, id)
                    bitmap.recycle()
                    delay(captureInterval)
                    if (oneShotCapture) stopCapture(id)
                }
            }
            coroutine.start()
            coroutines[id] = coroutine
        } catch (e: Exception) {
            Log.d("sdddđ", e.toString())
        }
    }

    fun stopCapture(id: String) {
        coroutines[id]?.cancel()
        coroutines.remove(id)
    }

    fun stopAllCapture() {
        coroutines.forEach { (id, job) -> job.cancel() }
        coroutines.clear()
    }
}
