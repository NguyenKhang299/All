package com.knd.duantotnghiep.phonetrackerlocation.ui.account.signUp.otp

import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.fragment.app.viewModels
import com.knd.duantotnghiep.phonetrackerlocation.R
import com.knd.duantotnghiep.phonetrackerlocation.core.BaseFragment
import com.knd.duantotnghiep.phonetrackerlocation.databinding.FragmentConfirmOtpBinding
import com.knd.duantotnghiep.phonetrackerlocation.models.UserRequest
import com.knd.duantotnghiep.phonetrackerlocation.models.VerifyOTPRequest
import com.knd.duantotnghiep.phonetrackerlocation.ui.account.AccountAction
import com.knd.duantotnghiep.phonetrackerlocation.ui.account.AccountAction.SignUpAction.ConfirmPhoneNumber
import com.knd.duantotnghiep.phonetrackerlocation.ui.account.signIn.LoginActivity
import com.knd.duantotnghiep.phonetrackerlocation.ui.account.signUp.SignUpActivity
 import com.knd.duantotnghiep.phonetrackerlocation.utils.ActivityExtensions.showMessage
import com.knd.duantotnghiep.phonetrackerlocation.utils.ActivityExtensions.startActivity
import com.knd.duantotnghiep.phonetrackerlocation.utils.NetworkResult
import com.knd.duantotnghiep.phonetrackerlocation.utils.TextChangeListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConfirmOTPFragment : BaseFragment<FragmentConfirmOtpBinding>(R.layout.fragment_confirm_otp) {
    private val viewModel by viewModels<ConfirmOTPViewModel>()
    private lateinit var newUser: UserRequest
    private lateinit var countDownTimer: CountDownTimer

    override fun initObserver() {
        with(viewModel) {
            val activity = requireActivity()
            error.postValue("Please enter the OTP code sent to your phone number ${newUser.phoneNumber} to complete the registration.")
            sendOtpStatus.observe(activity) { result ->
                when (result) {
                    is NetworkResult.Success -> {
                        //return result
                        countDownTimer.start()

                    }

                    is NetworkResult.Error -> {
                        requireActivity().showMessage(result.message.toString())
                    }

                    is NetworkResult.Loading -> {
                        // Show loading UI
                    }
                }

            }
            verificationAndSignUpStatus.observe(activity) { result ->
                when (result) {
                    is NetworkResult.Success -> {
                        binding.number1.requestFocus()
                        binding.shimmer.hideShimmer()
                        activity.showMessage(result.data.toString())
                        activity.startActivity(LoginActivity::class.java)
                    }

                    is NetworkResult.Error -> {
                        activity.showMessage(result.message.toString())
                        binding.shimmer.hideShimmer()
                    }

                    is NetworkResult.Loading -> {
                        binding.shimmer.showShimmer(true)
                    }
                }
            }
        }
    }

    override fun getViewBinding(view: View): FragmentConfirmOtpBinding {
        return FragmentConfirmOtpBinding.bind(view)
    }

    override fun initView() {
        with(binding) {
            txtResend.isEnabled = false
            shimmer.hideShimmer()
            val viewModel = this@ConfirmOTPFragment.viewModel
            countDownTimer.start()
            txtResend.setOnClickListener {
                viewModel.handel(ConfirmPhoneNumber.SendOTP(newUser.phoneNumber))
                txtResend.isEnabled = false
            }

            btnConfirm.setOnClickListener {

                with(viewModel) {
                    val txtOTP =
                        txtOtp1.value + txtOtp2.value + txtOtp3.value + txtOtp4.value + txtOtp5.value + txtOtp6.value
                    val verifyOTPRequest = VerifyOTPRequest(newUser, txtOTP)
                    handel(ConfirmPhoneNumber.VerifyOTPAndSignUp(verifyOTPRequest))
                }
            }

            val change = TextChangeListener()
            val editTexts = listOf(number1, number2, number3, number4, number5, number6)
            change.onTextChange(editTexts) { txt, id ->
                val currentIndex = editTexts.indexOfFirst { it.id == id }
                this@ConfirmOTPFragment.viewModel.error.postValue("Please enter the OTP code sent to your phone number ${newUser.phoneNumber} to complete the registration.")

                if (txt.length == 1) {
                    if (currentIndex < editTexts.size - 1) {
                        editTexts[currentIndex + 1].requestFocus()
                    }
                } else {
                    if (currentIndex >= 1) {
                        editTexts[currentIndex - 1].requestFocus()
                    }
                }
            }
        }
    }

    private fun countdown(txtResend: TextView): CountDownTimer {
        val countDownTimer = object : CountDownTimer(60 * 2 * 1000, 1000) {
            override fun onTick(p0: Long) {
                txtResend.text = "Resend the code after ${(p0 / 1000).toInt()} seconds."
            }

            override fun onFinish() {
                txtResend.text = "Resend"
                txtResend.isEnabled = true
            }

        }
        return countDownTimer
    }


    override fun initData() {
        countDownTimer = countdown(binding.txtResend)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        val activity = requireActivity() as SignUpActivity
        val viewModel = activity.accountActivityViewModel
        newUser = viewModel.newUser.value!!
    }

}