package com.image.effect.timewarp.scan.filtertiktok.face.filter.util

import java.text.SimpleDateFormat
import java.util.*

class DateUtils {

    companion object {
        fun todayFd(): Int {
            return SimpleDateFormat("yyMMdd").format(Date()).toInt()
        }

        fun fdToDate(fd: Int): Date {
            return SimpleDateFormat("yyMMdd").parse(fd.toString())
        }
    }

}