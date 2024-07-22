package com.knd.duantotnghiep.phonetrackerlocation.ui.videoCall

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.SurfaceView
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import com.google.firebase.messaging.FirebaseMessaging
import com.knd.duantotnghiep.phonetrackerlocation.R
import com.knd.duantotnghiep.phonetrackerlocation.core.BaseActivity
import com.knd.duantotnghiep.phonetrackerlocation.databinding.ActivityVideoCallBinding
import com.knd.duantotnghiep.phonetrackerlocation.models.Notification
import com.knd.duantotnghiep.phonetrackerlocation.models.RtcResponse
import com.knd.duantotnghiep.phonetrackerlocation.models.UserRequest
import com.knd.duantotnghiep.phonetrackerlocation.notification.HandleNotification
import com.knd.duantotnghiep.phonetrackerlocation.notification.MyNotificationManager
import com.knd.duantotnghiep.phonetrackerlocation.notification.NotificationAction
import com.knd.duantotnghiep.phonetrackerlocation.utils.ActivityExtensions.showMessage
import com.knd.duantotnghiep.phonetrackerlocation.utils.Constants
import com.knd.duantotnghiep.phonetrackerlocation.utils.NetworkResult
import com.knd.duantotnghiep.phonetrackerlocation.utils.Permission
import com.knd.duantotnghiep.phonetrackerlocation.utils.Permission.hasPermissionCamera
import com.knd.duantotnghiep.phonetrackerlocation.utils.Permission.hasPermissionRecordAudio
import com.knd.duantotnghiep.phonetrackerlocation.utils.TokenManager
import com.knd.duantotnghiep.phonetrackerlocation.utils.UserPreferencesManager
import com.knd.duantotnghiep.phonetrackerlocation.utils.getSerializableExtra
import dagger.hilt.android.AndroidEntryPoint
import io.agora.rtc2.ChannelMediaOptions
import io.agora.rtc2.IRtcEngineEventHandler
import io.agora.rtc2.RtcEngine
import io.agora.rtc2.RtcEngineConfig
import io.agora.rtc2.internal.AgoraExtension
import io.agora.rtc2.video.VideoCanvas
import javax.inject.Inject

@AndroidEntryPoint
class VideoCall : BaseActivity<ActivityVideoCallBinding>() {
    // Fill the App ID of your project generated on Agora Console.

    @Inject
    lateinit var tokenManager: TokenManager

    @Inject
    lateinit var upm: UserPreferencesManager
    private val videoCallViewModel by viewModels<VideoCallViewModel>()
    private var isJoined = false

    private var agoraEngine: RtcEngine? = null

    //SurfaceView to render local video in a Container.
    private var localSurfaceView: SurfaceView? = null

    //SurfaceView to render Remote video in a Container.
    private var remoteSurfaceView: SurfaceView? = null

    //  sender information
    private lateinit var infoSender: Notification
    private lateinit var rtcResponse: RtcResponse
    private lateinit var id_other: String
    override fun initData() {
        val callType = intent.getSerializableExtra<CallType>("CallType")
        when (callType!!) {
            CallType.INCOMING -> {
                infoSender = intent.getSerializableExtra<Notification>("InfoSender")!!
                id_other = infoSender._id_user_sender
                binding.mRelativeLayout.visibility = View.GONE
            }

            CallType.OUTGOING -> {
                id_other = intent.getStringExtra("id_other")!!
                binding.overlayIncoming.root.visibility = View.GONE
            }
        }
    }

    private val mRtcHandler: IRtcEngineEventHandler = object : IRtcEngineEventHandler() {
        override fun onUserJoined(uid: Int, elapsed: Int) {
            super.onUserJoined(uid, elapsed)
            runOnUiThread {
                showMessage("Remote user joined $uid")
                setupRemoteVideo(uid)
            }
        }

        override fun onJoinChannelSuccess(channel: String, uid: Int, elapsed: Int) {
            super.onJoinChannelSuccess(channel, uid, elapsed)
            isJoined = true
            runOnUiThread { showMessage("Joined Channel $channel") }
        }

        override fun onUserOffline(uid: Int, reason: Int) {
            super.onUserOffline(uid, reason)
            runOnUiThread {
                remoteSurfaceView!!.visibility = View.GONE
                showMessage("Remote user offline $uid $reason")
            }
        }

        override fun onError(err: Int) {
            super.onError(err)
            when (err) {
                ErrorCode.ERR_INVALID_TOKEN -> runOnUiThread { showMessage("ERR_INVALID_TOKEN") }
                ErrorCode.ERR_TOKEN_EXPIRED -> {}
            }
        }
    }

    override fun initObserver() {
        with(videoCallViewModel) {

            onGetTokenRtc.observe(this@VideoCall) { result ->
                when (result) {
                    is NetworkResult.Success -> {
                        //return result
                        rtcResponse = result.data!!
                        joinChannel(null)
                    }

                    is NetworkResult.Error -> {
                        // Handle error: $result.message$
                        showMessage(result.message)
                    }

                    is NetworkResult.Loading -> {
                        // Show loading UI
                    }
                }
            }
        }
    }

    private fun setupVideoSDKEngine() {
        try {
            val config = RtcEngineConfig()
            config.mContext = baseContext
            config.mAppId = getString(R.string.app_id_agora)
            config.mEventHandler = mRtcHandler
            agoraEngine = RtcEngine.create(config)
            agoraEngine!!.enableVideo()
        } catch (e: java.lang.Exception) {
            showMessage(e.toString())
        }
    }

    private fun setupLocalVideo() {
        val container = binding.localVideoViewContainer
        localSurfaceView = SurfaceView(baseContext)
        container.addView(localSurfaceView)
        agoraEngine!!.setupLocalVideo(
            VideoCanvas(
                localSurfaceView,
                VideoCanvas.RENDER_MODE_HIDDEN,
                0
            )
        )
    }

    private fun setupRemoteVideo(uid: Int) {
        val container = binding.remoteVideoViewContainer
        remoteSurfaceView = SurfaceView(baseContext)
        remoteSurfaceView!!.setZOrderMediaOverlay(true)
        container.addView(remoteSurfaceView)
        agoraEngine!!.setupRemoteVideo(
            VideoCanvas(
                remoteSurfaceView,
                VideoCanvas.RENDER_MODE_FIT,
                uid
            )
        )
        remoteSurfaceView!!.visibility = View.VISIBLE
    }


    fun joinChannel(view: View?) {
        if (hasPermissionRecordAudio() && hasPermissionCamera()) {
            val options = ChannelMediaOptions()
            // For a Video call, set the channel profile as COMMUNICATION.
            options.channelProfile = io.agora.rtc2.Constants.CHANNEL_PROFILE_COMMUNICATION
            // Set the client role as BROADCASTER or AUDIENCE according to the scenario.
            options.clientRoleType = io.agora.rtc2.Constants.CLIENT_ROLE_BROADCASTER
            setupLocalVideo()
            localSurfaceView!!.visibility = View.VISIBLE
            agoraEngine!!.startPreview()
            agoraEngine!!.joinChannel(
                videoCallViewModel.tokenRtc.value,
                rtcResponse.channelId,
                rtcResponse.uidUser,
                options
            )
        }
    }

    fun leaveChannel(view: View?) {
        if (!isJoined) {
            showMessage("Join a channel first")
        } else {
            agoraEngine!!.leaveChannel()
            showMessage("You left the channel")
            if (remoteSurfaceView != null) remoteSurfaceView!!.visibility = View.GONE
            if (localSurfaceView != null) localSurfaceView!!.visibility = View.GONE
            isJoined = false
        }
    }

    override fun initView() {
        setupVideoSDKEngine()
        FirebaseMessaging.getInstance().token.addOnSuccessListener {
            Log.d("okoosnhe", it.toString())
        }
        with(binding.overlayIncoming) {

            imgCall.setOnClickListener {
                videoCallViewModel.handle(VideoCallAction.GetTokenRtc(id_other))
                binding.mRelativeLayout.visibility = View.VISIBLE
            }

            imgReject.setOnClickListener {
                finish()
            }
        }
    }

    override fun getViewBinding(): ActivityVideoCallBinding {
        return ActivityVideoCallBinding.inflate(layoutInflater)
    }

    override fun onResume() {
        super.onResume()
        when (intent.getSerializableExtra<NotificationAction>("action")) {
            is NotificationAction.ActionDecline -> {

            }

            is NotificationAction.ActionAnswer -> {
                videoCallViewModel.handle(VideoCallAction.GetTokenRtc(id_other))
            }

            else -> {}
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        agoraEngine!!.stopPreview()
        agoraEngine!!.leaveChannel()
        // Destroy the engine in a sub-thread to avoid congestion
        Thread {
            RtcEngine.destroy()
            agoraEngine = null
        }.start()
    }
}