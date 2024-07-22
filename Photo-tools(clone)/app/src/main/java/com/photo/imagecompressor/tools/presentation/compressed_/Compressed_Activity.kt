package com.photo.imagecompressor.tools.presentation.compressed_

import android.app.Activity.RESULT_OK
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
import com.photo.imagecompressor.tools.R
import com.photo.imagecompressor.tools.utils.Utils.Companion.shareMultiple
import com.photo.imagecompressor.tools.utils.XToast
import com.photo.imagecompressor.tools.base.BaseFragment
import com.photo.imagecompressor.tools.databinding.FragmentCompressedBinding
import com.photo.imagecompressor.tools.domain.model.Image_
import com.photo.imagecompressor.tools.utils.PermUtils
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class Compressed_Activity : BaseFragment<FragmentCompressedBinding>(), ImageCompressedEvent {
    private val compressedViewModel by activityViewModels<Compressed_ViewModel>()
    private val compressedAdapter by lazy { ImageCompressedAdapter(this@Compressed_Activity) }

    override fun setupView(view: View, savedInstanceState: Bundle?) {
        if (PermUtils.checkImageAccessPermissions(requireContext())) {
            compressedViewModel.getImageSave()
        } else {
            binding.mLayoutNoFiles.visibility = View.VISIBLE
        }
        binding.title = getString(R.string.image_compressed)
        binding.rcy.adapter = compressedAdapter
    }

    private val intentSenderLauncher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {
            if (this::image.isInitialized) compressedViewModel._deleteImage.postValue(
                Pair(
                    it.resultCode == RESULT_OK,
                    image
                )
            )
        }

    override fun setupObservers() {
        compressedViewModel.imagesLiveData.observe(this) {
            if (it.isEmpty()) {
                binding.mLayoutNoFiles.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
            }
            compressedAdapter.submitData = ArrayList(it)
        }

        compressedViewModel.launchIntentSenderLiveData.observe(this) {
            intentSenderLauncher.launch(it)
        }
        compressedViewModel._deleteImage.observe(this) {
            if (this::image.isInitialized) {
                val index = compressedAdapter.submitData.indexOf(image)
                if (it.first && index != -1) {
                    compressedAdapter.submitData.removeAt(index)
                    compressedAdapter.notifyItemRemoved(index)
                    if (compressedAdapter.submitData.isEmpty()) {
                        binding.mLayoutNoFiles.visibility =
                            if (compressedAdapter.submitData.isEmpty()) View.VISIBLE else View.GONE
                    }
                    XToast.show(requireActivity(), "Photo delete success")
                } else {
                    XToast.show(requireActivity(), "Photo delete failed")
                }
            }
        }
    }

    override fun provideViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentCompressedBinding.inflate(inflater, container, false)


    override fun shareImage(uri: Uri) {
        requireActivity().shareMultiple(arrayListOf(uri))
    }

    lateinit var image: Image_
    override fun deleteImage(image: Image_, position: Int) {
        this.image = image
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            compressedViewModel.deletePhotoFromExternalStorage(
                requireContext(),
                image
            )
        } else {
            showReplaceAlertDialog()
        }
    }

    private fun showReplaceAlertDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(requireContext().getString(R.string.delete))
        builder.setMessage(requireContext().getString(R.string.do_you_want_delete_image))
        builder.setPositiveButton(requireContext().getString(R.string.delete)) { dialog, which ->
            try {
                val resolver = requireActivity().contentResolver
                val file = File(image.path)
                val rs = resolver.delete(
                    Uri.withAppendedPath(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        image.id.toString()
                    ), null, null
                )
                MediaScannerConnection.scanFile(
                    context, arrayOf(file.toString()),
                    null, null
                )
                compressedViewModel._deleteImage.postValue(Pair(rs > 0, image))
            } catch (e: Exception) {
                compressedViewModel._deleteImage.postValue(Pair(false, image))
            }
        }
        builder.setNegativeButton(requireContext().getString(R.string.cancel)) { dialog, which ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }
}