package com.knd.duantotnghiep.phonetrackerlocation.ui.dialog

import android.content.DialogInterface
import android.view.View

import androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions
import androidx.activity.result.contract.ActivityResultContracts.StartIntentSenderForResult
import androidx.appcompat.app.AppCompatActivity
import com.knd.duantotnghiep.phonetrackerlocation.R
import com.knd.duantotnghiep.phonetrackerlocation.core.BaseDialogFragment
import com.knd.duantotnghiep.phonetrackerlocation.databinding.DialogPermissionBinding
import com.knd.duantotnghiep.phonetrackerlocation.utils.ActivityExtensions.showMessage
import com.knd.duantotnghiep.phonetrackerlocation.utils.Permission
import com.knd.duantotnghiep.phonetrackerlocation.utils.Permission.hasEnableGPS
import com.knd.duantotnghiep.phonetrackerlocation.utils.Permission.turnOnGPS

class DialogPermission(private val dismissListener: DialogCallBack.OnDismissListener) : BaseDialogFragment<DialogPermissionBinding>(R.layout.dialog_permission) {
    private val registerPermission =
        registerForActivityResult(RequestMultiplePermissions()) { result ->
            if (result.values.all { it })
                if (!requireContext().hasEnableGPS())
                    requireActivity().turnOnGPS() { success, isRequestRequired ->
                        if (!success && isRequestRequired != null)
                            registerTurnOnGPS.launch(isRequestRequired)
                    }
                else dismiss()
            else
                requireActivity().showMessage("Permission not granted")

        }

    private val registerTurnOnGPS = registerForActivityResult(StartIntentSenderForResult()) { rs ->
        if (rs.resultCode == AppCompatActivity.RESULT_OK)
            dismiss()
        else
            requireActivity().showMessage("Permission not granted")
    }


    override fun getViewBinding(view: View): DialogPermissionBinding {
        return DialogPermissionBinding.bind(view)
    }

    override fun initView() {
        with(binding) {
            btnExits.setOnClickListener {
                dismiss()
            }

            btnGrant.setOnClickListener {
                registerPermission.launch(
                    arrayOf(
                        Permission.ACCESS_FINE_LOCATION,
                        Permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        dismissListener.onDismiss(dialog)
    }
}