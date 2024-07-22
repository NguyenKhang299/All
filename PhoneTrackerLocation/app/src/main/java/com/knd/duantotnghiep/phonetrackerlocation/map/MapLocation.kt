package com.knd.duantotnghiep.phonetrackerlocation.map

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Looper
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Granularity
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import com.knd.duantotnghiep.phonetrackerlocation.models.MapInfo
import com.knd.duantotnghiep.phonetrackerlocation.models.UserRequest
import com.knd.duantotnghiep.phonetrackerlocation.utils.UserPreferencesManager
import dagger.hilt.android.qualifiers.ApplicationContext
import java.text.DecimalFormat
import javax.inject.Inject

@SuppressLint("MissingPermission")
class MapLocation @Inject constructor(
    @ApplicationContext val context: Context,
    private val userPreferencesManager: UserPreferencesManager
) : LiveData<MapInfo>() {
    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)
    private lateinit var userName: String
    private lateinit var _id: String
    private val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 100)
        .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
        .setGranularity(Granularity.GRANULARITY_FINE)
        .setMinUpdateDistanceMeters(0.5f)
        .setWaitForAccurateLocation(true)
        .setMinUpdateIntervalMillis(100)
        .setMaxUpdateDelayMillis(100)
        .build()

    fun getCurrentLocation(callLocation: MutableLiveData<LatLng>) {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    val latLng = LatLng(location.latitude, location.longitude)
                    callLocation.postValue(latLng)
                }
            }
    }

    @SuppressLint("MissingPermission")
    fun requestLocationUpdates() {
        val userRequest = userPreferencesManager.getCurrentUser()
        userName = userRequest?.name ?: ""
        _id = userRequest?._id ?: ""
        fusedLocationClient.requestLocationUpdates(
            locationRequest, locationCallback, Looper.getMainLooper()
        )
    }

    @SuppressLint("MissingPermission")
    fun removeLocationCallBack() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    private val locationCallback = object : LocationCallback() {
        @RequiresApi(Build.VERSION_CODES.O)
        override fun onLocationResult(p0: LocationResult) {
            val lastLocation = p0.lastLocation
            if (lastLocation != null) {
                val mapInfo = lastLocation.let {
                    val speed = if (
                        lastLocation.hasSpeed() &&
                        lastLocation.hasAccuracy() &&
                        lastLocation.hasSpeedAccuracy()
                    ) it.speed * 3.6 else 0.0

                    MapInfo(
                        _id, userName,
                        false,
                        it.longitude,
                        it.latitude,
                        DecimalFormat("#,0").format(speed).toDouble(),
                        null, null
                    )

                }
                postValue(mapInfo)
            }
        }
    }
}