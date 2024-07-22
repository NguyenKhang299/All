package com.knd.duantotnghiep.phonetrackerlocation.respository

import com.knd.duantotnghiep.phonetrackerlocation.remote.UploadApi
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class UploadRepository @Inject constructor(private val uploadApi: UploadApi) {
    suspend fun upLoadAvatar(photo: MultipartBody.Part) = uploadApi.uploadAvatar(photo)

}