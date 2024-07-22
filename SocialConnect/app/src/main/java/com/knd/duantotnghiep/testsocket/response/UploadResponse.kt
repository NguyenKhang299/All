package com.knd.duantotnghiep.testsocket.response

class UploadResponse(
    val id: Int,
    val name: String,
    val filePath: String,
    val contentType: String,
    val amplitude:List<Float> = emptyList(),
    val size: String,
    val createAt: Long
) {
}