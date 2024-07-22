package com.knd.duantotnghiep.phonetrackerlocation.ui.account.signUp.phoneNumber

import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import com.knd.duantotnghiep.phonetrackerlocation.R
import com.knd.duantotnghiep.phonetrackerlocation.core.BaseFragment
import com.knd.duantotnghiep.phonetrackerlocation.databinding.FragmentConfirmPhoneNumberBinding
import com.knd.duantotnghiep.phonetrackerlocation.models.UserRequest
import com.knd.duantotnghiep.phonetrackerlocation.ui.account.AccountAction.SignUpAction.ConfirmPhoneNumber
import com.knd.duantotnghiep.phonetrackerlocation.ui.account.AccountSharedViewModel
import com.knd.duantotnghiep.phonetrackerlocation.ui.account.signUp.SignUpActivity
import com.knd.duantotnghiep.phonetrackerlocation.utils.ActivityExtensions.showMessage
import com.knd.duantotnghiep.phonetrackerlocation.utils.NetworkResult
import com.knd.duantotnghiep.phonetrackerlocation.utils.ObserveError.observeErrorLiveData
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConfirmPhoneNumberFragment :
    BaseFragment<FragmentConfirmPhoneNumberBinding>(R.layout.fragment__confirm_phone_number) {
    private val viewModel by viewModels<ConfirmPhoneNumberViewModel>()
    private lateinit var newUser: UserRequest
    private lateinit var accountActivityViewModel: AccountSharedViewModel
    override fun initObserver() {
        with(viewModel) {
            val owner = requireActivity()

            owner.observeErrorLiveData(errorPhoneNumber, binding.edtPhoneNumber)
            owner.observeErrorLiveData(errorPassword, binding.edtPassword)

            sendOtpStatus.observe(owner) { result ->
                when (result) {
                    is NetworkResult.Success -> {
                        //return result
                        accountActivityViewModel.newUser.postValue(newUser.apply {
                            phoneNumber = viewModel.phoneNumber.value!!
                            password = viewModel.password.value!!
                        })
                        owner.showMessage(result.data!!.message)
                        binding.shimmer.hideShimmer()
                        launchFragment(R.id.action_confirmPhoneNumberFragment_to_confirmOTPFragment)
                    }

                    is NetworkResult.Error -> {
                        // Handle error: $result.message$a
                        owner.showMessage(result.message.toString())
                        binding.shimmer.hideShimmer()
                    }

                    is NetworkResult.Loading -> {
                        // Show loading UI
                        binding.shimmer.showShimmer(true)
                    }
                }
            }
        }
    }

    override fun getViewBinding(view: View): FragmentConfirmPhoneNumberBinding {
        return FragmentConfirmPhoneNumberBinding.bind(view)
    }

    override fun initView() {
        with(binding) {
            shimmer.hideShimmer()
            btnConfirm.setOnClickListener {
                edtPhoneNumber.isErrorEnabled = false
                edtPassword.isErrorEnabled = false
                this@ConfirmPhoneNumberFragment.viewModel.handel(ConfirmPhoneNumber.Confirm)
            }
        }
    }

    override fun initData() {
        binding.viewModel = viewModel
        val activity = requireActivity() as SignUpActivity
        accountActivityViewModel = activity.accountActivityViewModel
        newUser = accountActivityViewModel.newUser.value!!
    }

}