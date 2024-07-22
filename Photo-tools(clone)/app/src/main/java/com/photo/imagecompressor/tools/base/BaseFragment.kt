package com.photo.imagecompressor.tools.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.photo.imagecompressor.tools.utils.XToast

abstract class BaseFragment<VB : ViewBinding> : Fragment() {

    private var _binding: VB? = null
    protected val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = provideViewBinding(inflater, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(view, savedInstanceState)
        setupObservers()
    }

    protected open fun setupObservers() {}

    open fun showMess(msg: String) {
        XToast.show(requireContext(), msg)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    protected abstract fun provideViewBinding(inflater: LayoutInflater, container: ViewGroup?): VB


    protected abstract fun setupView(view: View, savedInstanceState: Bundle?)


}