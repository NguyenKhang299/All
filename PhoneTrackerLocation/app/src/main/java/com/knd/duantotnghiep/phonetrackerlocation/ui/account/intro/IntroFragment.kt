package com.knd.duantotnghiep.phonetrackerlocation.ui.account.intro


import android.util.Log
import com.knd.duantotnghiep.phonetrackerlocation.core.BaseActivity
import com.knd.duantotnghiep.phonetrackerlocation.databinding.ActivityIntroBinding
import com.knd.duantotnghiep.phonetrackerlocation.respository.SocketRepository
import com.knd.duantotnghiep.phonetrackerlocation.ui.account.signUp.SignUpActivity
import com.knd.duantotnghiep.phonetrackerlocation.utils.ActivityExtensions.startActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
@AndroidEntryPoint
class IntroActivity : BaseActivity<ActivityIntroBinding>() {

    override fun getViewBinding(): ActivityIntroBinding {
        return ActivityIntroBinding.inflate(layoutInflater)
    }

    override fun initView() {
        with(binding) {

            binding.btnGetStarted.setOnClickListener {
                startActivity(SignUpActivity::class.java, false)
            }

            vPager.adapter = IntroAdapter(supportFragmentManager, lifecycle)
            circleIndicator3.setViewPager(vPager)
        }
    }
}