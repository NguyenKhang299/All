package com.knd.duantotnghiep.phonetrackerlocation.utils

import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import com.google.android.material.textfield.TextInputLayout

object ObserveError {
    fun FragmentActivity.observeErrorLiveData(
        errorLiveData: LiveData<String>?=null,
        textInputLayout: TextInputLayout,
        textChange: ((String) -> Unit)? = null)
    {
        errorLiveData?.observe(this) { errorMessage ->
            textInputLayout.apply {
                isErrorEnabled = errorMessage.isNotEmpty()
                error = errorMessage
            }
            textInputLayout.editText!!.addTextChangedListener {
                if (textChange != null) {
                    textChange(it.toString())
                }
            }
        }
    }
}