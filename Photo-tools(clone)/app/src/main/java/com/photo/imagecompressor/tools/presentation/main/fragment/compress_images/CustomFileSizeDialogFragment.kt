package com.photo.imagecompressor.tools.presentation.main.fragment.compress_images

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.photo.imagecompressor.tools.base.BaseDialogFragment
import com.photo.imagecompressor.tools.databinding.DialogFragmentCustomFileSizeBinding

interface ConfirmedFileSizeListener {
    fun onConfirmed(size: Long, unit: String)
}

class CustomFileSizeDialogFragment(private val confirmedFileSizeListener: ConfirmedFileSizeListener) :
    BaseDialogFragment<DialogFragmentCustomFileSizeBinding>(),
    AdapterView.OnItemSelectedListener {
    override fun provideViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = DialogFragmentCustomFileSizeBinding.inflate(inflater, container, false)

    private var unitSelected: String = "KB"
    private var unit = arrayOf(
        "KB", "MB"
    )

    override fun setupView(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            setAdapterSpinner()
            btnCompress.setOnClickListener {
                val size = edtSize.text.toString()
                if (size.isNotEmpty()) {
                    confirmedFileSizeListener.onConfirmed(size.toLong(), unitSelected)
                    dismiss()
                }
            }
        }
    }

    private fun setAdapterSpinner() {
        val ad = ArrayAdapter(
            requireActivity(),
            android.R.layout.simple_spinner_item,
            unit
        )
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner.adapter = ad
        binding.spinner.onItemSelectedListener = this
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        unitSelected = unit[position]

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

}