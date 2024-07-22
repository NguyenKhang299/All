package com.knd.duantotnghiep.phonetrackerlocation.ui.account.signUp.name

import android.view.View
import androidx.fragment.app.viewModels
import com.knd.duantotnghiep.phonetrackerlocation.R
import com.knd.duantotnghiep.phonetrackerlocation.core.BaseFragment
import com.knd.duantotnghiep.phonetrackerlocation.databinding.FragmentConfirmNameBinding
import com.knd.duantotnghiep.phonetrackerlocation.models.UserRequest
import com.knd.duantotnghiep.phonetrackerlocation.ui.account.signUp.SignUpActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConfirmNameFragment : BaseFragment<FragmentConfirmNameBinding>(R.layout.fragment_confirm_name) {
    private val viewModel by viewModels<ConfirmNameViewModel>()
    override fun initObserver() {
        with(viewModel) {
             actionNext.observe(requireActivity()) {
                if (it) {
                    (requireActivity() as SignUpActivity).accountActivityViewModel.newUser.postValue(
                        UserRequest(name = txtFirstName.value + " " + txtLastName.value)
                    )
                    actionNext.postValue(false)
                    launchFragment(R.id.action_confirmNameFragment_to_confirmSexFragment)
                }
            }
        }
    }

    override fun getViewBinding(view: View): FragmentConfirmNameBinding {
        return FragmentConfirmNameBinding.bind(view)
    }


    override fun initView() {}

    override fun initData() {
        binding.viewModel = viewModel
        binding.lifecycleOwner=this
    }

}