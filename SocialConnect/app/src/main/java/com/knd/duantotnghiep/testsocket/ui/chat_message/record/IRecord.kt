package com.knd.duantotnghiep.testsocket.ui.chat_message.record

interface IRecord {
    fun startRecord()
    fun stopRecord()
    fun pauseRecord()
    fun resumeRecord()

    fun deleteRecord()
    fun saveRecord(amplitudes: ArrayList<Float>)
}