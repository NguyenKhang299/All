package com.knd.duantotnghiep.phonetrackerlocation.core

import android.app.Activity
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBar.DISPLAY_HOME_AS_UP
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.common.api.ApiException
import com.knd.duantotnghiep.phonetrackerlocation.ui.dialog.DialogLoading
import com.knd.duantotnghiep.phonetrackerlocation.utils.Constants
import dagger.hilt.android.AndroidEntryPoint

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {
    private var _binding: VB? = null

//    open var activityResultCompletion: (String?, Boolean) -> Unit = { param, s -> }

    protected   val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = getViewBinding()
        setContentView(binding.root)
        initData()
        initView()
        initObserver()
    }

    fun setUpToolBar(toolbar: Toolbar, enableDisplayHome: Boolean = false, draw: Drawable? = null) {
        setSupportActionBar(toolbar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(draw)
            it.setDisplayShowHomeEnabled(true)
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (getMenu() != -1 && menu != null) {
            menuInflater.inflate(getMenu(), menu)
            this.menu = menu
            return true
        }
        return super.onCreateOptionsMenu(menu)
    }

    protected lateinit var menu: Menu
    abstract fun getViewBinding(): VB
    protected open fun getMenu(): Int = -1
    protected open fun initView() {}
    protected  open fun initObserver() {}
    protected open fun initData() {}

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}