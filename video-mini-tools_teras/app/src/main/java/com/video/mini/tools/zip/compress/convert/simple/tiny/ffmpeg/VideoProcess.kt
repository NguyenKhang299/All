package com.video.mini.tools.zip.compress.convert.simple.tiny.ffmpeg

import android.content.Context
import android.util.Log
import com.arthenica.ffmpegkit.FFmpegKit
import com.arthenica.ffmpegkit.FFmpegKitConfig
import com.arthenica.ffmpegkit.FFprobeKit
import com.arthenica.ffmpegkit.MediaInformation
import com.arthenica.ffmpegkit.ReturnCode
import com.arthenica.ffmpegkit.Session
import com.video.mini.tools.zip.compress.convert.simple.tiny.model.MediaInfo
import com.video.mini.tools.zip.compress.convert.simple.tiny.model.OptionMedia
import com.video.mini.tools.zip.compress.convert.simple.tiny.model.Resolution
import com.video.mini.tools.zip.compress.convert.simple.tiny.utils.Utils
import java.io.File
import kotlin.math.abs


class VideoProcess {
    class Builder(val context: Context, private val optionMedia: OptionMedia) {
        companion object {
            fun cancel() = FFmpegKit.cancel()
        }

        private val durations = arrayListOf<Float>()
        private val commands = ArrayList<String>()
        private var pathOutputs = ArrayList<String>()
        private val mediaInfoOutput = ArrayList<MediaInfo>()
        private var countSuccess = 0
        private var countFailed = 0
        private var duration = 0
        private var currentIndex = 0
        private lateinit var iProcess: IProcessFFmpeg
        private var pass1: String = ""
        private var pass2: String = ""
        private var isTwoCompress: Boolean = false
        private val countCompress
            get() = durations.size - 1

        init {
            FFmpegKitConfig.enableStatisticsCallback { newStatistics ->
                var currentIndex = 0
                if (count % 2 == 0) currentIndex = (count / 2) - 1
                if (!isTwoCompress) currentIndex = this.currentIndex
                val process = (newStatistics.time.toFloat() / durations[currentIndex]) * 100
                val min = minOf(100, process.toInt())
                iProcess.processElement(abs(currentIndex), min)
            }
        }

        private fun createMediaInfo(
            mediaInfo: MediaInformation,
            executionId: Long,
            pathOutput: String,
        ): MediaInfo {
            val filename = mediaInfo.filename.substringAfterLast("/")
            val size = mediaInfo.size.toLong()
            val resolution = try {
                if (optionMedia.mediaAction != MediaAction.JoinVideo) {
                    Resolution(
                        mediaInfo.streams[0].width.toInt(),
                        mediaInfo.streams[0].height.toInt()
                    )
                } else null
            } catch (e: NullPointerException) {
                Resolution(0, 0)
            }
            val durationFormatted = Utils.formatTime(duration.toLong() * 1000)
            val extension = pathOutput.substringAfterLast(".")
            val bitrate = mediaInfo.bitrate.toFloat().toLong()

            return MediaInfo(
                executionId,
                filename,
                pathOutput,
                size,
                resolution,
                durationFormatted,
                extension,
                bitrate,
                Utils.formatDate(System.currentTimeMillis()),
                isVideo = optionMedia.mediaAction != MediaAction.ExtractAudio,
                false
            )
        }


        fun compressAsync(
            commands: List<String>,
            iProcess: IProcessFFmpeg
        ) {
            setProgress()
            initializeCompressAsync(commands, iProcess)
            processFileSet(this.commands, 0)
        }

        fun twoCompressAsync(
            commands: List<String>,
            iProcess: IProcessFFmpeg
        ) {
            setProgress()
            initializeTwoCompress(commands, iProcess)
            processFileSet(this.commands, 0)
        }

        private fun setProgress() {
            optionMedia.dataOriginal.forEach { it ->
                val timeVideo = Utils.convertTimeToMiliSeconds(it.time).toFloat()
                val duration: Float = (
                        when (optionMedia.mediaAction) {
                            is MediaAction.CutTrim.CutVideo -> {
                                timeVideo - (optionMedia.endTime - optionMedia.startTime)
                            }

                            is MediaAction.CutTrim.TrimVideo -> {
                                optionMedia.endTime - optionMedia.startTime
                            }

                            is MediaAction.FastForward -> {
                                timeVideo / (optionMedia.speed)
                            }

                            is MediaAction.SlowVideo -> {
                                timeVideo * (1 + (1 - optionMedia.speed))
                            }

                            is MediaAction.JoinVideo -> {
                                optionMedia.dataOriginal.sumOf { Utils.convertTimeToMiliSeconds(it.time) }
                            }

                            else -> timeVideo
                        }
                        ).toFloat()
                 durations.add(duration)
                if (optionMedia.mediaAction is MediaAction.CutTrim.CutVideo ||
                    optionMedia.mediaAction is MediaAction.CutTrim.TrimVideo ||
                    optionMedia.mediaAction is MediaAction.FastForward ||
                    optionMedia.mediaAction is MediaAction.JoinVideo
                ) {
                    return@forEach
                }
            }
        }


        private fun initializeCompressAsync(commands: List<String>, iProcess: IProcessFFmpeg) {
            this.iProcess = iProcess
            isTwoCompress = false
            this.commands.addAll(commands)
            commands.forEach {
                pathOutputs.add(it.substring(it.lastIndexOf(" ") + 1))
            }
        }

        private fun initializeTwoCompress(commands: List<String>, iProcess: IProcessFFmpeg) {
            isTwoCompress = true
            this.iProcess = iProcess
            commands.forEach { command ->
                val positionPass2 = command.split("-y -i")
                if (positionPass2.size >= 2) {
                    pass1 = "-y -i ${positionPass2[1].trim()}"
                    pass2 = "-y -i ${positionPass2[2].trim()}"
                    pathOutputs.add(command.substring(command.lastIndexOf(" ") + 1))
                    this.commands.addAll(listOf(pass1, pass2))
                } else {
                    iProcess.onFailure(currentIndex, "Invalid double compression command syntax")
                    return
                }
            }

        }


        private fun processFileSet(commands: List<String>, currentIndex: Int = 0) {
            this.currentIndex = currentIndex
            if (currentIndex >= commands.size) {
                resetToZero()
                return
            }
            iProcess.onCurrentElement(currentIndex)
            Log.d("ssssssssss",commands[currentIndex])
            FFmpegKit.executeAsync(commands[currentIndex]) { session ->
                handleExecutionResult(session)
            }
        }


        private var count = 1
        private fun handleExecutionResult(session: Session) {
            if (ReturnCode.isSuccess(session.returnCode)) {
                if (count % 2 == 0 || !isTwoCompress) {
                    var currentIndex = 0
                    if (count % 2 == 0) currentIndex = (count / 2) - 1
                    if (!isTwoCompress) currentIndex = this.currentIndex

                    val pathOutput = pathOutputs[currentIndex]
                     val mediaInfo = FFprobeKit.getMediaInformation(pathOutput)
                    mediaInfoOutput.add(
                        createMediaInfo(
                            mediaInfo.mediaInformation,
                            session.sessionId,
                            pathOutput
                        )
                    )
                    countSuccess += 1
                    iProcess.processElement(currentIndex, 100)
                    iProcess.onSuccess(
                        currentIndex, createMediaInfo(
                            mediaInfo.mediaInformation,
                            session.sessionId,
                            pathOutput
                        )
                    )
                }
                processFileSet(commands, currentIndex + 1)
                count += 1
            } else {
                cleanupAndFail("Something went wrong. Please try again.")
                countFailed += 1
                if (currentIndex == countCompress) iProcess.onFinish()
                processFileSet(commands, currentIndex + 1)
            }
        }

        private fun cleanupAndFail(errorMessage: String) {
             iProcess.onFailure(currentIndex, errorMessage)

        }


        private fun resetToZero() {
            iProcess.onFinish()
            countSuccess = 0
            countFailed = 0
            duration = 0
            currentIndex = 0
            commands.clear()
            mediaInfoOutput.clear()
        }
    }
}