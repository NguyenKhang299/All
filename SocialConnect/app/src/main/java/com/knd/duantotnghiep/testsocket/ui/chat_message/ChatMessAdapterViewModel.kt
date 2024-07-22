package com.knd.duantotnghiep.testsocket.ui.chat_message

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import com.knd.duantotnghiep.testsocket.ui.chat_message.record.IRecord
import com.knd.duantotnghiep.testsocket.ui.chat_message.record.ITimer
import com.knd.duantotnghiep.testsocket.ui.chat_message.record.TimerRecord
import com.knd.duantotnghiep.testsocket.ui.chat_message.record.WaveformView
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class ChatMessAdapterViewModel
@Inject constructor(@ApplicationContext val context: Context) : ViewModel(){
    private var timeRecord = TimerRecord(object : TimerRecord.OnTimerTickListener {
        override fun onTimerTick(duration: String) {

        }
    })


}