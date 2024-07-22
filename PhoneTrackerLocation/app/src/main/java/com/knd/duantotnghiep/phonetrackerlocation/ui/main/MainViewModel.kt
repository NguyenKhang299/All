package com.knd.duantotnghiep.phonetrackerlocation.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.knd.duantotnghiep.phonetrackerlocation.broadcast.BatteryMonitor
import com.knd.duantotnghiep.phonetrackerlocation.broadcast.NetworkMonitor
import com.knd.duantotnghiep.phonetrackerlocation.map.MapLocation
import com.knd.duantotnghiep.phonetrackerlocation.models.BatteryInfo
import com.knd.duantotnghiep.phonetrackerlocation.models.MapInfo
import com.knd.duantotnghiep.phonetrackerlocation.models.UserRequest
import com.knd.duantotnghiep.phonetrackerlocation.respository.MapInfoRepository
import com.knd.duantotnghiep.phonetrackerlocation.respository.SocketRepository
import com.knd.duantotnghiep.phonetrackerlocation.respository.UploadRepository
import com.knd.duantotnghiep.phonetrackerlocation.respository.UserRepository
import com.knd.duantotnghiep.phonetrackerlocation.socket.SocketViewModel
import com.knd.duantotnghiep.phonetrackerlocation.utils.NetworkResult
import com.knd.duantotnghiep.phonetrackerlocation.utils.UserPreferencesManager
import com.knd.duantotnghiep.phonetrackerlocation.utils.handleApiCall
import dagger.hilt.android.lifecycle.HiltViewModel
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mapLocation: MapLocation, // Provides access to location services.
    private val batteryMonitor: BatteryMonitor, // Monitors the device's battery status.
    private val networkMonitor: NetworkMonitor, // Monitors network connectivity.
    val socketRepository: SocketRepository, // Handles socket-related operations.
    private val userPreferencesManager: UserPreferencesManager, // Manages user preferences.
    private val userRepository: UserRepository, // Manages user data.
    private val uploadRepository: UploadRepository, // Handles file uploads.
    private val mapInfoRepository: MapInfoRepository // Manages map-related data.
) : SocketViewModel(socketRepository) {
    // LiveData for observing network connectivity status.
    val stateNetworkLiveData = networkMonitor

    // LiveData for current user information.
    private val _currentUserRequest = MutableLiveData<NetworkResult<UserRequest>>()
    val currentUser: LiveData<NetworkResult<UserRequest>> get() = _currentUserRequest

    // LiveData for the current device's location.
    val currentLocationLiveData = MutableLiveData<LatLng>()

    // LiveData for retrieving information about user's friends.
    private val _friendsUser = MutableLiveData<NetworkResult<List<UserRequest>>>()
    val friendsUser: LiveData<NetworkResult<List<UserRequest>>> get() = _friendsUser

    // LiveData for observing location changes on the map.
    private val locationMapLiveData: LiveData<MapInfo> get() = mapLocation

    // LiveData for observing battery status changes.
    private val batteryLiveData: LiveData<BatteryInfo> get() = batteryMonitor.batteryLiveData

    // LiveData for observing synthetic data combining location and battery info.
    val infoLiveLocationLiveData = MediatorLiveData<MapInfo>()

    init {
        infoLiveLocationLiveData.addSource(locationMapLiveData) { locationMap ->
            val batteryValue = batteryLiveData.value
            if (batteryValue != null) {
                infoLiveLocationLiveData.value = locationMap.apply {
                    batteryInfo = batteryValue
                }
            }
        }

        infoLiveLocationLiveData.addSource(batteryLiveData) { battery ->
            val locationMap = locationMapLiveData.value
            if (locationMap != null) {
                infoLiveLocationLiveData.value = locationMap.apply {
                    batteryInfo = battery
                }
            }
        }

    }


    fun handel(action: MainViewAction) {

        when (action) {
            is MainViewAction.TurnOffLocationSharing -> handelTurnOffLocationSharing()
            is MainViewAction.TurnOnLocationSharing -> handelTurnOnLocationSharing()
            is MainViewAction.GetCurrentUser -> getCurrentUser()
            is MainViewAction.GetCurrentLocation -> handelGetCurrentLocation()
            is MainViewAction.GetMapInfo -> getInfoFriends()
            else -> {}
        }
    }

    private fun handelGetCurrentLocation() = mapLocation.getCurrentLocation(currentLocationLiveData)


    private fun handelTurnOnLocationSharing() {
        batteryMonitor.registerReceiver()
        mapLocation.requestLocationUpdates()
    }

    private fun handelTurnOffLocationSharing() {
        batteryMonitor.unregisterReceiver()
        mapLocation.removeLocationCallBack()
    }

    private fun getCurrentUser() =  handleApiCall (_currentUserRequest, { userRepository.getCurrentUser() })

    private fun getInfoFriends() = handleApiCall(_friendsUser, { userRepository.getInfoFriends() })
}

 