package com.knd.duantotnghiep.phonetrackerlocation.ui.account.signIn

import android.annotation.SuppressLint
import android.app.Activity
import android.view.View
import android.widget.LinearLayout
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.common.api.ApiException

import com.knd.duantotnghiep.phonetrackerlocation.core.BaseActivity
import com.knd.duantotnghiep.phonetrackerlocation.databinding.ActivityLoginBinding
import com.knd.duantotnghiep.phonetrackerlocation.models.UserResponse
import com.knd.duantotnghiep.phonetrackerlocation.ui.account.AccountEvent
import com.knd.duantotnghiep.phonetrackerlocation.ui.main.MainActivity
import com.knd.duantotnghiep.phonetrackerlocation.utils.NetworkResult
import com.knd.duantotnghiep.phonetrackerlocation.utils.UserPreferencesManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import com.knd.duantotnghiep.phonetrackerlocation.ui.account.AccountAction.SignInAction
import com.knd.duantotnghiep.phonetrackerlocation.ui.account.signUp.SignUpActivity
 import com.knd.duantotnghiep.phonetrackerlocation.utils.ActivityExtensions.showMessage
import com.knd.duantotnghiep.phonetrackerlocation.utils.ActivityExtensions.startActivity
import com.knd.duantotnghiep.phonetrackerlocation.utils.Constants
import com.knd.duantotnghiep.phonetrackerlocation.utils.DialogUtils.dismissDialogLoading
import com.knd.duantotnghiep.phonetrackerlocation.utils.DialogUtils.showDialogLoading
import com.knd.duantotnghiep.phonetrackerlocation.utils.ObserveError.observeErrorLiveData
import com.knd.duantotnghiep.phonetrackerlocation.utils.TokenManager


@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding>() {
    var activityResultCompletion: (String?, Boolean) -> Unit = { param, s -> }
    private fun resultSignInGoogle(result: ActivityResult) {
        try {
            val credential = Identity.getSignInClient(this).getSignInCredentialFromIntent(result.data)
            val idToken = credential.googleIdToken
            when {
                idToken != null -> {
                    activityResultCompletion(idToken, true)
                }

                else -> {
                    activityResultCompletion("Cannot SignIn", false)
                }
            }
        } catch (e: ApiException) {
            activityResultCompletion(e.localizedMessage, false)
        }
    }


    private val registerForResult =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            if (result?.resultCode == Activity.RESULT_OK) {
                resultSignInGoogle(result)
            }
        }

    init {
        activityResultCompletion = { rs, b ->
            loginViewModel._resultSignInWithGoogleClient.postValue(
                if (b) NetworkResult.Success(rs) else NetworkResult.Error(rs)
            )
        }
    }

    private val loginViewModel by viewModels<LoginViewModel>()

    @Inject
    lateinit var userPreferencesManager: UserPreferencesManager


    override fun getViewBinding(): ActivityLoginBinding {
        return ActivityLoginBinding.inflate(layoutInflater)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun initView() {
        with(binding) {

            imgBack.setOnClickListener { finish() }

            btnLaunchGetStarted.setOnClickListener { startActivity(SignUpActivity::class.java) }

            btnLoginForPass.setOnClickListener {
                loginViewModel.handleAccountAction(SignInAction.SignInWithPassword)
            }

            btnLoginGg.setOnClickListener {
                loginViewModel.handleAccountAction(
                    SignInAction.SignInWithGoogle(this@LoginActivity)
                )
            }

            btnLoginFb.setOnClickListener {
                loginViewModel.handleAccountAction(
                    SignInAction.SignInWithFacebook(this@LoginActivity)
                )
            }

            countryCode.setDialogKeyboardAutoPopup(false)

        }
    }

    override fun initObserver() {
        with(loginViewModel) {
            observeErrorLiveData(errorPhoneNumber, binding.txtInputPhone)
            observeErrorLiveData(errorPassword, binding.textInputPass)
            val activity = this@LoginActivity
            txtPassword.observe(activity) {
                binding.textInputPass.error = null
            }

            txtPhoneNumber.observe(activity) {
                binding.txtInputPhone.error = null
            }

            intentSenderRequestLiveData.observe(activity) { result ->
                if (result is NetworkResult.Success) {
                    registerForResult.launch(result.data!!)
                 }
            }

            resultAuthenticateFacebookToken.observe(activity) { result ->
                handleNetworkResult(result)
            }

            resultAuthenticateGoogleToken.observe(activity) { result ->
                handleNetworkResult(result)
            }

            //xử lí kết quả khi click signIn password
            //processing results when clicking sign In password
            resultSignInWithPass.observe(activity) { result ->
                handleNetworkResult(result)
            }

            resultSignInWithFaceBookClient.observe(activity) { result ->
                handleSocialMediaSignInResult(
                    result,
                    AccountEvent.ClickSignInFacebook,
                    this@LoginActivity
                )
            }

            _resultSignInWithGoogleClient.observe(activity) { result ->
                handleSocialMediaSignInResult(
                    result,
                    AccountEvent.ClickSignInGoogle,
                    this@LoginActivity
                )
            }
        }
    }


    private fun handleNetworkResult(
        result: NetworkResult<UserResponse>,
    ) {
        val overlayView = binding.overlayView
        when (result) {
            is NetworkResult.Success -> {
                dismissDialogLoading()
                userPreferencesManager.saveUser(result.data!!.userRequest)
                startActivity(MainActivity::class.java, false)
            }

            is NetworkResult.Error -> {
                dismissDialogLoading()
                showMessage(result.message.toString())
            }

            is NetworkResult.Loading -> {
                showDialogLoading()
            }
        }
    }

    override fun initData() {
        binding.viewModel = loginViewModel
        binding.lifecycleOwner = this
    }
}