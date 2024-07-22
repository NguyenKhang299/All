package com.photo.image.picture.tools.compressor.base

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.viewbinding.ViewBinding
import com.photo.image.picture.tools.compressor.utils.XToast
import javax.inject.Inject

abstract class BaseDialogFragment<VB : ViewBinding> : DialogFragment() {

    protected val binding get() = _viewBinding!!
    private var _viewBinding: VB? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupObservers()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _viewBinding = provideViewBinding(inflater, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupView(view, savedInstanceState)

        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog!!.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        super.onViewCreated(view, savedInstanceState)
    }


    protected open fun setupObservers() {

    }

    open fun showMess(msg: String) {
        XToast.show(requireContext(), msg)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }


    protected abstract fun provideViewBinding(inflater: LayoutInflater, container: ViewGroup?): VB

    protected abstract fun setupView(view: View, savedInstanceState: Bundle?)
}