package com.image.effect.timewarp.scan.filtertiktok.face.filter.util

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.codemybrainsout.ratingdialog.RatingDialog
import com.image.effect.timewarp.scan.filtertiktok.face.filter.Events
import com.image.effect.timewarp.scan.filtertiktok.face.filter.Pref
import com.orhanobut.hawk.Hawk

class UIUtils {
    companion object {
        fun showRatingDialog(activity: Activity) {
            var rate = 0f
            RatingDialog.Builder(activity)
                .threshold(4)
                .onRatingBarFormSubmit { feedback ->
                    Toast.makeText(activity, "Thanks for your feedback", Toast.LENGTH_LONG).show()
                    Events.log(Events.Feedback, Bundle().apply {
                        putInt("rate", rate.toInt())
                        putString("message", feedback)
                    })
                }
                .onRatingChanged { rating, thresholdCleared ->
                    Hawk.put(Pref.AppRated, true)
                    rate = rating
                }
                .build()
                .show()
        }
    }
}