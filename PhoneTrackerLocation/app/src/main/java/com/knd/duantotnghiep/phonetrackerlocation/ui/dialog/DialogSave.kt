package com.knd.duantotnghiep.phonetrackerlocation.ui.dialog

import android.util.Log
import android.view.View
import androidx.fragment.app.FragmentTransaction
import com.knd.duantotnghiep.phonetrackerlocation.R
import com.knd.duantotnghiep.phonetrackerlocation.core.BaseDialogFragment
import com.knd.duantotnghiep.phonetrackerlocation.databinding.DialogSaveBinding
interface DialogSaveCallBack{
    fun onSaveClick()
}
class DialogSave(private val dialogSaveCallBack: DialogSaveCallBack)  :BaseDialogFragment<DialogSaveBinding>(R.layout.dialog_save){
    override fun getViewBinding(view: View): DialogSaveBinding {
        return DialogSaveBinding.bind(view)
    }

    override fun initView() {
         with(binding){
             btnSave.setOnClickListener {
                 dialogSaveCallBack.onSaveClick()
             }

             btnDiscard.setOnClickListener {
                 Log.d("okookokok","plplpl")
                 requireActivity().finish()
             }
         }
    }

}