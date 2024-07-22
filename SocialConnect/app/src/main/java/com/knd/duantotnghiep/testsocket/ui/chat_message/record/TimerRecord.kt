package com.knd.duantotnghiep.testsocket.ui.chat_message.record

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class TimerRecord(private val listener: OnTimerTickListener) : ITimer {
    interface OnTimerTickListener {
        fun onTimerTick(duration: String)
    }

    private var handler = Handler(Looper.getMainLooper())

    @SuppressLint("SimpleDateFormat")
    private var simpleDateFormat = SimpleDateFormat("HH:mm:ss")
        .apply { timeZone = TimeZone.getTimeZone("GMT") }
    private lateinit var runnable: Runnable

    private var duration = 0L
    private var delay = 100L

    init {
        runnable = Runnable {
            duration += delay
            handler.postDelayed(runnable, delay)
            listener.onTimerTick(formatTime(duration))
        }
    }

    private fun formatTime(time: Long): String {
        val time = simpleDateFormat.format(time)
        return if (time.startsWith("00:")) time.substring(3) else time
    }

    override fun startTimer() {
        handler.postDelayed(runnable, delay)
    }

    override fun stopTimer() {
        handler.removeCallbacks(runnable)
        duration = 0L
    }

    override fun pauseTimer() {
        handler.removeCallbacks(runnable)
    }

    override fun resumeTimer() {

    }
}