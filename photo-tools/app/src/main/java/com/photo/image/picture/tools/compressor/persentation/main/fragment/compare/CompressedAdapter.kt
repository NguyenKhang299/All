package com.photo.image.picture.tools.compressor.persentation.main.fragment.compare

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import com.photo.image.picture.tools.compressor.base.BaseAdapter
import com.photo.image.picture.tools.compressor.databinding.ItemResult2Binding
import com.photo.image.picture.tools.compressor.domain.model.Image
import com.photo.image.picture.tools.compressor.utils.Utils
import com.photo.image.picture.tools.compressor.utils.Utils.Companion.fileToUri
import java.io.File


class CompressedAdapter(private val context: Context) : BaseAdapter<ItemResult2Binding, Image>() {
    override fun getViewBinding(inflater: LayoutInflater, parent: ViewGroup) =
        ItemResult2Binding.inflate(inflater, parent, false)

    override fun onBind(binding: ItemResult2Binding, data: Image, position: Int) {
        Utils.loadImage(binding.img, data.path)
        val txtPath = "<u>${data.path}</u>"
        binding.txtPath.text = HtmlCompat.fromHtml(txtPath, HtmlCompat.FROM_HTML_MODE_LEGACY)
        binding.txtPath.setOnClickListener {
            val file = File(data.path)
            val uri = context.fileToUri(file)
            val intent = Intent(Intent.ACTION_VIEW )
            intent.setDataAndType(uri, "image/*")
            intent.flags=Intent.FLAG_GRANT_READ_URI_PERMISSION
            context.startActivity(Intent.createChooser(intent,"View using"))
        }
    }
}