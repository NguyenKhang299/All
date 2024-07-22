package com.knd.duantotnghiep.phonetrackerlocation.utils

import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentActivity
import com.knd.duantotnghiep.phonetrackerlocation.models.UserRequest
import java.io.Serializable


object ActivityExtensions {
    fun PickImageOnly() = PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)

    fun <T> FragmentActivity.startActivity(
        activity: Class<T>,
        destroyCurrentActivity: Boolean = true
    ) {
        if (destroyCurrentActivity) this.finish()
        startActivity(Intent(this, activity))
    }

    fun FragmentActivity.showMessage(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}