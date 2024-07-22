package com.knd.duantotnghiep.phonetrackerlocation.utils

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

fun ViewModel.launch(call: suspend () -> Unit) {
    viewModelScope.launch {
        call.invoke()
    }
}




