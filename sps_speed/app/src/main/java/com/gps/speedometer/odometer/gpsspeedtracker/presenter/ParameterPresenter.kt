package com.gps.speedometer.odometer.gpsspeedtracker.presenter

import android.app.ActivityManager
import android.app.Service
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.gps.speedometer.odometer.gpsspeedtracker.constants.MyLocationConstants
import com.gps.speedometer.odometer.gpsspeedtracker.dao.MyDataBase
import com.gps.speedometer.odometer.gpsspeedtracker.interfaces.ParameterContracts
import com.gps.speedometer.odometer.gpsspeedtracker.model.MovementData
import com.gps.speedometer.odometer.gpsspeedtracker.`object`.SharedData
import com.gps.speedometer.odometer.gpsspeedtracker.service.MyService
import java.text.SimpleDateFormat

class ParameterPresenter(
    private val context: Fragment,
    private val view: ParameterContracts.View
) : ParameterContracts.Presenter {
    private var sharedPreferences: SharedPreferences =
        context.requireContext().getSharedPreferences("state", Service.MODE_PRIVATE)
    private var myDataBase: MyDataBase = MyDataBase.getInstance(context.requireContext())
    override fun showReset() {
         SharedData.onShowResetButton.observe(context.viewLifecycleOwner){
             view.onShowReset(it)
         }
    }

    override fun getMaxSpeed() {
        SharedData.maxSpeedLiveData.observe(context.viewLifecycleOwner) {
            view.onShowMaxSpeed(
                if (it <= 0) "0" + SharedData.toUnit else String.format(
                    "%.0f",
                    SharedData.convertSpeed(it)
                ) + SharedData.toUnit
            )
        }
    }

    override fun getDistance() {
        SharedData.distanceLiveData.observe(context.viewLifecycleOwner) {
           if(it!=null){
               view.onShowDistance(
                   String.format(
                       "%.2f",
                       SharedData.convertDistance(it)
                   ) + SharedData.toUnitDistance
               )
           }
        }
    }

    override fun getAverageSpeed() {
        SharedData.averageSpeedLiveData.observe(context.viewLifecycleOwner) {
            view.onShowAverageSpeed(
                if (it <= 0) "0" + SharedData.toUnit else String.format(
                    "%.0f",
                    SharedData.convertSpeed(it)
                ) + SharedData.toUnit
            )
        }
    }

    override fun timeStart() {
        SharedData.onTimeStart.observe(context.viewLifecycleOwner) {
            view.onTimeStart(it)
        }
    }
    override fun callMyService(action: String) {
        val intent = Intent(context.requireContext(), MyService::class.java)
        intent.action = action
        context.requireContext().startService(intent)
    }

    override fun insertMovementDataWhenStart() {
        val timeStart=System.currentTimeMillis()
        myDataBase.movementDao().insertMovementData(
            MovementData(
                0,
                timeStart,
                0.0,
                0.0,
                0.0,
                0.0,
                0.0,
                0.0,
                0.0,
                0
            )
        )
        SharedData.onTimeStart.value =   SimpleDateFormat("dd/MM/yyyy\nHH:mm:ss").format(timeStart)
    }

    override fun startService() {
        insertMovementDataWhenStart()
        setState(MyLocationConstants.START)
        callMyService(MyLocationConstants.START)
        hideStart()
    }


    override fun stopService() {
        setState(MyLocationConstants.STOP)
        callMyService(MyLocationConstants.STOP)
        showStart()
        SharedData.onTimeStart.value= SimpleDateFormat("dd/MM/yyyy\n00:00:00").format(System.currentTimeMillis())
    }


    override fun updateUIState() {
        when (sharedPreferences?.getString(MyLocationConstants.STATE, null)) {
            MyLocationConstants.START -> {
                hideStart()
            }

            MyLocationConstants.PAUSE -> {
                hidePause()
            }

            MyLocationConstants.RESUME -> {
                hideResume()
            }

            MyLocationConstants.STOP -> {
                showStart()
            }
        }
    }

    override fun pauseService() {
        setState(MyLocationConstants.PAUSE)
        callMyService(MyLocationConstants.PAUSE)
        hidePause()
    }


    override fun resumeService() {
        setState(MyLocationConstants.RESUME)
        callMyService(MyLocationConstants.RESUME)
        hideResume()
    }

    private fun hidePause() {
        view.onHidePause()
        view.onShowResume()
        view.onShowReset()
    }

    private fun hideResume() {
        view.onHideResume()
        view.onShowPause()
    }

    private fun showStart() {
        view.onShowStart()
        view.onHideStop()
        view.onHidePause()
        view.onHideResume()
        view.onHideReset()
    }

    private fun hideStart() {
        view.onHideStart()
        view.onShowStop()
        view.onShowReset()
        view.onShowPause()
    }

    override fun setState(state: String) {
        sharedPreferences.edit().putString(MyLocationConstants.STATE, state).apply()
    }

    override fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager =
            context.activity?.getSystemService(AppCompatActivity.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }


}