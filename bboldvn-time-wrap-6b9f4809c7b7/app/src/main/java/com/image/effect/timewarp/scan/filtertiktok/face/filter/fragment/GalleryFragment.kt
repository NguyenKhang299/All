package com.image.effect.timewarp.scan.filtertiktok.face.filter.fragment

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxError
import com.applovin.mediation.ads.MaxInterstitialAd
import com.image.effect.timewarp.scan.filtertiktok.face.filter.Const
import com.image.effect.timewarp.scan.filtertiktok.face.filter.R
import com.image.effect.timewarp.scan.filtertiktok.face.filter.activity.MainActivity
import com.image.effect.timewarp.scan.filtertiktok.face.filter.activity.ShareActivity
import com.image.effect.timewarp.scan.filtertiktok.face.filter.ad.BaseAdRevenueListener
import com.image.effect.timewarp.scan.filtertiktok.face.filter.ad.BaseMaxAdInterListener
import com.image.effect.timewarp.scan.filtertiktok.face.filter.ad.MaxAds
import com.image.effect.timewarp.scan.filtertiktok.face.filter.adapter.GalleryVideoItemAdapter
import com.image.effect.timewarp.scan.filtertiktok.face.filter.databinding.FragmentGalleryBinding
import com.image.effect.timewarp.scan.filtertiktok.face.filter.util.FileUtils
import com.image.effect.timewarp.scan.filtertiktok.face.filter.util.PermUtils
import com.tbruyelle.rxpermissions3.RxPermissions
import java.util.*
import java.util.concurrent.Executors

class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!

    private var videoPaths = ArrayList<String>()
    private lateinit var videoGridAdapter: GalleryVideoItemAdapter

    var interAd: MaxInterstitialAd? = null
    var choosingVideoPath: String? = null
    var _needRequestPermission = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        val view = binding.root
        initViews()
        return view
    }

    private fun initViews() {
        context?.let {
            videoGridAdapter = GalleryVideoItemAdapter(it, videoPaths, object : GalleryVideoItemAdapter.OnItemClickCallback {
                override fun onItemVideoClick(position: Int) {
                    choosingVideoPath = videoPaths[position]
                    showAdIfReady()
                }
            })
            binding.recyclerView.adapter = videoGridAdapter
            binding.recyclerView.layoutManager = GridLayoutManager(it, 2, GridLayoutManager.VERTICAL, false)
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            loadData()
        }
    }

    override fun onResume() {
        super.onResume()
        loadAd()

        if (_needRequestPermission) {
            _needRequestPermission = false
            RxPermissions(this)
                .requestEachCombined(WRITE_EXTERNAL_STORAGE)
                .subscribe { permission ->
                    if (permission.granted) {
                        loadData()
                    } else if (permission.shouldShowRequestPermissionRationale) {
                        Toast.makeText(activity, "Permissions denied", Toast.LENGTH_LONG).show()
                        backToPage0()
                        _needRequestPermission = true
                    } else {
                        PermUtils.showDialogPermissionDenied(activity) {
                            backToPage0()
                            _needRequestPermission = true
                        }
                    }
                }
        }
    }

    private fun backToPage0() {
        try {
            (activity as MainActivity).binding.viewpager.currentItem = 0
        } catch (_: Exception) {
        }
    }

    private fun openPlayer() {
        val intent = Intent(context, ShareActivity::class.java).apply {
            putExtra("source_file", choosingVideoPath)
        }
        startActivity(intent)
    }

    private fun loadData() {
        binding.txtMessage.text = getString(R.string.loading)
        Executors.newSingleThreadExecutor().execute {
            val dir = FileUtils.getExportDir()
            val fileList = dir.listFiles()
            fileList?.let {
                Arrays.sort(it) { a, b -> java.lang.Long.compare(b.lastModified(), a.lastModified()) }
                Handler(Looper.getMainLooper()).post {
                    val paths = fileList.map { it -> it.absolutePath }
                    videoPaths.clear()
                    videoPaths.addAll(paths)
                    videoGridAdapter.notifyDataSetChanged()

                    if (videoPaths.size == 0) {
                        binding.txtMessage.visibility = View.VISIBLE
                        binding.txtMessage.text = getString(R.string.no_video_tap_to_record)
                    } else {
                        binding.txtMessage.visibility = View.GONE
                    }
                    binding.swipeRefreshLayout.isRefreshing = false
                }
            }
        }
    }

    private fun loadAd() {
        if (MaxAds.canShowAd(requireContext())) {
            if (interAd == null) {
                interAd = MaxInterstitialAd(Const.MaxFullId, activity)
                interAd?.setRevenueListener(BaseAdRevenueListener())
                interAd?.setListener(object : BaseMaxAdInterListener(interAd) {
                    override fun onAdDisplayFailed(p0: MaxAd, p1: MaxError) {
                        super.onAdDisplayFailed(p0, p1)
                        openPlayer()
                        interAd?.loadAd()
                    }

                    override fun onAdHidden(maxAd: com.applovin.mediation.MaxAd) {
                        super.onAdHidden(maxAd)
                        openPlayer()
                        interAd?.loadAd()
                    }
                })
            }
            if (interAd?.isReady == false) {
                interAd?.loadAd()
            }
        }
    }

    private fun showAdIfReady() {
        if (MaxAds.canShowAd(requireContext()) && interAd?.isReady == true) {
            interAd?.showAd("gallery_video_clicked")
        } else {
            openPlayer()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}