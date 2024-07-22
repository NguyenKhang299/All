package com.photo.image.picture.tools.compressor.utils

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.photo.image.picture.tools.compressor.R
import com.photo.image.picture.tools.compressor.persentation.main.fragment.compress_images.SelectCompressionFragmentDirections
import com.photo.image.picture.tools.compressor.persentation.main.fragment.crop_compress.CropImageFragmentDirections
import com.photo.image.picture.tools.compressor.persentation.main.fragment.select_images.MainPickFragmentDirections

fun Fragment.startToImageFolder(idBucket: Int, nameBucket: String) {
     val action =
        MainPickFragmentDirections.actionMainPickFragmentToImageFolderFragment(idBucket, nameBucket)
    findNavController().navigate(action)
}

fun Fragment.popBackStack() {
    findNavController().popBackStack()
}

fun Fragment.startToCompressingFromSelectCompression() {
     findNavController().navigate(R.id.action_selectCompression_to_compressingFragment)
}

fun Fragment.startToSelectCompressionFromCrop() {
     findNavController().navigate(R.id.action_cropImageFragment_to_selectCompression)
}
fun Fragment.startToResults(){
    findNavController().navigate(R.id.action_compressingDialogFragment_to_resultFragment)
}
