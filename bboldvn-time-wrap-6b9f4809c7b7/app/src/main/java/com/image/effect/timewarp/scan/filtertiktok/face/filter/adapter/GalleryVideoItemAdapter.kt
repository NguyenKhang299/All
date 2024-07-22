package com.image.effect.timewarp.scan.filtertiktok.face.filter.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.image.effect.timewarp.scan.filtertiktok.face.filter.R
import com.image.effect.timewarp.scan.filtertiktok.face.filter.util.Utils
import java.io.File

class GalleryVideoItemAdapter(val context: Context, private val videoPaths: List<String>, private val callback: OnItemClickCallback) :
    RecyclerView.Adapter<GalleryVideoItemAdapter.ViewHolder>() {

    private val inflater = LayoutInflater.from(context)

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView = itemView.findViewById(R.id.imageView) as AppCompatImageView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val contactView: View = inflater.inflate(R.layout.item_grid, parent, false)
        return ViewHolder(contactView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.layoutParams?.height = Utils.screenWidth() / 2 * 3 / 2

        val videoPath = videoPaths[position]
        if(videoPath.startsWith("http")) {
            Glide.with(context).load(videoPath).into(holder.imageView)
        } else {
            Glide.with(context).load(Uri.fromFile(File(videoPath))).into(holder.imageView)
        }
        holder.itemView.setOnClickListener {
            callback.onItemVideoClick(position)
        }
    }

    override fun getItemCount(): Int {
        return videoPaths.size
    }

    interface OnItemClickCallback {
        fun onItemVideoClick(position: Int)
    }
}