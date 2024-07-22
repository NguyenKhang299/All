package com.photo.image.picture.tools.compressor.persentation.main.fragment.result

import android.app.Activity.RESULT_OK
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.photo.image.picture.tools.compressor.R
import com.photo.image.picture.tools.compressor.base.BaseFragment
import com.photo.image.picture.tools.compressor.databinding.FragmentResultBinding
import com.photo.image.picture.tools.compressor.persentation.main.fragment.select_images.MainImageViewModel
import com.photo.image.picture.tools.compressor.persentation.main.fragment.select_images.MainPickImageActivity
import com.photo.image.picture.tools.compressor.utils.Utils.Companion.fileToUri
import com.photo.image.picture.tools.compressor.utils.Utils.Companion.shareMultiple
import com.photo.image.picture.tools.compressor.utils.Utils.Companion.startToMain
import com.photo.image.picture.tools.compressor.utils.XToast
import com.photo.image.picture.tools.compressor.utils.clickIconNavigation
import com.photo.image.picture.tools.compressor.utils.setColorItemMenu
import com.photo.image.picture.tools.compressor.utils.setMenuToolbar
import java.io.File
import java.util.ArrayList


class ResultFragment : BaseFragment<FragmentResultBinding>() {
    override fun provideViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentResultBinding.inflate(inflater, container, false)

    private val mainViewModel by activityViewModels<MainImageViewModel>()
    private val rsViewModel by viewModels<RsViewModel>()
    private val rsAdapter by lazy { RsAdapter(requireContext()) }

    override fun setupView(view: View, savedInstanceState: Bundle?) {
        binding.title = "Images Compressed"
        binding.selected = mainViewModel.getImagesSelected().size
        (requireActivity() as MainPickImageActivity).showNativeAds(binding.nativeAd)
        binding.rcyResult.adapter = rsAdapter
        binding.includeToolbar.clickIconNavigation {
            requireActivity().startToMain()
        }
        binding.includeToolbar.mToolbar.setMenuToolbar(R.menu.menu_rs) {
            if (it.itemId == R.id.share) {
                val listUri = mainViewModel.imagesAfter.map {
                    requireContext().fileToUri(File(it.path))
                }.toList()
                requireContext().shareMultiple(listUri)
            } else {
                rsViewModel.deletePhotoFromExternalStorage(
                    requireContext(),
                    mainViewModel.getImagesSelected().map { it.id.toLong() }.toList()
                )
            }
            false
        }
        binding.includeToolbar.mToolbar.setColorItemMenu(R.id.share, Color.WHITE)
        binding.includeToolbar.mToolbar.setColorItemMenu(R.id.replace, Color.WHITE)
        rsViewModel.postResult(mainViewModel.getImagesSelected(), mainViewModel.imagesAfter)
        binding.show.setOnClickListener {
            findNavController().navigate(R.id.action_resultFragment_to_compareFragment)
        }

    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as MainPickImageActivity).hideBanner(true)
    }

    private val intentSenderLauncher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {
            if (it.resultCode == RESULT_OK) {
                XToast.show(requireContext(), "Photo replace success")
            } else {
                XToast.show(requireContext(), "Photo couldn't be replace")
            }
        }


    override fun setupObservers() {
        rsViewModel.resultLiveData.observe(this) {
            rsAdapter.submitData = ArrayList(it)
        }

        rsViewModel.launchIntentSenderLiveData.observe(this) {
            intentSenderLauncher.launch(it)
        }
    }
}