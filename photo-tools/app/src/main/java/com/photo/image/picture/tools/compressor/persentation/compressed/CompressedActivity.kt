package com.photo.image.picture.tools.compressor.persentation.compressed

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.photo.image.picture.tools.compressor.databinding.FragmentCompressedBinding
import com.photo.image.picture.tools.compressor.base.BaseActivity
import com.photo.image.picture.tools.compressor.utils.Utils.Companion.fileToUri
import com.photo.image.picture.tools.compressor.utils.Utils.Companion.shareMultiple
import com.photo.image.picture.tools.compressor.utils.Utils.Companion.startToMain
import com.photo.image.picture.tools.compressor.utils.XToast
import com.photo.image.picture.tools.compressor.utils.clickIconNavigation
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class CompressedActivity : BaseActivity<FragmentCompressedBinding>(), ImageCompressedEvent {
    override fun provideViewBinding() = FragmentCompressedBinding.inflate(layoutInflater)
    private val compressedViewModel by viewModels<CompressedViewModel>()
    private val compressedAdapter by lazy { ImageCompressedAdapter(this@CompressedActivity) }
    override fun setupView(savedInstanceState: Bundle?) {
        compressedViewModel.getImageSave()
        binding.title = "Images Compressed"
        binding.includeToolbar.clickIconNavigation {
            startToMain()
        }

        binding.rcy.adapter = compressedAdapter
        showBannerAds(binding.bannerAds)
    }

    private val intentSenderLauncher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {
            if (it.resultCode == RESULT_OK) {
                compressedAdapter.submitData.removeAt(i)
                compressedAdapter.notifyItemRemoved(i)
                if (compressedAdapter.submitData.isEmpty()) {
                    binding.mLayoutNoFiles.visibility=if (compressedAdapter.submitData.isEmpty()) View.VISIBLE else View.GONE
                }
                 XToast.show(this, "Photo delete success")
            } else {
                XToast.show(this, "Photo delete failed")
            }
        }

    override fun setupObservers() {
        compressedViewModel.imagesLiveData.observe(this) {
            if (it.isEmpty()) {
                binding.mLayoutNoFiles.visibility=if (it.isEmpty()) View.VISIBLE else View.GONE
            }
            compressedAdapter.submitData = ArrayList(it)
        }

        compressedViewModel.launchIntentSenderLiveData.observe(this) {
            intentSenderLauncher.launch(it)
        }
    }

    override fun shareImage(uri: Uri) {
        shareMultiple(arrayListOf(uri))
    }

    var i = 0
    override fun deleteImage(id: Long, position: Int) {
        i = position
        compressedViewModel.deletePhotoFromExternalStorage(
            this,
            listOf(id)
        )
    }
}