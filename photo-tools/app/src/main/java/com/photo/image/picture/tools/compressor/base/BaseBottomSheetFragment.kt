package com.photo.image.picture.tools.compressor.base

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.FragmentActivity
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BaseBottomSheetFragment<VB : ViewBinding> :
    BottomSheetDialogFragment() {
    private var _binding: VB? = null
    val binding get() = _binding!!
    private lateinit var activity: FragmentActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = getViewBinding(inflater,container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activity = requireActivity()

        dialog?.setOnShowListener { dialogInterface ->
            val bottomSheetDialog = dialogInterface as BottomSheetDialog
            val bottomSheet =
                bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            if (bottomSheet != null) {
                BottomSheetBehavior.from(bottomSheet).apply {
                    state = BottomSheetBehavior.STATE_EXPANDED
                    peekHeight = 0
                }
            }
            val layoutParams = bottomSheet?.layoutParams
            bottomSheet?.setBackgroundColor(Color.TRANSPARENT)
            layoutParams?.height = WindowManager.LayoutParams.WRAP_CONTENT
            bottomSheet?.layoutParams = layoutParams
            isCancelable = true
        }

        super.onViewCreated(view, savedInstanceState)
        initData()
        initView()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    abstract fun getViewBinding(view: LayoutInflater, container: ViewGroup?): VB
    abstract fun initView()
    open fun initData() {}
    open fun initObserve() {}
}