package com.knd.duantotnghiep.phonetrackerlocation.utils

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

object ValidateUtils {
    fun Any.validatePhoneNumber():Boolean{
          return Patterns.PHONE.matcher(this.toString()).matches()
    }
    fun Any.validatePassword(): Boolean {
        val regex = Regex("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,}$")
        Log.d("okokok", regex.matches(this.toString()).toString())
        return regex.matches(this.toString())
    }
}
