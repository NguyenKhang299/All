package com.photo.image.picture.tools.compressor.base

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.access.pro.adcontrol.AdsBannerView
import com.access.pro.callBack.OnShowInterstitialListener
import com.access.pro.callBack.OnShowNativeListener
import com.google.android.gms.ads.nativead.NativeAd
import com.photo.image.picture.tools.compressor.R
import com.photo.image.picture.tools.compressor.utils.XToast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

abstract class BaseFragment<VB : ViewBinding> : Fragment() {

    private var _binding: VB? = null
    protected val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupObservers()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = provideViewBinding(inflater, container)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(view, savedInstanceState)
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

    open fun showBannerAds(viewContainer: ViewGroup) {
        if (!BaseActivity.IS_SUB_VIP && BaseActivity.ENABLE_ADS) {
            val banner = AdsBannerView.getView(requireActivity().windowManager, requireActivity(), viewContainer)
            AdsBannerView.loadAds(AdsBannerView.BANNER_BOTTOM, banner)
        }
    }
}