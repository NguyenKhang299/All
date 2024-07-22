package com.knd.duantotnghiep.phonetrackerlocation.ui.account.signUp.name

import android.content.Context
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.knd.duantotnghiep.phonetrackerlocation.R
import com.knd.duantotnghiep.phonetrackerlocation.ui.account.AccountAction
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class ConfirmNameViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {
    val txtFirstName by lazy {
        MutableLiveData<String>()
    }
    val txtLastName by lazy {
        MutableLiveData<String>()
    }
    val txtSuggestName by lazy {
        MutableLiveData(context.getString(R.string.enter_your_name_in_real_life))
    }
    val actionNext by lazy {
        MutableLiveData<Boolean>()
    }
    val enableError by lazy {
        MutableLiveData (false)
    }
    fun confirmName(view: View) {
        val firstName = txtFirstName.value?: ""
        val lastName = txtLastName.value ?: ""

        when {
            firstName.isEmpty() || lastName.isEmpty() -> {
                txtSuggestName.postValue(context.getString(R.string.error_name_blank))
                enableError.postValue(true)
             }

            firstName.isNotEmpty() && lastName.isNotEmpty() -> {
                actionNext.postValue(true)
                enableError.postValue(false)
                txtSuggestName.postValue(context.getString(R.string.enter_your_name_in_real_life))
            }
        }
    }
}