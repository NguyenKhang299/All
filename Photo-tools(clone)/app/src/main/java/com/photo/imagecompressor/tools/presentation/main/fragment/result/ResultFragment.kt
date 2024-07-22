package com.photo.imagecompressor.tools.presentation.main.fragment.result

import android.app.Activity.RESULT_OK
import android.graphics.Color
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.photo.imagecompressor.tools.base.BaseFragment
import com.photo.imagecompressor.tools.utils.Utils.Companion.fileToUri
import com.photo.imagecompressor.tools.utils.Utils.Companion.shareMultiple
import com.photo.imagecompressor.tools.utils.Utils.Companion.startToMain
import com.photo.imagecompressor.tools.utils.XToast
import com.photo.imagecompressor.tools.utils.clickIconNavigation
import com.photo.imagecompressor.tools.utils.setColorItemMenu
import com.photo.imagecompressor.tools.utils.setMenuToolbar
import com.photo.imagecompressor.tools.R
import com.photo.imagecompressor.tools.databinding.FragmentResultBinding
import com.photo.imagecompressor.tools.presentation.main.fragment.select_images.MainImageViewModel
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
        binding.title = getString(R.string.image_compressed)
        binding.selected = mainViewModel.getImagesSelected().size
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
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    rsViewModel.deletePhotoFromExternalStorage(
                        requireContext(),
                        mainViewModel.getImagesSelected().map { it.id.toLong() }.toList()
                    )
                } else {
                    showReplaceAlertDialog()
                }
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

    private fun showReplaceAlertDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(requireContext().getString(R.string.replace_))
        builder.setMessage(requireContext().getString(R.string.do_you_want_replace))
        builder.setPositiveButton(requireContext().getString(R.string.replace)) { dialog, which ->
            try {
                val resolver = requireActivity().contentResolver
                mainViewModel.imagesAfter.forEach {
                    val file = File(it.path)
                    val rs = resolver.delete(
                        Uri.withAppendedPath(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            it.id.toString()
                        ), null, null
                    )
                    MediaScannerConnection.scanFile(
                        context, arrayOf(file.toString()),
                        null, null
                    )
                }
                XToast.show(requireContext(), requireContext().getString(R.string.replace_success))
            } catch (e: Exception) {
                XToast.show(requireContext(), requireContext().getString(R.string.replace_error))
            }
        }
        builder.setNegativeButton(requireContext().getString(R.string.cancel)) { dialog, which ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

    override fun onResume() {
        super.onResume()
    }

    private val intentSenderLauncher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {
            if (it.resultCode == RESULT_OK) {
                XToast.show(requireContext(), requireContext().getString(R.string.replace_success))
            } else {
                XToast.show(requireContext(), requireContext().getString(R.string.replace_error))
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