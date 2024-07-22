package com.image.effect.timewarp.scan.filtertiktok.face.filter.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.image.effect.timewarp.scan.filtertiktok.face.filter.R
import com.image.effect.timewarp.scan.filtertiktok.face.filter.model.TrendingVideo
import com.image.effect.timewarp.scan.filtertiktok.face.filter.util.Utils

class TrendingVideoItemAdapter(val context: Context, private val trendingVideos: List<TrendingVideo>, private val callback: OnItemClickCallback) :
    RecyclerView.Adapter<TrendingVideoItemAdapter.ViewHolder>() {

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

        val video = trendingVideos[position]
        Glide.with(context).load(video.thumbnail_url).into(holder.imageView)
        holder.itemView.setOnClickListener {
            callback.onItemVideoClick(position)
        }
    }

    override fun getItemCount(): Int {
        return trendingVideos.size
    }

    interface OnItemClickCallback {
        fun onItemVideoClick(position: Int)
    }
}