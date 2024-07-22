package com.knd.duantotnghiep.testsocket.ui.chat_message.record

import android.annotation.SuppressLint
import android.content.Context
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.Build
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.ObjectOutputStream
import java.util.Date
import javax.inject.Inject

class RecordManager constructor(
    private val context: Context,
    val listener: OnRecordListener
) : IRecord {

    interface OnRecordListener : TimerRecord.OnTimerTickListener {
        fun onStatus(action: RecordAction)
        fun onMaxAmplitude(maxAmplitude: Int)
    }

    private lateinit var record: MediaRecorder
    var dirPath = ""
    var filename = ""
    private var isStart = false

    private val timerRecord = TimerRecord(object : TimerRecord.OnTimerTickListener {
        override fun onTimerTick(duration: String) {
            listener.onTimerTick(duration)
            listener.onMaxAmplitude(record.maxAmplitude)
        }
    })

    override fun startRecord() {
        record = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(context)
        } else {
            MediaRecorder()
        }

        record.apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile("$dirPath$filename.mp3")
            prepare()
            start()
        }
        listener.onStatus(RecordAction.Start)
        timerRecord.startTimer()
        isStart = true
    }

    override fun stopRecord() {
        if (isStart) {
            listener.onStatus(RecordAction.Stop)
            timerRecord.stopTimer()
            record.stop()
            record.release()
            isStart = false
        }
    }

    override fun pauseRecord() {
        if (isStart) {
            listener.onStatus(RecordAction.Pause)
            timerRecord.pauseTimer()
            record.pause()
        }
    }

    override fun resumeRecord() {
        if (isStart) {
            listener.onStatus(RecordAction.Resume)
            timerRecord.resumeTimer()
            record.resume()
        }
    }

    @SuppressLint("MissingPermission")
    override fun saveRecord(amplitudes: ArrayList<Float>) {
        val newFilename = System.currentTimeMillis().toString()
        if (newFilename != filename) {
            val newFile = File("$dirPath$newFilename.mp3")
            File("$dirPath$filename.mp3").renameTo(newFile)
        }

        var filePath = "$dirPath$newFilename.mp3"
        var timestamp = Date().time
        val ampsPath = "$dirPath$newFilename"

        val fos = FileOutputStream(ampsPath)
        val out = ObjectOutputStream(fos)
        out.writeObject(amplitudes)
        fos.close()
        out.close()
    }

    override fun deleteRecord() {

    }
}