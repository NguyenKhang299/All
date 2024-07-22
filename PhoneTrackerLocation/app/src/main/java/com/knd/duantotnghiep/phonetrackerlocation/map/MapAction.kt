package com.knd.duantotnghiep.phonetrackerlocation.map

sealed class MapAction  {
    object RequestLocationUpdates:MapAction()
    object RemoveLocationCallBack:MapAction()
}