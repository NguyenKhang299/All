package com.knd.duantotnghiep.phonetrackerlocation.map

import android.content.Context
import android.util.Log
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.knd.duantotnghiep.phonetrackerlocation.models.MapInfo


class ClusterMarkerRenderer(
    context: Context?,
    private val map: GoogleMap?,
    private val clusterManager: ClusterManager<MapInfo>?
) : DefaultClusterRenderer<MapInfo>(context, map, clusterManager) {
    var long = System.currentTimeMillis().toInt()
    override fun onBeforeClusterItemRendered(item: MapInfo, markerOptions: MarkerOptions) {
        //  https://stackoverflow.com/questions/14811579/how-to-create-a-custom-shaped-bitmap-marker-with-android-map-api-v2/
        markerOptions.icon(item.icon).anchor(0.5f, 0.5f).title(item.title).snippet(item.snippet)
         long = System.currentTimeMillis().toInt()
    }


    override fun onClusterItemRendered(clusterItem: MapInfo, marker: Marker) {
        super.onClusterItemRendered(clusterItem, marker)
        if (clusterItem.isShowInfo) marker.showInfoWindow() else marker.hideInfoWindow()
    }
}