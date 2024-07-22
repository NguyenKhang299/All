package com.knd.duantotnghiep.phonetrackerlocation.ui.account.signUp.sex

import android.util.Log
import android.view.View
import com.knd.duantotnghiep.phonetrackerlocation.R
import com.knd.duantotnghiep.phonetrackerlocation.core.BaseFragment
import com.knd.duantotnghiep.phonetrackerlocation.databinding.FragmentConfirmSexBinding
import com.knd.duantotnghiep.phonetrackerlocation.models.Sex
import com.knd.duantotnghiep.phonetrackerlocation.models.UserRequest
import com.knd.duantotnghiep.phonetrackerlocation.ui.account.signUp.SignUpActivity
 import dagger.hilt.android.AndroidEntryPoint

class ConfirmSexFragment : BaseFragment<FragmentConfirmSexBinding>(R.layout.fragment_confirm_sex) {

    override fun getViewBinding(view: View): FragmentConfirmSexBinding {
        return FragmentConfirmSexBinding.bind(view)
    }

    override fun initView() {

        val activity = requireActivity() as SignUpActivity
        val viewModel = activity.accountActivityViewModel
        with(binding) {
            btnConfirm.setOnClickListener {
                viewModel.newUser.postValue(viewModel.newUser.value!!.apply {
                    val check = rdiGroup.checkedRadioButtonId == rdiMale.id
                    sex = if (check) Sex.MALE  else Sex.FEMALE
                })
                launchFragment(R.id.action_confirmSexFragment_to_confirmPhoneNumberFragment)
            }
        }
    }
}