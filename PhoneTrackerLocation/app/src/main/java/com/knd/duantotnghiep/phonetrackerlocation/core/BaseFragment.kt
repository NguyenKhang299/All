package com.knd.duantotnghiep.phonetrackerlocation.core

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Message
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.knd.duantotnghiep.phonetrackerlocation.utils.Constants
import dagger.hilt.android.AndroidEntryPoint

abstract class BaseFragment<VB : ViewBinding>(layout: Int) : Fragment(layout) {
    private var _binding: VB? = null
    private var requestCode = 0
    open val binding get() = _binding!!
    private val registerForResult =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            if (result?.resultCode == Activity.RESULT_OK && requestCode != 0) {
                when (requestCode) {
                    Constants.RQE_GOOGLE -> {
                        resultSignInGoogle(result)
                    }

                    Constants.RQE_ON_GPS -> {

                    }
                }
            } else {
                fragmentResultCompletion("User exits", false)
            }
        }

    private fun resultSignInGoogle(result: ActivityResult) {
        try {
            val credential = Identity.getSignInClient(requireActivity())
                .getSignInCredentialFromIntent(result.data)
            val idToken = credential.googleIdToken

            if (idToken != null) {
                fragmentResultCompletion(idToken, true)

            } else {
                fragmentResultCompletion("Error", false)
            }
        } catch (e: ApiException) {
            fragmentResultCompletion(e.localizedMessage, false)
        }
    }

   open fun launchRegister(intentSenderRequest: IntentSenderRequest, reqCode: Int) {
        requestCode = reqCode
        registerForResult.launch(intentSenderRequest)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = getViewBinding(view)
        initData()
        initView()
        initObserver()
    }


    open var fragmentResultCompletion: (String?, Boolean) -> Unit = { param, s -> }


    fun launchFragment(id: Int) = try {
        findNavController().navigate(id)
    } catch (e: Exception) {
        Log.d("okokok", e.localizedMessage)
    }

    fun popBackStack() = findNavController().popBackStack()

    open fun initObserver() {}

    abstract fun getViewBinding(view: View): VB
    open fun initView() {}
    open fun initData() {}
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}