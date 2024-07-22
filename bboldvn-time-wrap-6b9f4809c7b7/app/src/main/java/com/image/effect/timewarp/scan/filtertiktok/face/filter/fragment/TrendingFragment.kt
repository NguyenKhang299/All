package com.image.effect.timewarp.scan.filtertiktok.face.filter.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxError
import com.applovin.mediation.ads.MaxInterstitialAd
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.image.effect.timewarp.scan.filtertiktok.face.filter.Const
import com.image.effect.timewarp.scan.filtertiktok.face.filter.R
import com.image.effect.timewarp.scan.filtertiktok.face.filter.model.TrendingVideo
import com.image.effect.timewarp.scan.filtertiktok.face.filter.activity.TrendingPlayerActivity
import com.image.effect.timewarp.scan.filtertiktok.face.filter.activity.TrendingPlayerActivity.Companion.ExtraTrendingVideo
import com.image.effect.timewarp.scan.filtertiktok.face.filter.ad.BaseAdRevenueListener
import com.image.effect.timewarp.scan.filtertiktok.face.filter.ad.BaseMaxAdInterListener
import com.image.effect.timewarp.scan.filtertiktok.face.filter.ad.MaxAds
import com.image.effect.timewarp.scan.filtertiktok.face.filter.adapter.TrendingVideoItemAdapter
import com.image.effect.timewarp.scan.filtertiktok.face.filter.databinding.FragmentTrendingBinding
import com.image.effect.timewarp.scan.filtertiktok.face.filter.util.NetworkUtils

class TrendingFragment : Fragment() {
    val TAG = "TrendingFragment"

    private var binding: FragmentTrendingBinding? = null

    private var trendingVideos = ArrayList<TrendingVideo>()
    private lateinit var videoGridAdapter: TrendingVideoItemAdapter

    var interAd: MaxInterstitialAd? = null
    var choosingVideo: TrendingVideo? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentTrendingBinding.inflate(inflater, container, false)
        val view = binding?.root
        initViews()
        return view!!
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onResume() {
        super.onResume()
        if (binding?.swipeRefreshLayout?.isRefreshing == false && trendingVideos.size == 0) {
            loadData()
        }
        loadAd()
    }

    private fun initViews() {
        context?.let {
            videoGridAdapter = TrendingVideoItemAdapter(it, trendingVideos, object : TrendingVideoItemAdapter.OnItemClickCallback {
                override fun onItemVideoClick(position: Int) {
                    choosingVideo = trendingVideos[position]
                    showAdIdReady()
                }
            })
            binding?.recyclerView?.adapter = videoGridAdapter
            binding?.recyclerView?.layoutManager = GridLayoutManager(it, 2, GridLayoutManager.VERTICAL, false)
        }

        binding?.swipeRefreshLayout?.setOnRefreshListener {
            loadData()
        }
    }

    private fun openPlayer() {
        val intent = Intent(context, TrendingPlayerActivity::class.java).apply {
            putExtra(ExtraTrendingVideo, choosingVideo)
        }
        startActivity(intent)
    }

    private fun loadData() {
        Log.e(TAG, "LoadData: ...")
        binding?.txtMessage?.visibility = View.VISIBLE
        binding?.txtMessage?.text = getString(R.string.loading)
        binding?.swipeRefreshLayout?.isRefreshing = true

        if (!NetworkUtils.isOnline(context)) {
            binding?.txtMessage?.text = getString(R.string.network_error_msg)
            binding?.swipeRefreshLayout?.isRefreshing = false
            return
        }

        Firebase.database.reference.child("trending")
            .get()
            .addOnSuccessListener { snapshot ->
                val type = object : GenericTypeIndicator<List<TrendingVideo>>() {}
                val paths = snapshot.getValue(type)

                Handler(Looper.getMainLooper()).post {
                    trendingVideos.clear()
                    paths?.let {
                        trendingVideos.addAll(it)
                        trendingVideos.shuffle()
                    }
                    videoGridAdapter.notifyDataSetChanged()

                    if (trendingVideos.size == 0) {
                        binding?.txtMessage?.visibility = View.VISIBLE
                        binding?.txtMessage?.text = getString(R.string.no_video)
                    } else {
                        binding?.txtMessage?.visibility = View.GONE
                    }
                    binding?.swipeRefreshLayout?.isRefreshing = false
                }
            }
            .addOnFailureListener { ex ->
                ex.printStackTrace()
                binding?.txtMessage?.visibility = View.VISIBLE
                binding?.txtMessage?.text = getString(R.string.network_error_msg)
                binding?.swipeRefreshLayout?.isRefreshing = false
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

                    override fun onAdHidden(maxAd: MaxAd) {
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

    private fun showAdIdReady() {
        if (MaxAds.canShowAd(requireContext()) && interAd?.isReady == true) {
            interAd?.showAd("trending_video_clicked")
        } else {
            openPlayer()
        }
    }

}