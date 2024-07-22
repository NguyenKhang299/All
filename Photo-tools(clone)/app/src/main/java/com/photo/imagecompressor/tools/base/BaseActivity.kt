package com.photo.imagecompressor.tools.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.photo.imagecompressor.tools.utils.XToast

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity(){
    private var _binding: VB? = null
    protected val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = provideViewBinding().apply {
            setContentView(root)
        }

        setupObservers()

        setupView(savedInstanceState)
    }


    protected open fun setupObservers() {

    }

    open fun showMess(msg: String) {
        XToast.show(this, msg)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    protected abstract fun provideViewBinding(): VB

    protected abstract fun setupView(savedInstanceState: Bundle?)
}