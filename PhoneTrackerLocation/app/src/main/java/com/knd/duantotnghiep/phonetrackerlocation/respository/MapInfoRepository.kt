package com.knd.duantotnghiep.phonetrackerlocation.respository

import com.knd.duantotnghiep.phonetrackerlocation.remote.MapInfoAPI
import javax.inject.Inject

class MapInfoRepository @Inject constructor(private val mapInfoAPI: MapInfoAPI) {
    suspend fun getMapInfo() = mapInfoAPI.getMapInfo()
}