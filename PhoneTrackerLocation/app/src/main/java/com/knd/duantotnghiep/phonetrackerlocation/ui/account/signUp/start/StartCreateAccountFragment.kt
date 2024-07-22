package com.knd.duantotnghiep.phonetrackerlocation.ui.account.signUp.start

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import com.knd.duantotnghiep.phonetrackerlocation.R
import com.knd.duantotnghiep.phonetrackerlocation.core.BaseFragment
import com.knd.duantotnghiep.phonetrackerlocation.databinding.FragmentStartCreateAccountBinding
import com.knd.duantotnghiep.phonetrackerlocation.models.UserResponse
import com.knd.duantotnghiep.phonetrackerlocation.ui.account.AccountAction
import com.knd.duantotnghiep.phonetrackerlocation.ui.account.AccountAction.SignUpAction
import com.knd.duantotnghiep.phonetrackerlocation.ui.account.AccountEvent
import com.knd.duantotnghiep.phonetrackerlocation.ui.account.AccountSharedViewModel
import com.knd.duantotnghiep.phonetrackerlocation.ui.account.signIn.LoginActivity
import com.knd.duantotnghiep.phonetrackerlocation.ui.main.MainActivity
import com.knd.duantotnghiep.phonetrackerlocation.utils.ActivityExtensions.showMessage
import com.knd.duantotnghiep.phonetrackerlocation.utils.ActivityExtensions.startActivity
import com.knd.duantotnghiep.phonetrackerlocation.utils.Constants
import com.knd.duantotnghiep.phonetrackerlocation.utils.DialogUtils.dismissDialogLoading
import com.knd.duantotnghiep.phonetrackerlocation.utils.DialogUtils.showDialogLoading
import com.knd.duantotnghiep.phonetrackerlocation.utils.NetworkResult
import com.knd.duantotnghiep.phonetrackerlocation.utils.UserPreferencesManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class StartCreateAccountFragment :
    BaseFragment<FragmentStartCreateAccountBinding>(R.layout.fragment_start_create_account) {
    private val viewModel by viewModels<AccountSharedViewModel>()

    @Inject
    lateinit var userPreferencesManager: UserPreferencesManager

    init {
        fragmentResultCompletion = { rs, b ->
            viewModel._resultSignInWithGoogleClient.postValue(
                if (b) NetworkResult.Success(rs) else NetworkResult.Error(rs)
            )
        }
    }

    override fun initObserver() {


        with(viewModel) {
            val owner = requireActivity()
            resultAuthenticateFacebookToken.observe(owner) { result ->
                handleNetworkResult(result)
            }

            resultAuthenticateGoogleToken.observe(owner) { result ->
                handleNetworkResult(result)
            }

            resultSignInWithFaceBookClient.observe(owner) { result ->
                handleSocialMediaSignInResult(result, AccountEvent.ClickSignInFacebook, owner)
            }

            _resultSignInWithGoogleClient.observe(owner) { result ->
                handleSocialMediaSignInResult(result, AccountEvent.ClickSignInGoogle, owner)
            }

            intentSenderRequestLiveData.observe(owner) { result ->
                if (result is NetworkResult.Success) {
                    launchRegister(result.data!!, Constants.RQE_GOOGLE)
                }
            }
        }
    }

    override fun getViewBinding(view: View): FragmentStartCreateAccountBinding {
        return FragmentStartCreateAccountBinding.bind(view)
    }

    override fun initView() {
        with(binding) {
            btnLoginGg.setOnClickListener {
                viewModel.handleAccountAction(SignUpAction.SignUpWithGoogle(requireActivity()))
            }

            btnLoginFb.setOnClickListener {
                viewModel.handleAccountAction(SignUpAction.SignUpWithFacebook(requireActivity()))
            }

            btnStart.setOnClickListener {
                launchFragment(R.id.action_startCreateAccountFragment_to_confirmNameFragment)
            }
            btnSignIn.setOnClickListener {
                requireActivity().startActivity(LoginActivity::class.java)
            }
            imgBack.setOnClickListener {
                requireActivity().finish()
            }

        }
    }

    private fun handleNetworkResult(
        result: NetworkResult<UserResponse>,
     ) {
        val activity = requireActivity()
        when (result) {

            is NetworkResult.Success -> {
                dismissDialogLoading()
                userPreferencesManager.saveUser(result.data!!.userRequest)
                activity.startActivity(MainActivity::class.java, false)
            }

            is NetworkResult.Error -> {
                dismissDialogLoading()
                activity.showMessage(result.message.toString())
            }

            is NetworkResult.Loading -> {
                activity.showDialogLoading()
            }
        }
    }



    override fun initData() {
    }
}