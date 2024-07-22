package com.knd.duantotnghiep.phonetrackerlocation.ui.main

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import com.google.maps.android.clustering.ClusterManager
import com.knd.duantotnghiep.phonetrackerlocation.R
import com.knd.duantotnghiep.phonetrackerlocation.application.MyApplication
import com.knd.duantotnghiep.phonetrackerlocation.core.BaseActivity
import com.knd.duantotnghiep.phonetrackerlocation.databinding.ActivityMainBinding
import com.knd.duantotnghiep.phonetrackerlocation.databinding.ItemMarkerBinding
import com.knd.duantotnghiep.phonetrackerlocation.ui.dialog.search.BottomSheetSearch
import com.knd.duantotnghiep.phonetrackerlocation.ui.dialog.DialogCallBack
import com.knd.duantotnghiep.phonetrackerlocation.ui.dialog.DialogPermission
import com.knd.duantotnghiep.phonetrackerlocation.map.ClusterMarkerRenderer
import com.knd.duantotnghiep.phonetrackerlocation.models.MapInfo
import com.knd.duantotnghiep.phonetrackerlocation.models.UserRequest
import com.knd.duantotnghiep.phonetrackerlocation.ui.profile.ProfileActivity
import com.knd.duantotnghiep.phonetrackerlocation.socket.SocketAction
import com.knd.duantotnghiep.phonetrackerlocation.ui.dialog.chats.ListChatsFragment
import com.knd.duantotnghiep.phonetrackerlocation.utils.ActivityExtensions.startActivity
import com.knd.duantotnghiep.phonetrackerlocation.utils.Constants
import com.knd.duantotnghiep.phonetrackerlocation.utils.DialogUtils.dismissDialogLoading
import com.knd.duantotnghiep.phonetrackerlocation.utils.DialogUtils.showDialogLoading
import com.knd.duantotnghiep.phonetrackerlocation.utils.NetworkResult
import com.knd.duantotnghiep.phonetrackerlocation.utils.Permission.hasPermissionLocation
import com.knd.duantotnghiep.phonetrackerlocation.utils.UserPreferencesManager
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {
    @Inject
    lateinit var userPreferencesManager: UserPreferencesManager
    private lateinit var file: File

    @Inject
    lateinit var liveBitmapScreens: LiveBitmapScreens
    private lateinit var mMap: GoogleMap
    private var userCurrent: UserRequest? = null
    private lateinit var clusterManager: ClusterManager<MapInfo>
    private var mapInfo: MapInfo? = null
    private val listItem = mutableListOf<MapInfo>()
    private val listMarkerBindings = mutableListOf<ItemMarkerBinding>()
    private val mainViewModel by viewModels<MainViewModel>()
    private var users: MutableList<UserRequest> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel.handel(MainViewAction.TurnOnLocationSharing)
    }

    override fun onDestroy() {
        super.onDestroy()
        mainViewModel.handel(MainViewAction.TurnOffLocationSharing)
        mainViewModel.handleSocket(SocketAction.DisconnectSocket)
    }

    override fun initData() {
        userCurrent = userPreferencesManager.getCurrentUser()
        Log.d("Configsassss", userCurrent!!._id!!)

    }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun initView() {
        setUpGoogleMap()
        with(binding) {
            FirebaseMessaging.getInstance().token.addOnSuccessListener {
                Log.d("complete", it)
            }.addOnFailureListener {
                Log.d("complete", it.toString())
            }

            imgSearch.setOnClickListener {
                setUpBottomSheetSearch()
            }

            launchProfile.setOnClickListener {
                startActivity(ProfileActivity::class.java, false)
            }

            currentLocation.setOnClickListener {
                mainViewModel.handel(MainViewAction.GetCurrentLocation)
            }

            imgMessage.setOnClickListener {
                setUpBottomSheetChats()
            }
        }
        if (!hasPermissionLocation()) {
            setUpDialogPermission()
        }
    }

    private fun setUpBottomSheetChats() {
        val bottomSheetChats = ListChatsFragment()
        bottomSheetChats.show(supportFragmentManager, bottomSheetChats::class.java.name)
    }

    private fun setUpBottomSheetSearch() {
        val bss = BottomSheetSearch(object : DialogCallBack.OnResultListener<UserRequest> {
            override fun onResult(rs: UserRequest) {
                val locationFriend = listItem.find { rs._id == it._idUser }!!
                mMap.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(
                            locationFriend.latitude,
                            locationFriend.longitude
                        ), Constants.ZOOM_MAP_DEFAULT
                    )
                )
            }
        }).apply {
            arguments = Bundle().apply {
                putString(LIST_FRIENDS, Gson().toJson(users))
            }
        }
        bss.show(supportFragmentManager, "BottomSheetSearch")
    }

    @SuppressLint("MissingPermission")
    private fun setUpDialogPermission() {
        val dialog = DialogPermission(object : DialogCallBack.OnDismissListener {
            override fun onDismiss(var1: DialogInterface?) {
                mMap.isMyLocationEnabled = true
                mainViewModel.handel(MainViewAction.GetCurrentLocation)
            }
        }).apply {
            isCancelable = false
        }
        dialog.show(supportFragmentManager, dialog::class.java.name)
    }

    @SuppressLint("MissingPermission")
    private fun setUpGoogleMap() {
        val supportFragmentManager = supportFragmentManager.findFragmentById(binding.mFragment.id)
        val fragmentMapManager = supportFragmentManager as SupportMapFragment
        fragmentMapManager.getMapAsync { it ->
            mMap = it
            //setStylingCustom Google map
            val mapStyleOptions = MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style)
            it.setMapStyle(mapStyleOptions)

            if (hasPermissionLocation()) {
                mMap.isMyLocationEnabled = false
                mainViewModel.handel(MainViewAction.GetCurrentLocation)
            }

            mMap.uiSettings.apply {
                isMapToolbarEnabled = false
                isMyLocationButtonEnabled = false
            }

            //manager marker and animation marker
            clusterManager = ClusterManager<MapInfo>(this, mMap)
            clusterManager.renderer = ClusterMarkerRenderer(this, mMap, clusterManager)
            mainViewModel.handel(MainViewAction.GetMapInfo)
            clusterManager.setOnClusterItemClickListener {
                clusterManager.updateItem(listItem[listItem.indexOf(it)].apply {
                    mapInfo?.isShowInfo = !it.isShowInfo
                })
                clusterManager.cluster()
                false
            }
        }
    }


    override fun initObserver() {
        val context = this@MainActivity
        with(mainViewModel) {
            friendsUser.observe(context) { result ->
                when (result) {
                    is NetworkResult.Success -> {
                        val body = result.data
                        if (!body.isNullOrEmpty()) {
                            users = body.toMutableList()
                            users.add(userCurrent!!)
                            users.forEach { userRqe ->
                                Log.d("Configsa", userRqe._id!!)

                                configureAndAddItemMarker(userRqe)
                            }
                        }
                        dismissDialogLoading()
                    }

                    is NetworkResult.Error -> {
                        dismissDialogLoading()
                    }

                    is NetworkResult.Loading -> {
                        showDialogLoading()
                    }

                    else -> {}
                }
            }
            onConnectSocket.observe(context) {
                if (it) {
                    handleSocket(SocketAction.LocationChanges)
                    handleSocket(SocketAction.OnlineStatusUpdates)
                    handleSocket(SocketAction.OfflineStatusUpdates)
                }
            }

            onFriendsOnline.observe(context) {
                if (it != null) {
                    val itemMarker = listMarkerBindings[getIndexById(it)]
                    val index = getIndexById(it)
                    setStatusUser(it, true)
                    handleCapture(MainViewAction.StartLiveCapture(itemMarker.root, it, false))
                }
            }

            onFriendsOffline.observe(context) {
                if (it != null) {
                    val index = getIndexById(it)
                    setStatusUser(it, false)
                    handleCapture(MainViewAction.StopLiveCapture(it))
                }
            }

            currentLocationLiveData.observe(context) {
                if (it != null) mMap.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        it,
                        Constants.ZOOM_MAP_DEFAULT
                    )
                )
            }

            onReceiverLocation.observe(context) { mInfo ->
                val index = getIndexById(mInfo._idUser)
                updateMarker(index, mInfo)
                clusterManager.updateItem(getMapInfoById(mInfo)?.apply {
                    mapInfo = mInfo
                })
            }
            //send location
            infoLiveLocationLiveData.observe(context) { mapInfo ->
                val index = getIndexById(userCurrent!!._id!!)
                updateMarker(index, mapInfo)
                handleSocket(SocketAction.SendLocation(mapInfo, mapInfo.batteryInfo!!))
            }
        }
    }

    private fun setStatusUser(idUser: String, statusUser: Boolean) {
        val index = getIndexById(idUser)
        val itemMarkerBinding = listMarkerBindings[index]
        itemMarkerBinding.statusUser = if (idUser != userCurrent!!._id) statusUser else true
    }

    private fun getIndexById(id: String) =
        listItem.indexOfFirst { mapInfo -> mapInfo._idUser == id }

    private fun getMapInfoById(mapInfo: MapInfo) = listItem.find { it._idUser == mapInfo._idUser }
    private fun configureAndAddItemMarker(userRequest: UserRequest) {
        createAndAddItemMarkerBinding(userRequest)
        val childViewCount = binding.mLinearLayout.childCount
        val mapInfo = userRequest.let { it ->
            MapInfo(
                it._id!!,
                it.name,
                false,
                105.7468539 + childViewCount,
                21.0056775 + childViewCount
            )
        }
        // Thêm MapInfoResponse vào danh sách và cluster
        Log.d("Configsa", userRequest._id!!)
        listItem.add(mapInfo)
        clusterManager.addItem(mapInfo)
        clusterManager.cluster()
    }

    private fun handleLiveCapture(view: View, id: String, oneShot: Boolean) {
        // Cập nhật item trong cluster
        liveBitmapScreens.startCapture(view, id, oneShot) { bitmap, index ->
            val index = binding.mLinearLayout.indexOfChild(view)
            val itemToUpdate = listItem[index]
            itemToUpdate.icon = BitmapDescriptorFactory.fromBitmap(bitmap)
            clusterManager.updateItem(itemToUpdate)
            if (index == 1) clusterManager.cluster()
        }
    }

    private fun handleStopCapture(id: String) {
        liveBitmapScreens.stopCapture(id)
    }

    private fun createAndAddItemMarkerBinding(userRequest: UserRequest): View {
        // Tạo và thêm ItemMarkerBinding vào LinearLayout
        val clonedChildView =
            LayoutInflater.from(this).inflate(R.layout.item_marker, binding.mLinearLayout, false)
        val itemMarkerBinding = ItemMarkerBinding.bind(clonedChildView)
        val img = itemMarkerBinding.cardView
        img.startAnimation(AnimationUtils.loadAnimation(this, R.anim.bonce))
        binding.mLinearLayout.addView(clonedChildView)
        listMarkerBindings.add(itemMarkerBinding)
        Picasso.get().load(userRequest.avatar).fit().into(img, object : Callback {
            override fun onSuccess() {
                oneShotCapture(userRequest._id!!, userRequest.status)
            }

            override fun onError(e: java.lang.Exception?) {

            }
        })
        return clonedChildView
    }

    private fun oneShotCapture(id: String, statusUser: Boolean) {
        val handel = Handler(Looper.getMainLooper())
        handel.postDelayed({
            val index = getIndexById(id)
            val markerBinding = listMarkerBindings[index]
            val oneShot = if (id != userCurrent!!._id) !statusUser else false
            setStatusUser(id, statusUser)
            handleCapture(MainViewAction.StartLiveCapture(markerBinding.root, id, oneShot))
        }, 500)
    }

    private fun updateMarker(index: Int, mapInfo: MapInfo) {
        //setUpData
        if (index <= listMarkerBindings.size - 1 && index != -1) {
            listMarkerBindings[index].apply {
                this.mapInfo = mapInfo
                battery = mapInfo.batteryInfo
            }
        }
    }

    private fun handleCapture(action: MainViewAction) {
        when (action) {
            is MainViewAction.StartLiveCapture -> handleLiveCapture(
                action.viewCapture, action.id, action.oneShot
            )

            is MainViewAction.StopLiveCapture -> handleStopCapture(action.id)
            else -> {
                throw Exception("Not Found Action Capture")
            }
        }
    }

    override fun getViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }
}