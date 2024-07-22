package com.knd.duantotnghiep.testsocket

import com.knd.duantotnghiep.testsocket.core.BaseActivity
import com.knd.duantotnghiep.testsocket.databinding.ActivityMainBinding
import com.knd.duantotnghiep.testsocket.repository.impl.SocketRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {
    @Inject
    lateinit var socketRepository: SocketRepository

    override fun getViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }


}