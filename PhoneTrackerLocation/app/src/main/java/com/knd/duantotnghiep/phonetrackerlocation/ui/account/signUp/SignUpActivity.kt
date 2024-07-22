package com.knd.duantotnghiep.phonetrackerlocation.ui.account.signUp

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.knd.duantotnghiep.phonetrackerlocation.R
import com.knd.duantotnghiep.phonetrackerlocation.core.BaseActivity
import com.knd.duantotnghiep.phonetrackerlocation.core.BaseFragment
import com.knd.duantotnghiep.phonetrackerlocation.databinding.AcitivitySignupBinding
import com.knd.duantotnghiep.phonetrackerlocation.ui.account.AccountSharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpActivity : BaseActivity<AcitivitySignupBinding>() {
    val accountActivityViewModel by viewModels<AccountSharedViewModel>()
    private lateinit var appBarConfiguration: AppBarConfiguration
    override fun getViewBinding(): AcitivitySignupBinding {
        return AcitivitySignupBinding.inflate(layoutInflater)
    }

    override fun initView() {
        binding.lifecycleOwner=this
        with(binding) {

            setUpToolBar(toolBar)
            //setUpNavigation
            val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
            val navController = navHostFragment.navController
            appBarConfiguration = AppBarConfiguration(navController.graph)
            toolBar.setupWithNavController(navController, appBarConfiguration)

            navController.addOnDestinationChangedListener { _, destination, _ ->
                val isDefaultFragment = destination.id == R.id.startCreateAccountFragment
                toolBar.visibility = if (isDefaultFragment) View.GONE else View.VISIBLE
            }

            onBackPressedDispatcher.addCallback(this@SignUpActivity,
                object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        finish()
                    }
                })

            toolBar.setNavigationOnClickListener {
                finish()
            }
        }
    }


//    override fun onSupportNavigateUp(): Boolean {
//        //Nếu việc điều hướng thành công, navigateUp trả về true.
//        // Nếu không, chúng ta gọi super.onSupportNavigateUp() để
//        // xử lý mặc định khi không có điều hướng nào xảy ra.
//        val navController = findNavController(R.id.fragmentContainerView)
//        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
//    }


}