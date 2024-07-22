package com.knd.duantotnghiep.phonetrackerlocation.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

class TextWatcher(private val preserveText: String , private val editText: EditText) :  TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        editText.error=null
    }

    override fun afterTextChanged(s: Editable?) {
        if (s != null) {
            if (s.length < preserveText.length) {
                editText.setText("+");
                editText.setSelection(s.length + 1);
            }
        }
    }
}