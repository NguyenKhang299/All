package com.image.effect.timewarp.scan.filtertiktok.face.filter.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.image.effect.timewarp.scan.filtertiktok.face.filter.R
import com.tbruyelle.rxpermissions3.RxPermissions

class PermUtils {
    companion object {

        fun hasPermissions(context: Context, vararg permissions: String): Boolean {
            for (permission in permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) !== PackageManager.PERMISSION_GRANTED) {
                    return false
                }
            }
            return true;
        }

        fun showDialogPermissionDenied(activity: Activity?, closedCallback: Runnable? = null) {
            activity?.let {
                AlertDialog.Builder(activity)
                    .setTitle(R.string.permission_denied)
                    .setMessage(R.string.enalble_permissons_msg)
                    .setPositiveButton(R.string.go_to_settings) { _, _ ->
                        activity.startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:${activity.packageName}")))
                        closedCallback?.run()
                    }
                    .setNegativeButton(R.string.cancel) { _, _ ->
                        closedCallback?.run()
                    }
                    .show()
            }
        }
    }
}