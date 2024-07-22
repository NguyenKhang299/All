package com.photo.imagecompressor.tools.utils

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.photo.imagecompressor.tools.R
import com.photo.imagecompressor.tools.presentation.main.fragment.select_images.MainPickFragmentDirections

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
