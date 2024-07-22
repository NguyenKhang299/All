package com.knd.duantotnghiep.phonetrackerlocation.utils

import android.annotation.SuppressLint
import androidx.core.widget.addTextChangedListener
import com.google.android.material.textfield.TextInputLayout

interface OnTextChangeCallBack {
    fun onTextChange(textInputLayout: List<TextInputLayout>, callback: (String, Int) -> Unit)
}

class TextChangeListener() : OnTextChangeCallBack {
    @SuppressLint("NotConstructor")
    override fun onTextChange(
        textInputLayout: List<TextInputLayout>,
        callback: (String, Int) -> Unit
    ) {
        for (i in textInputLayout) {
            i.editText?.addTextChangedListener(onTextChanged = { t, _, _, count ->
                callback(t.toString(), i.id)
            })
        }
    }
}