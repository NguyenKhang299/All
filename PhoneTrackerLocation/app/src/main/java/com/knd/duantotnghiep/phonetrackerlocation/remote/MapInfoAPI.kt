package com.knd.duantotnghiep.phonetrackerlocation.remote

import com.knd.duantotnghiep.phonetrackerlocation.models.MapInfo
import retrofit2.Response
import retrofit2.http.GET

interface MapInfoAPI {
    @GET("mapInfo/mapinfos") // Điền endpoint của API ở đây
    suspend fun getMapInfo(): Response<List<MapInfo>>

}