package com.knd.duantotnghiep.phonetrackerlocation.models

import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.LatLng
import com.google.gson.annotations.SerializedName
import com.google.maps.android.clustering.ClusterItem
import java.text.DecimalFormat

data class MapInfo(
    @SerializedName("_id_user")
    val _idUser: String,
    var name: String,
    var isShowInfo: Boolean = false,
    var longitude: Double,
    var latitude: Double,
    var speed: Double =0.0,
    var batteryInfo: BatteryInfo?=null,
    var icon: BitmapDescriptor?=null
) : ClusterItem {
    override fun getPosition(): LatLng {
        return LatLng(latitude, longitude)
    }

    override fun getTitle(): String {
        return name
    }

    override fun getSnippet(): String? {
        return null
    }

    override fun getZIndex(): Float? {
        return null
    }
}
